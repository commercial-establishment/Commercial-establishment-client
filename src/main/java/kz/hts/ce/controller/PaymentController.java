package kz.hts.ce.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import kz.hts.ce.config.PagesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaUtil.stringToBigDecimal;
import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class PaymentController implements Initializable {

    public static final String ZERO = "0.00";
    @FXML
    private TextField shortage;
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
    @Autowired
    private ProductsController productsController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        total.setText(productsController.getPriceResult().getText());
        shortage.setText("-" + total.getText());
        given.setText(ZERO);
        change.setText(ZERO);
        given.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                BigDecimal totalBD = stringToBigDecimal(total.getText());
                if (!newValue.equals("")) {
                    BigDecimal newVal = stringToBigDecimal(newValue);
                    if (newVal.compareTo(totalBD) == 1) {
                        BigDecimal changeBD = newVal.subtract(totalBD);
                        change.setText(String.valueOf(changeBD));
                        shortage.setText(ZERO);
                    } else {
                        BigDecimal shortageBD = newVal.subtract(totalBD);
                        shortage.setText(String.valueOf(shortageBD));
                        change.setText(ZERO);
                    }
                }
            }
        });
    }

    @FXML
    private void cancel() {
        PagesConfiguration screens = getPagesConfiguration();
        screens.payment().close();
    }

    public TextField getGiven() {
        return given;
    }

}
