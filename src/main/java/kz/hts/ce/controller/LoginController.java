package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.hts.ce.config.UrlConfiguration;
import kz.hts.ce.util.AppContextSingleton;
import kz.hts.ce.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LoginController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    @Transactional
    private void btnLoginAction(ActionEvent event) throws IOException {
        try {
            springUtils.authorize(txtUsername.getText(), txtPassword.getText());
            ApplicationContext context = AppContextSingleton.getInstance();
            UrlConfiguration screens = context.getBean(UrlConfiguration.class);
            screens.login().close();
            Stage stage = new Stage();
            screens.setPrimaryStage(stage);
            screens.main();
        }catch (UsernameNotFoundException e) {
            lblMessage.setText("Login failure, please try again:");
        }
    }
}
