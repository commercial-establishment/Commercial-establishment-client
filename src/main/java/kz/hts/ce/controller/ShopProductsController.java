package kz.hts.ce.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.Category;
import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.service.CategoryService;
import kz.hts.ce.service.EmployeeService;
import kz.hts.ce.service.WarehouseProductService;
import kz.hts.ce.util.spring.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static kz.hts.ce.util.spring.SpringUtil.getPrincipal;

@Controller
public class ShopProductsController implements Initializable {

    public static final String GREEN_COLOR = "greenColor";
    public static final String ORANGE_COLOR = "orangeColor";
    public static final String RED_COLOR = "redColor";

    private long shopId;
    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();

    @FXML
    private TableView<ProductDto> productTable;
    @FXML
    private TableColumn<ProductDto, BigDecimal> price;
    @FXML
    private TableColumn<ProductDto, Number> barcode;
    @FXML
    private TableColumn<ProductDto, String> name;
    @FXML
    private TableColumn<ProductDto, String> unit;
    @FXML
    private TableColumn<ProductDto, Number> amount;
    @FXML
    private TableColumn<ProductDto, Number> residue;
    @FXML
    private ComboBox<String> categories;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private WarehouseProductService warehouseProductService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private SettingsController settingsController;
    @Autowired
    private JsonUtil jsonUtil;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Category> categoriesFromDB = categoryService.findAll();
        List<String> categoryNames = categoriesFromDB.stream().map(Category::getName).collect(Collectors.toList());
        categories.getItems().addAll(categoryNames);

        shopId = employeeService.findByUsername(getPrincipal()).getShop().getId();
    }

    public void findProductsByCategories(ActionEvent event) {
        productsData.clear();

        ComboBox<String> source = (ComboBox<String>) event.getSource();
        String categoryName = source.getValue();

        List<WarehouseProduct> warehouseProducts = warehouseProductService.
                findByCategoryIdAndShopId(categoryService.findByName(categoryName).getId(), shopId);

        for (WarehouseProduct warehouseProduct : warehouseProducts) {
            ProductDto productDto = new ProductDto();
            productDto.setName(warehouseProduct.getProduct().getName());
            productDto.setBarcode(warehouseProduct.getProduct().getBarcode());
            productDto.setUnitName(warehouseProduct.getProduct().getUnit().getName());
            productDto.setAmount(warehouseProduct.getArrival());
            productDto.setResidue(warehouseProduct.getResidue());
            productDto.setPrice(warehouseProduct.getPrice());
            productsData.add(productDto);
        }

        barcode.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        unit.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty());
        amount.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        residue.setCellValueFactory(cellData -> cellData.getValue().residueProperty());
        price.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        productTable.setItems(productsData);

        productTable.setRowFactory(new Callback<TableView<ProductDto>, TableRow<ProductDto>>() {
            @Override
            public TableRow<ProductDto> call(TableView<ProductDto> tableView) {
                return new TableRow<ProductDto>() {
                    @Override
                    protected void updateItem(ProductDto productDto, boolean empty) {
                        super.updateItem(productDto, empty);
                        if (settingsController.getProductMaxInt() == null && settingsController.getProductMinInt() == null) {
                            settingsController.setProductMinInt(jsonUtil.getProductMinInt());
                            settingsController.setProductMaxInt(jsonUtil.getProductMaxInt());
                        }
                        if (!empty && settingsController.getProductMaxInt() != null && settingsController.getProductMinInt() != null) {
                            if (productDto.getResidue() <= settingsController.getProductMinInt()) {
                                getStyleClass().removeAll(Collections.singleton(GREEN_COLOR));
                                getStyleClass().removeAll(Collections.singleton(ORANGE_COLOR));
                                getStyleClass().add(RED_COLOR);
                            } else if (productDto.getResidue() > settingsController.getProductMinInt() && productDto.getResidue() <= settingsController.getProductMaxInt()) {
                                getStyleClass().removeAll(Collections.singleton(GREEN_COLOR));
                                getStyleClass().removeAll(Collections.singleton(RED_COLOR));
                                getStyleClass().add(ORANGE_COLOR);
                            } else if (productDto.getResidue() > settingsController.getProductMaxInt()) {
                                getStyleClass().removeAll(Collections.singleton(ORANGE_COLOR));
                                getStyleClass().removeAll(Collections.singleton(RED_COLOR));
                                getStyleClass().add(GREEN_COLOR);
                            }
                        } else {
                            getStyleClass().removeAll(Collections.singleton(ORANGE_COLOR));
                            getStyleClass().removeAll(Collections.singleton(RED_COLOR));
                            getStyleClass().removeAll(Collections.singleton(GREEN_COLOR));
                        }
                    }
                };
            }
        });
    }
}
