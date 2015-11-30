package kz.hts.ce.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import kz.hts.ce.model.dto.ProductDto;
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

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private WarehouseProductService warehouseProductService;
    @Autowired
    private InvoiceWarehouseProductService invoiceWarehouseProductService;

    @FXML
    private TextField number;
    @FXML
    private Spinner postponement;
    @FXML
    private CheckBox vat;
    @FXML
    private DatePicker date;
    @FXML
    private ComboBox<String> providers;

    @FXML
    private VBox vBox;
    @FXML
    private TableView<ProductDto> tableView = new TableView<>();

    private ObservableList<ProductDto> tableData = FXCollections.observableArrayList();

    @FXML
    public void add(){

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
