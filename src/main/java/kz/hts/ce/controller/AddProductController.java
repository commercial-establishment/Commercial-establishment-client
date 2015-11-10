package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

@Controller
public class AddProductController {

    @FXML
    public TextField name;
    @FXML
    public TextField price;
    @FXML
    public TextField amount;
    @FXML
    public TextField vat;
    @FXML
    public TextField priceWithVat;
    @FXML
    public TextField totalPrice;

    @FXML
    public void success(ActionEvent event) {
    }

    @FXML
    public void cancel(ActionEvent event) {

    }
}