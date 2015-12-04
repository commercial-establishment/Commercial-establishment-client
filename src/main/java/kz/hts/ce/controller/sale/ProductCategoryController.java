package kz.hts.ce.controller.sale;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.*;
import kz.hts.ce.service.CategoryService;
import kz.hts.ce.service.EmployeeService;
import kz.hts.ce.service.WarehouseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static kz.hts.ce.util.spring.SpringUtil.getPrincipal;

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

        Map<String, List<WarehouseProduct>> productMap = new HashMap<>();
        List<Category> categoriesFromDB = categoryService.findAll();
        for (Category category : categoriesFromDB) {
            List<WarehouseProduct> warehouseProducts = warehouseProductService.
                    findByCategoryIdAndShopId(category.getId(), employee.getShop().getId());
            productMap.put(category.getName(), warehouseProducts);
        }

        categories.getSelectionModel().selectedItemProperty().addListener(categoriesListener(productMap));

        if (categoryProductsTable != null) {
            categoryProductsTable.setOnMousePressed(event -> {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    ProductDto productDto = categoryProductsTable.getSelectionModel().getSelectedItem();
                    productDto.setAmount(1);
                    productsController.addProductIntProductDto(productDto);
                    productsController.addProductsToTable();
                }
            });
        }
    }

    public ChangeListener<String> categoriesListener( Map<String, List<WarehouseProduct>> productMap){
       return (ov, oldVal, newVal) -> {
            if (flag) {
                productsData.clear();

                List<WarehouseProduct> warehouseProducts = null;
                for (Map.Entry<String, List<WarehouseProduct>> productMapEntry : productMap.entrySet()) {
                    if (newVal.equals(productMapEntry.getKey())) {
                        warehouseProducts = productMapEntry.getValue();
                    }
                }
                if (warehouseProducts != null) {
                    for (WarehouseProduct productWarehouse : warehouseProducts) {
                        ProductDto productDto = new ProductDto();
                        productDto.setName(productWarehouse.getProduct().getName());
                        productDto.setPrice(productWarehouse.getPrice());
                        productDto.setResidue(productWarehouse.getResidue());
                        productsData.add(productDto);
                    }
                }
                name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
                price.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
                residue.setCellValueFactory(cellData -> cellData.getValue().residueProperty());
                categoryProductsTable.setItems(productsData);
            }
            flag = true;
        };
    }

    public TableColumn<ProductDto, String> getName() {
        return name;
    }

    public void setName(TableColumn<ProductDto, String> name) {
        this.name = name;
    }

    public TableColumn<ProductDto, BigDecimal> getPrice() {
        return price;
    }

    public void setPrice(TableColumn<ProductDto, BigDecimal> price) {
        this.price = price;
    }
}