package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.hts.ce.config.FXMLDialog;
import kz.hts.ce.config.ScreensConfiguration;
import kz.hts.ce.service.EmployeeService;
import kz.hts.ce.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    @Autowired
    private SpringUtils springUtils;

    private FXMLDialog dialog;
    private ScreensConfiguration screens;


    public LoginController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @FXML
    private void btnLoginAction(ActionEvent event) throws IOException {
        try {
            springUtils.authorize(txtUsername.getText(), txtPassword.getText());
            ((Node)event.getSource()).getScene().getWindow().hide();
            Parent parent = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Main");
            stage.show();
        }catch (UsernameNotFoundException e) {
            lblMessage.setText("Login failure, please try again:");
        }
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
}
