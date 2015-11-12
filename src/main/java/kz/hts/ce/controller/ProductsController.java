package kz.hts.ce.controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import kz.hts.ce.model.dto.ProductDto;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductsController {

    @FXML
    private TableView<ProductDto> productTable;
    @FXML
    private TableColumn<ProductDto, String> name;
    @FXML
    private TableColumn<ProductDto, Number> amount;
    @FXML
    private TableColumn<ProductDto, BigDecimal> price;
    @FXML
    private TableColumn<ProductDto, BigDecimal> totalPrice;

    private List<ProductDto> productsDto = new ArrayList<>();
    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();

    public void addProductsToTable() {
        productsData.clear();
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
        totalPrice.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductDto, BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<ProductDto, BigDecimal> cellData) {
                return cellData.getValue().totalPriceProperty();
            }
        });

//        productTable.setEditable(true);
        productTable.setItems(productsData);
    }

    public void setProductDtoToProductsDto(ProductDto productDto) {
        productsDto.add(productDto);
    }
}
