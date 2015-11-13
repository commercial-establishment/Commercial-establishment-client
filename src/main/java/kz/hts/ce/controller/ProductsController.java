package kz.hts.ce.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    @FXML
    private TextField priceResult;

    private List<ProductDto> productsDto = new ArrayList<>();
    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();

    public void addProductsToTable() {
        productsData.clear();
        BigDecimal priceResultBD = BigDecimal.ZERO;
        for (ProductDto productDto : productsDto) {
            productsData.add(productDto);
            BigDecimal totalPrice = productDto.getTotalPrice();
            priceResultBD = priceResultBD.add(totalPrice);
        }
        priceResult.setText(String.valueOf(priceResultBD));

        name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        amount.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        price.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        totalPrice.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty());

        productTable.setItems(productsData);
    }

    public void setProductDtoToProductsDto(ProductDto productDto) {
        productsDto.add(productDto);
    }

    public void deleteSelectedProductFromTable(){
        ProductDto productDto = productTable.getSelectionModel().getSelectedItem();
        productsDto.remove(productDto);
        productsData.remove(productDto);
        productTable.setItems(productsData);
    }
}
