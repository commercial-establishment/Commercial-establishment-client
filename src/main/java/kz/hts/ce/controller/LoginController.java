package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.model.entity.Employee;
import kz.hts.ce.model.entity.Shift;
import kz.hts.ce.model.entity.Transfer;
import kz.hts.ce.service.EmployeeService;
import kz.hts.ce.service.ShiftService;
import kz.hts.ce.service.TransferService;
import kz.hts.ce.util.spring.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaUtil.checkConnection;
import static kz.hts.ce.util.JavaUtil.getFixedDate;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;
import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;
import static kz.hts.ce.util.spring.SpringUtil.getPrincipal;

@Controller
public class LoginController implements Initializable{

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
    private TransferService transferService;

//    @Autowired
//    private CalculatorController calculatorController;

    @Autowired
    private SpringUtil springUtils;

    @FXML
    @Transactional
    private void loginAction() throws IOException {
        PagesConfiguration screens = getPagesConfiguration();
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
                long lastTransferDate = transferService.findLastTransferDate();
                springUtils.checkAndUpdateNewDataFromServer(lastTransferDate);
                springUtils.sendDataToServer(lastTransferDate);
                transferService.saveWithNewDate();
            } else {
                alert(Alert.AlertType.WARNING, "Проверьте интернет соединение", null, "Данные с сервера небыли подгружены.");
            }

            screens.login().hide();
            screens.main().show();
            message.setText("");
//            calculatorController.startEventHandler(screens.getPrimaryStage().getScene());
        } catch (NullPointerException | UsernameNotFoundException e) {
            message.setText("Неверное имя пользователя или пароль:");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                try{
                    loginAction();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
