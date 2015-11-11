package kz.hts.ce.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import kz.hts.ce.entity.ShopProduct;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductsController {

    @FXML
    private TableView<ShopProduct> productTable = new TableView<>();
    @FXML
    public TableColumn name;
    @FXML
    public TableColumn amount;
    @FXML
    public TableColumn price;
    @FXML
    public TableColumn totalPrice;

    private List<ShopProduct> shopProducts = new ArrayList<>();

    public void setProductsToTable() {
//        ObservableList<ShopProduct> data = FXCollections.observableArrayList(getShopProducts());
//        productTable.setEditable(true);
//
//        TableColumn priceColumn = new TableColumn("Price");
//        priceColumn.setMinWidth(100);
//        priceColumn.setCellValueFactory(new PropertyValueFactory<ShopProduct, String>("priceColumn"));
//        productTable.setItems(data);
//        productTable.getColumns().addAll(priceColumn);
    }

    public List<ShopProduct> getShopProducts() {
        return shopProducts;
    }

    public void setShopProductToShopProducts(ShopProduct product) {
        shopProducts.add(product);
    }

    public TableView<ShopProduct> getProductTable() {
        return productTable;
    }

    public void setProductTable(TableView<ShopProduct> productTable) {
        this.productTable = productTable;
    }
}
