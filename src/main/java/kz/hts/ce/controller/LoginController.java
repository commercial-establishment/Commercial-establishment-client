package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class LoginController {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Label message;

    @Autowired
    private SpringUtil springUtils;

    @FXML
    @Transactional
    private void btnLoginAction(ActionEvent event) throws IOException {
        try {
            springUtils.authorize(username.getText(), password.getText());
            PagesConfiguration screens = getPagesConfiguration();
            screens.login().close();
            Stage stage = new Stage();
            screens.setPrimaryStage(stage);
            screens.main();
        } catch (NullPointerException | UsernameNotFoundException e) {
            message.setText("Ошибка авторизации. Пожалуйста, попробуйте ещё раз:");
        }
    }
}
