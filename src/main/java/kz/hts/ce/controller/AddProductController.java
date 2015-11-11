package kz.hts.ce.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;


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
    }

    @FXML
    private void initialize(){
        amount.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                System.out.println("TextField Text Changed (newValue: " + newValue + ")\n");

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

}