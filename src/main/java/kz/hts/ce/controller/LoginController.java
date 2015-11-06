package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.hts.ce.config.AppContext;
import kz.hts.ce.config.FXMLDialog;
import kz.hts.ce.config.ScreensConfiguration;
import kz.hts.ce.entity.Employee;
import kz.hts.ce.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginController implements DialogController {

    @FXML
    public TextField txtUsername;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public Button btnLogin;
    @FXML
    public Label lblMessage;

    @Autowired
    private EmployeeService employeeService;

    private FXMLDialog dialog;
    private ScreensConfiguration screens;

    public LoginController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @FXML
    private void btnLoginAction(ActionEvent event) throws IOException {
        Employee employee = employeeService.findByUsername(txtUsername.getText());
        if (employee == null) {
            lblMessage.setText("Username or Password invalid");
        }
        else if (employee.getUsername().equals(txtUsername.getText()) && employee.getPassword().equals(txtPassword.getText())) {
            ((Node)event.getSource()).getScene().getWindow().hide();
            ApplicationContext context = AppContext.getInstance();
            ScreensConfiguration screens = context.getBean(ScreensConfiguration.class);
            Stage stage = new Stage();
            screens.setPrimaryStage(stage);
            screens.mainDialog().show();
        } else {
            lblMessage.setText("Username or Password invalid");
        }
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
}
