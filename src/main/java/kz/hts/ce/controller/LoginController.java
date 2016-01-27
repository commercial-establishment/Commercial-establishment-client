package kz.hts.ce.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.model.entity.Employee;
import kz.hts.ce.model.entity.Shift;
import kz.hts.ce.service.EmployeeService;
import kz.hts.ce.service.ShiftService;
import kz.hts.ce.service.ShopService;
import kz.hts.ce.service.TransferService;
import kz.hts.ce.util.spring.SpringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaUtil.checkConnection;
import static kz.hts.ce.util.JavaUtil.getDateFromInternet;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;
import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;
import static kz.hts.ce.util.spring.SpringHelper.getPrincipal;

@Controller
public class LoginController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label message;

    @Autowired
    private ShiftService shiftService;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SpringHelper springHelper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    loginAction();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    @Transactional
    private void loginAction() throws IOException {
        try {
            springHelper.authorize(username.getText(), password.getText());

            Employee employee = employeeService.findByUsername(getPrincipal());

            Shift shiftByLastEndDate = shiftService.findByLastEndDate();
            Shift newShift = shiftService.openNewShift(employee);

            checkAndAuthorize(shiftByLastEndDate, newShift, employee);
        } catch (NullPointerException | UsernameNotFoundException e) {
            message.setText("Неверное имя пользователя или пароль:");
        }
    }

    private boolean checkShopLockDateFromServer(Employee employee) {
        Date lockDate = employee.getShop().getLockDate();
        Date currentDateFromInternet = getDateFromInternet();
        return currentDateFromInternet.after(lockDate);
    }

    private boolean checkLocalShopLockDate(Employee employee) {
        Date lockDate = employee.getShop().getLockDate();
        Date currentLocalDate = new Date();
        return currentLocalDate.after(lockDate);
    }

    private void lockEmployeeAndShowAlertMessage(Employee employee, String title, String body) {
        if (!employee.isBlocked()) employeeService.lockById(employee.getId());
        alert(Alert.AlertType.WARNING, title, null, body);
    }

    private void checkAndAuthorize(Shift shiftByLastEndDate, Shift newShift, Employee employee) {
        if (newShift.getStart().before(shiftByLastEndDate.getEnd())) {
            lockEmployeeAndShowAlertMessage(employee, "Дата на вашем компьютере была изменена.",
                    "В целях корректности отчётов вы были заблокированы. Пожалуйста, обратитесь в службу поддержки.");
        } else if (newShift.getStart().after(shiftByLastEndDate.getEnd()) && checkLocalShopLockDate(employee)) {
            lockEmployeeAndShowAlertMessage(employee, "Срок действия окончен",
                    "Извините, срок действия программой закончен. Пожалуйста, обратитесь в службу поддержки.");
        } else {
            if (checkShopLockDateFromServer(employee) && checkConnection()) {
                lockEmployeeAndShowAlertMessage(employee, "Срок действия окончен",
                        "Извините, срок действия программой закончен. Пожалуйста, обратитесь в службу поддержки.");
            } else {
                PagesConfiguration screens = getPagesConfiguration();
                Stage stage = new Stage();
                screens.setPrimaryStage(stage);
                Shift shift = shiftService.save(newShift);

                springHelper.setPassword(password.getText());
                springHelper.setEmployee(employee);
                springHelper.setShift(shift);

                screens.login().hide();
                screens.main().show();
                springHelper.transmitAndReceiveData();
                message.setText("");
            }
        }
    }
}
