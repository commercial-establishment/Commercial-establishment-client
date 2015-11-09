package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    private FXMLDialog dialog;
    @FXML
    public TextField txtUsername;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public Button btnLogin;
    @FXML
    public Label lblMessage;
    @Autowired
    private SpringUtils springUtils;
    @Autowired
    private ScreensConfiguration screens;

    public LoginController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @FXML
    private void btnLoginAction(ActionEvent event) throws IOException {
        try {
            springUtils.authorize(txtUsername.getText(), txtPassword.getText());
            Stage stage = new Stage();
            screens.setPrimaryStage(stage);
            screens.mainDialog().show();
            dialog.close();
        }catch (UsernameNotFoundException e) {
            lblMessage.setText("Login failure, please try again:");
        }
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
}
