package kz.hts.ce.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import kz.hts.ce.config.PagesConfiguration;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

import static kz.hts.ce.util.JavaUtil.stringToBigDecimal;
import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;

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

                if(!newValue.equals("")) {
                    BigDecimal newVal = stringToBigDecimal(newValue);
                    if(newVal.compareTo(totalBD)==1) {
                        BigDecimal changeBD = newVal.subtract(totalBD);
                        change.setText(String.valueOf(changeBD));
                    }
                    else
                        change.setText("");
                }
            }
        });
    }

    @FXML
    private void cancel(){
        PagesConfiguration screens = getPagesConfiguration();
        screens.payment().close();
    }

    public TextField getTotal() {
        return total;
    }

    public TextField getGiven() {
        return given;
    }
}
