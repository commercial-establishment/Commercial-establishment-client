package kz.hts.ce.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private TransferService transferService;

    @Autowired
    private SpringHelper springHelper;

    @FXML
    @Transactional
    private void loginAction() throws IOException {
        try {
            PagesConfiguration screens = getPagesConfiguration();
            springHelper.authorize(username.getText(), password.getText());
            springHelper.setPassword(password.getText());
            Stage stage = new Stage();
            screens.setPrimaryStage(stage);

            Shift shiftEntity = new Shift();
            shiftEntity.setStart(new Date());
            Employee employee = employeeService.findByUsername(getPrincipal());
            springHelper.setEmployee(employee);
            shiftEntity.setEmployee(employee);
            Shift shift = shiftService.save(shiftEntity);
            springHelper.setShift(shift);

            screens.login().hide();
            screens.main().show();

            springHelper.transmitAndReceiveData();
            message.setText("");
        } catch (NullPointerException | UsernameNotFoundException e) {
            message.setText("Неверное имя пользователя или пароль:");
        }
    }


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
}
