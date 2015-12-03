package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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
    private Label message;

    @Autowired
    private SpringUtil springUtils;
    @Autowired
    private CalculatorController calculatorController;

    @FXML
    @Transactional
    private void loginAction(ActionEvent event) throws IOException {
        PagesConfiguration screens = getPagesConfiguration();
        try {
            springUtils.authorize(username.getText(), password.getText());
            Stage stage = new Stage();
            screens.setPrimaryStage(stage);
            screens.login().close();
            screens.main().show();
            calculatorController.startEventHandler(screens.getPrimaryStage().getScene());
        } catch (NullPointerException | UsernameNotFoundException e) {
            message.setText("Неверное имя пользователя или пароль:");
        }
    }
}
