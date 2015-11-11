package kz.hts.ce.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.util.AppContextSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

import static kz.hts.ce.util.JavaUtil.stringToBigDecimal;


@Controller
public class PaymentController {
    @FXML
    private TextField given;
    @FXML
    private TextField change;
    @FXML
    private TextField total;
    @FXML
    private Button accept;
    @FXML
    private Button cancel;
    @FXML
    private Button print;

    @FXML
    private void initialize(){
        given.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                BigDecimal totalBD = stringToBigDecimal(getTotal().getText());
                BigDecimal changeBD = new BigDecimal(Long.parseLong(newValue)).subtract(totalBD);
                change.setText(String.valueOf(changeBD));
            }
        });
    }


    public TextField getTotal() {
        return total;
    }

    public TextField getGiven() {
        return given;
    }
}
