package kz.hts.ce.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import kz.hts.ce.model.entity.Invoice;
import kz.hts.ce.model.entity.InvoiceWarehouseProduct;
import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.service.InvoiceService;
import kz.hts.ce.service.InvoiceWarehouseProductService;
import kz.hts.ce.service.WarehouseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import static kz.hts.ce.util.JavaFxUtil.alert;

@Controller
public class AddReceiptController {

    private int cnt=-1;

    @FXML
    private TextField number;
    @FXML
    private Spinner postponement;
    @FXML
    private CheckBox vat;
    @FXML
    private DatePicker date;
    @FXML
    private ComboBox<String> provider;
    @FXML
    private VBox vBox;

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private WarehouseProductService warehouseProductService;
    @Autowired
    private InvoiceWarehouseProductService invoiceWarehouseProductService;

    @FXML
    private void addRowForNewProduct() {
        HBox box = new HBox();
        TextField productName = new TextField();
        TextField unitOfMeasure = new TextField();
        TextField amount = new TextField();
        TextField price = new TextField();
        TextField priceAmount = new TextField();

        productName.setPrefWidth(362);
        price.setPrefWidth(118);
        unitOfMeasure.setPrefWidth(89);
        amount.setPrefWidth(89);
        priceAmount.setPrefWidth(112);
        box.getChildren().addAll(productName, price, unitOfMeasure, amount, priceAmount);
        vBox.getChildren().add(box);
        cnt++;
    }

    @FXML
    private void deleteProductRow() {
        if (cnt != -1) {
            vBox.getChildren().remove(cnt--);
        }
    }

    @FXML
    private void save() {
        try {
            ObservableList<Node> products = vBox.getChildren();
            for (Node product : products) {
                HBox productHBox = (HBox) product;
                ObservableList<Node> productFields = productHBox.getChildren();
                for (Node productField : productFields) {
                    TextField productTextField = (TextField) productField;
                    String fieldText = ((TextField) productField).getText();
                }
            }
        } catch (ControllerException e) {
            alert(Alert.AlertType.WARNING, "Ошибка", null, "Не все поля заполнены верно");
        }
    }
}
