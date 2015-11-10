package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import kz.hts.ce.config.FXMLDialog;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class CalculatorController implements Initializable {

    private FXMLDialog dialog;
    private BigDecimal left;
    private String selectedOperator;
    private boolean numberInputting;

    @FXML
    private TextField display;

    @FXML
    public void handleOnAnyButtonClicked(ActionEvent evt) {
        Button button = (Button)evt.getSource();
        final String buttonText = button.getText();
        if (buttonText.equals("C")) {
            selectedOperator = "";
            numberInputting = false;
            display.setText("0");
            return;
        }
        if (buttonText.matches("[0-9\\.]")) {
            if (!numberInputting) {
                numberInputting = true;
                display.clear();
            }
            display.appendText(buttonText);
            return;
        }
        if (buttonText.matches("[＋－×÷]")) {
            left = new BigDecimal(display.getText());
            selectedOperator = buttonText;
            numberInputting = false;
            return;
        }
        if (buttonText.equals("=")) {
            final BigDecimal right = numberInputting ? new BigDecimal(display.getText()) : left;
            left = calculate(selectedOperator, left, right);
            display.setText(left.toString());
            numberInputting = false;
            return;
        }
    }

    static BigDecimal calculate(String operator, BigDecimal left, BigDecimal right) {
        switch (operator) {
            case "＋":
                return left.add(right);
            case "－":
                return left.subtract(right);
            case "×":
                return left.multiply(right);
            case "÷":
                return left.divide(right);
            default:
        }
        return right;
    }
    @Override
       public void initialize(URL location, ResourceBundle resources) {
        display.setText("See the controller");
    }
    @FXML
    private void change(ActionEvent e){
        display.setText("asda");
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
}
