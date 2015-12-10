package kz.hts.ce.controller.reports;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.*;
import kz.hts.ce.service.CategoryService;
import kz.hts.ce.service.ProductService;
import kz.hts.ce.service.WarehouseProductHistoryService;
import kz.hts.ce.service.WarehouseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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
    @FXML
    private TreeTableColumn<ProductDto, BigDecimal> sumShopPrice;
    @FXML
    private TreeTableColumn<ProductDto, BigDecimal> sumCostPrice;


    private TreeItem<ProductDto> root = null;
    private TreeItem<ProductDto> categoryTreeItem = null;
    private TreeItem<ProductDto> productTreeItem = new TreeItem<>();


    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private WarehouseProductService warehouseProductService;
    @Autowired
    private WarehouseProductHistoryService wphService;

    @FXML
    public void export(){
    }
    @FXML
    public void showReport() {
        LocalDate startLocaleDate = startDate.getValue();
        Date startDateUtil = Date.from(startLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate endLocaleDate = endDate.getValue();
        Date endDateUtil = Date.from(endLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<WarehouseProductHistory> productHistories = wphService.findByDateBetweenAndProductId(startDateUtil, endDateUtil, 1);

        root = new TreeItem<>();
        categoryTreeItem = new TreeItem<>();
        productsReport.getStylesheets().add("style.css");
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
        productDto1.setUnitSymbol("");
        productDto1.setOldAmount(0);
        productDto1.setResidue(0);
        productDto1.setFinalPrice(BigDecimal.ZERO);
        productDto1.setPrice(BigDecimal.ZERO);
        productDto1.setSumOfCostPrice(BigDecimal.ZERO);
        productDto1.setSumOfShopPrice(BigDecimal.ZERO);
        root.setValue(productDto1);

        for (Map.Entry<String, List<WarehouseProduct>> map : categoryProductsMap.entrySet()) {
            String categoryName = map.getKey();
            List<WarehouseProduct> products = map.getValue();
            ProductDto productDtoKey = new ProductDto();
            productDtoKey.setName(categoryName);
            productDtoKey.setAmount(0);
            productDtoKey.setOldAmount(0);
            productDtoKey.setResidue(0);
            productDtoKey.setUnitSymbol("");
            productDtoKey.setFinalPrice(BigDecimal.ZERO);
            productDtoKey.setPrice(BigDecimal.ZERO);
            productDtoKey.setSumOfCostPrice(BigDecimal.ZERO);
            productDtoKey.setSumOfShopPrice(BigDecimal.ZERO);

            TreeItem<ProductDto> categoryItem = new TreeItem<>(productDtoKey);
            root.getChildren().add(categoryItem);
            for (WarehouseProduct product : products) {
                ProductDto productDtoValue = new ProductDto();
                productDtoValue.setName(product.getProduct().getName());
                productDtoValue.setOldAmount(product.getArrival());
                productDtoValue.setResidue(product.getResidue());
                productDtoValue.setUnitSymbol(product.getProduct().getUnit().getSymbol());
                productDtoValue.setFinalPrice(product.getFinalPrice());
                productDtoValue.setPrice(product.getInitialPrice());
                productDtoValue.setSumOfCostPrice(product.getInitialPrice().multiply(BigDecimal.valueOf(product.getResidue())));
                productDtoValue.setSumOfShopPrice(product.getFinalPrice().multiply(BigDecimal.valueOf(product.getResidue())));

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
                new ReadOnlyStringWrapper(p.getValue().getValue().getUnitSymbol()));
        costPrice.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getPrice()));
        shopPrice.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getFinalPrice()));
        sumCostPrice.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getSumOfCostPrice()));
        sumShopPrice.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getSumOfShopPrice()));

    }
}
