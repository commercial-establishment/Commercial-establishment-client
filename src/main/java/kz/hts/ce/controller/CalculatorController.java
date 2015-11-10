package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaFxUtil.calculator;

@Controller
public class CalculatorController implements Initializable {

    @FXML
    private TextField display;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void handleOnAnyButtonClicked(ActionEvent evt) {
        calculator(evt, display);
    }
}
