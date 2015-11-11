package kz.hts.ce.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.util.AppContextSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

import static kz.hts.ce.util.JavaUtil.stringToBigDecimal;

@Controller
public class AddProductController {

    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextField amount;
    @FXML
    private TextField totalPrice;

    @FXML
    public void success(ActionEvent event) {
    }

    @FXML
    public void cancel(ActionEvent event) {
        ApplicationContext context = AppContextSingleton.getInstance();
        PagesConfiguration screens = context.getBean(PagesConfiguration.class);
        screens.addProduct().close();
    }

    @FXML
    private void initialize() {
        amount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                BigDecimal priceBD = stringToBigDecimal(getPrice().getText());
                BigDecimal totalPriceBD = priceBD.multiply(new BigDecimal(Long.parseLong(newValue)));
                totalPrice.setText(String.valueOf(totalPriceBD));
            }
        });

        price.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (amount.getText().equals("")) {
                    amount.setText("1");
                }
                long amountLong = Long.parseLong(amount.getText());
                BigDecimal priceBD = stringToBigDecimal(newValue);
                BigDecimal totalPriceBD = priceBD.multiply(new BigDecimal(amountLong));
                totalPrice.setText(String.valueOf(totalPriceBD));
            }
        });
    }

    public TextField getName() {
        return name;
    }

    public TextField getPrice() {
        return price;
    }

    public TextField getAmount() {
        return amount;
    }

    public TextField getTotalPrice() {
        return totalPrice;
    }

    public void setName(TextField name) {
        this.name = name;
    }

    public void setPrice(TextField price) {
        this.price = price;
    }

    public void setAmount(TextField amount) {
        this.amount = amount;
    }

    public void setTotalPrice(TextField totalPrice) {
        this.totalPrice = totalPrice;
    }
}