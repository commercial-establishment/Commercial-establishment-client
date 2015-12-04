//package kz.hts.ce.controller;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.TextField;
//import kz.hts.ce.config.PagesConfiguration;
//import org.springframework.stereotype.Controller;
//
//import java.math.BigDecimal;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import static kz.hts.ce.util.JavaUtil.stringToBigDecimal;
//import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;
//
//@Controller
//public class AddProductController implements Initializable {
//
//    @FXML
//    private TextField name;
//    @FXML
//    private TextField price;
//    @FXML
//    private TextField amount;
//    @FXML
//    private TextField totalPrice;
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        amount.textProperty().addListener((observable, oldValue, newValue) -> {
//            BigDecimal priceBD = stringToBigDecimal(price.getText());
//            BigDecimal totalPriceBD = priceBD.multiply(new BigDecimal(Long.parseLong(newValue)));
//            totalPrice.setText(String.valueOf(totalPriceBD));
//        });
//
//        price.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (amount.getText().equals("")) {
//                amount.setText("1");
//            }
//            long amountLong = Long.parseLong(amount.getText());
//            BigDecimal priceBD = stringToBigDecimal(newValue);
//            BigDecimal totalPriceBD = priceBD.multiply(new BigDecimal(amountLong));
//            totalPrice.setText(String.valueOf(totalPriceBD));
//        });
//    }
//
//    @FXML
//    public void success(ActionEvent event) {
//    }
//
//    @FXML
//    public void cancel() {
//        PagesConfiguration screens = getPagesConfiguration();
//        screens.addProduct().close();
//    }
//
//    public TextField getName() {
//        return name;
//    }
//
//    public void setName(TextField name) {
//        this.name = name;
//    }
//
//    public TextField getPrice() {
//        return price;
//    }
//
//    public void setPrice(TextField price) {
//        this.price = price;
//    }
//
//    public TextField getAmount() {
//        return amount;
//    }
//
//    public void setAmount(TextField amount) {
//        this.amount = amount;
//    }
//
//    public TextField getTotalPrice() {
//        return totalPrice;
//    }
//
//    public void setTotalPrice(TextField totalPrice) {
//        this.totalPrice = totalPrice;
//    }
//}