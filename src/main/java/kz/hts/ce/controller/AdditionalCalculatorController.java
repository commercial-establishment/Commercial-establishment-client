package kz.hts.ce.controller;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import kz.hts.ce.config.PagesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaFxUtil.additionalCalculator;
import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class AdditionalCalculatorController implements Initializable {

    private StringBuilder buttonState;
    private Button button;

    @Autowired
    private PaymentController paymentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button = new Button();
        PagesConfiguration screens = getPagesConfiguration();
        buttonState = new StringBuilder("");
        EventHandler<KeyEvent> eventHandler = evt -> {
            buttonState.setLength(0);
            buttonState.append(evt.getCode().toString());
            this.handleOnAnyButtonFromKeypad();
        };
        ChangeListener<Boolean> changeListener = (observable, oldValue, newValue) -> {
            if (newValue) screens.getPrimaryStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
        };

        screens.getPrimaryStage().focusedProperty().addListener(changeListener);
    }

    @FXML
    public void handleOnAnyButtonClicked(ActionEvent evt) {
        Button button = (Button) evt.getSource();
        final String buttonText = button.getText();
        if (buttonText.matches("^[0-9CE\\s[*+=.]\\s]*$")) {
            additionalCalculator(buttonText, paymentController.getGiven());
        }
    }

    @FXML
    public void handleOnAnyButtonFromKeypad() {
        button.setText(String.valueOf(buttonState));
        String buttonText = button.getText();
        if (buttonText.startsWith("NUMPAD")) {
            String[] splittedButtonText = buttonText.split("NUMPAD");
            buttonText = splittedButtonText[1];
        } else if (button.getText().equals("DECIMAL")) {
            buttonText = ".";
        } else if (button.getText().equals("MULTIPLY")) {
            buttonText = "*";
        } else if (button.getText().equals("BACK_SPACE")) {
            buttonText = "CE";
        }
        if (buttonText.matches("^[0-9CE\\s[*+.]\\s]*$")) {
            additionalCalculator(buttonText, paymentController.getGiven());
        }
    }
}

