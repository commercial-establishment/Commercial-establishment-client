package kz.hts.ce.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.Category;
import kz.hts.ce.model.entity.Employee;
import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.service.CategoryService;
import kz.hts.ce.service.EmployeeService;
import kz.hts.ce.service.WarehouseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static kz.hts.ce.util.SpringUtil.getPrincipal;

@Controller
public class ProductCategoryController implements Initializable {

    private boolean flag;
    private ObservableList<String> categoriesData = FXCollections.observableArrayList();
    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();

    @FXML
    private TableColumn<ProductDto, String> name;
    @FXML
    private TableColumn<ProductDto, BigDecimal> price;
    @FXML
    private TableColumn<ProductDto, Number> residue;
    @FXML
    private TableView<ProductDto> categoryProductsTable;
    @FXML
    private ListView<String> categories;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private WarehouseProductService warehouseProductService;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProductsController productsController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoriesData.clear();
        List<Category> categoriesFromDb = categoryService.findAll();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categoriesFromDb) {
            String categoryName = category.getName();
            categoryNames.add(categoryName);
        }

        categoriesData.addAll(categoryNames.stream().collect(Collectors.toList()));
        categories.setItems(categoriesData);

        Employee employee = employeeService.findByUsername(getPrincipal());
        categories.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldVal, String newVal) {
                if (flag) {
                    productsData.clear();
                    Category category = categoryService.findByName(newVal);
                    List<WarehouseProduct> warehouseProducts = warehouseProductService.
                            findByCategoryIdAndShopId(category.getId(), employee.getShop().getId());
                    for (WarehouseProduct productWarehouse : warehouseProducts) {
                        ProductDto productDto = new ProductDto();
                        productDto.setName(productWarehouse.getProduct().getName());
                        productDto.setPrice(productWarehouse.getPrice());
                        productDto.setResidue(productWarehouse.getResidue());
                        productsData.add(productDto);
                    }
                    name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
                    price.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
                    residue.setCellValueFactory(cellData -> cellData.getValue().residueProperty());
                    categoryProductsTable.setItems(productsData);
                }
                flag = true;
            }
        });

        if (categoryProductsTable != null) {
            categoryProductsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                        ProductDto productDto = categoryProductsTable.getSelectionModel().getSelectedItem();
                        productDto.setAmount(1);
                        productsController.addProductIntProductDto(productDto);
                        productsController.addProductsToTable();
                    }
                }
            });
        }
    }
}
