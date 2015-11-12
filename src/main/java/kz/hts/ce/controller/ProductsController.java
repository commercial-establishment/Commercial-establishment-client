package kz.hts.ce.controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import kz.hts.ce.model.dto.ProductDto;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class ProductsController implements Initializable {

    @FXML
    private TableView<ProductDto> productTable;
    @FXML
    private TableColumn<ProductDto, String> name;
    @FXML
    private TableColumn<ProductDto, Number> amount;
    @FXML
    private TableColumn<ProductDto, BigDecimal> price;
    @FXML
    private TableColumn totalPrice;

    private List<ProductDto> productsDto = new ArrayList<>();
    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        totalPrice.setText("Total Price");
    }

    public void addProductsToTable() {
        for (ProductDto productDto : productsDto) {
            productsData.add(productDto);
        }

        name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductDto, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProductDto, String> cellData) {
                return cellData.getValue().nameProperty();
            }
        });
        amount.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductDto, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<ProductDto, Number> cellData) {
                return cellData.getValue().amountProperty();
            }
        });
        price.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductDto, BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<ProductDto, BigDecimal> cellData) {
                return cellData.getValue().priceProperty();
            }
        });

        productTable.setEditable(true);
        productTable.setItems(productsData);
    }

    public void setProductDtoToProductsDto(ProductDto productDto) {
        productsDto.add(productDto);
    }

    public TableView<ProductDto> getProductTable() {
        return productTable;
    }

    public void setProductTable(TableView<ProductDto> productTable) {
        this.productTable = productTable;
    }

    public TableColumn getName() {
        return name;
    }

    public void setName(TableColumn name) {
        this.name = name;
    }

    public TableColumn getAmount() {
        return amount;
    }

    public void setAmount(TableColumn amount) {
        this.amount = amount;
    }

    public TableColumn getPrice() {
        return price;
    }

    public void setPrice(TableColumn price) {
        this.price = price;
    }

    public TableColumn getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(TableColumn totalPrice) {
        this.totalPrice = totalPrice;
    }
}
