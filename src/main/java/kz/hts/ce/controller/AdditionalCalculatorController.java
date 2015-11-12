package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import static kz.hts.ce.util.JavaFxUtil.additionalCalculator;

@Controller
public class AdditionalCalculatorController {

    @FXML
    private Button button;

    @Autowired
    private PaymentController paymentController;

    @FXML
    public void handleOnAnyButtonClicked(ActionEvent evt) {
        Button button = (Button) evt.getSource();
        final String buttonText = button.getText();
        if (buttonText.matches("^[0-9CE\\s[－.]\\s]$")) {
            additionalCalculator(buttonText, paymentController.getGiven());
        }
        System.out.println(paymentController.getGiven());
    }
}

