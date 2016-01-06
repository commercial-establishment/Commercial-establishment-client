package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.controller.sale.CalculatorController;
import kz.hts.ce.model.entity.Broadcast;
import kz.hts.ce.model.entity.Employee;
import kz.hts.ce.model.entity.Shift;
import kz.hts.ce.service.BroadcastService;
import kz.hts.ce.service.EmployeeService;
import kz.hts.ce.service.ShiftService;
import kz.hts.ce.util.spring.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

import static kz.hts.ce.util.JavaUtil.checkConnection;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;
import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;
import static kz.hts.ce.util.spring.SpringUtil.getPrincipal;

@Controller
public class LoginController {

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
    private CalculatorController calculatorController;

    @Autowired
    private SpringUtil springUtils;

    @FXML
    @Transactional
    private void loginAction(ActionEvent event) throws IOException {
        try {
            PagesConfiguration screens = getPagesConfiguration();
            springUtils.authorize(username.getText(), password.getText());
            springUtils.setPassword(password.getText());
            Stage stage = new Stage();
            screens.setPrimaryStage(stage);

            Shift shiftEntity = new Shift();
            shiftEntity.setStart(new Date());
            Employee employee = employeeService.findByUsername(getPrincipal());
            springUtils.setEmployee(employee);
            shiftEntity.setEmployee(employee);
            Shift shift = shiftService.save(shiftEntity);
            springUtils.setShift(shift);

            if (checkConnection()) {
                springUtils.checkAndUpdateNewDataFromServer();
            } else {
                alert(Alert.AlertType.WARNING, "Проверьте интернет соединение", null, "Данные с сервера небыли подгружены.");
            }

            screens.login().hide();
            screens.main().show();
            message.setText("");
            calculatorController.startEventHandler(screens.getPrimaryStage().getScene());
        } catch (NullPointerException | UsernameNotFoundException e) {
            message.setText("Неверное имя пользователя или пароль:");
        }
    }
}
