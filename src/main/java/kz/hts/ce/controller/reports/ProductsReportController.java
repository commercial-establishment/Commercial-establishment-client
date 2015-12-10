package kz.hts.ce.controller.reports;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.Category;
import kz.hts.ce.model.entity.InvoiceProduct;
import kz.hts.ce.model.entity.Product;
import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.service.CategoryService;
import kz.hts.ce.service.EmployeeService;
import kz.hts.ce.service.ProductService;
import kz.hts.ce.service.WarehouseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.*;

import static kz.hts.ce.util.spring.SpringUtil.getPrincipal;

@Controller
public class ProductsReportController {

    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TreeTableView productsReport;
    @FXML
    private TreeTableColumn<ProductDto, String> categoryProduct;
    @FXML
    private TreeTableColumn<ProductDto, String> unitOfMeasure;
    @FXML
    private TreeTableColumn<ProductDto, Number> residueInitial;
    @FXML
    private TreeTableColumn<ProductDto, Number> residueFinal;
    @FXML
    private TreeTableColumn<ProductDto, BigDecimal> costPrice;
    @FXML
    private TreeTableColumn<ProductDto, BigDecimal> shopPrice;

    private TreeItem<ProductDto> root = new TreeItem<>();
    private TreeItem<ProductDto> categoryTreeItem = new TreeItem<>();
    private TreeItem<ProductDto> productTreeItem = new TreeItem<>();


    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private WarehouseProductService warehouseProductService;

    @FXML
    public void showReport() {
        productsReport.refresh();
        root.setExpanded(true);
        productsReport.setShowRoot(false);
        List<WarehouseProduct> warehouseProducts = warehouseProductService.findAll();
        Set<Category> categories = new HashSet<>();
        for (WarehouseProduct warehouseProduct : warehouseProducts) {
            categories.add(warehouseProduct.getProduct().getCategory());
        }

        Map<String, List<WarehouseProduct>> categoryProductsMap = new HashMap<>();
        for (Category category : categories) {
            List<WarehouseProduct> warehouseProductsByCategory = warehouseProductService.findByCategoryId(category.getId());
            categoryProductsMap.put(category.getName(), warehouseProductsByCategory);
        }

        ProductDto productDto1 = new ProductDto();
        productDto1.setName("A");
        productDto1.setUnitName("B");
        productDto1.setOldAmount(0);
        productDto1.setResidue(0);
        productDto1.setFinalPrice(BigDecimal.ZERO);
        productDto1.setPrice(BigDecimal.ZERO);
        root.setValue(productDto1);

        for (Map.Entry<String, List<WarehouseProduct>> map : categoryProductsMap.entrySet()) {
            String categoryName = map.getKey();
            List<WarehouseProduct> products = map.getValue();
            ProductDto productDtoKey = new ProductDto();
            productDtoKey.setName(categoryName);
            productDtoKey.setAmount(0);
            productDtoKey.setOldAmount(0);
            productDtoKey.setResidue(0);
            productDtoKey.setUnitName(" ");
            productDtoKey.setFinalPrice(BigDecimal.ZERO);
            productDtoKey.setPrice(BigDecimal.ZERO);

            TreeItem<ProductDto> categoryItem = new TreeItem<>(productDtoKey);
            root.getChildren().add(categoryItem);
            for (WarehouseProduct product : products) {
                ProductDto productDtoValue = new ProductDto();
                productDtoValue.setName(product.getProduct().getName());
                productDtoValue.setOldAmount(product.getArrival());
                productDtoValue.setResidue(product.getResidue());
                productDtoValue.setUnitName(product.getProduct().getUnit().getName());
                productDtoValue.setFinalPrice(product.getFinalPrice());
                productDtoValue.setPrice(product.getInitialPrice());
                TreeItem<ProductDto> categoryItem1 = new TreeItem<>(productDtoValue);
                categoryItem.getChildren().add(categoryItem1);
            }
        }
        productsReport.setRoot(root);
        categoryTreeItem.setExpanded(true);

        categoryProduct.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductDto, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getName()));
        residueInitial.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getOldAmount()));
        residueFinal.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getResidue()));
        unitOfMeasure.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductDto, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getUnitName()));
        costPrice.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getPrice()));
        shopPrice.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getFinalPrice()));

    }
}
