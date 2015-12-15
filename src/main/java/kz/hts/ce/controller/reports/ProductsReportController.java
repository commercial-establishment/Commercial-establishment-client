package kz.hts.ce.controller.reports;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.Category;
import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.model.entity.WarehouseProductHistory;
import kz.hts.ce.service.WarehouseProductHistoryService;
import kz.hts.ce.service.WarehouseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static kz.hts.ce.util.JavaUtil.getEndOfDay;
import static kz.hts.ce.util.WriteExcelFileExample.writeProductsToExcel;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;

@Controller
public class ProductsReportController {

    public static final int ZERO = 0;
    public static final int ONE = 1;
    private TreeItem<ProductDto> root = null;
    private TreeItem<ProductDto> categoryTreeItem = null;
    private TreeItem<ProductDto> productTreeItem = new TreeItem<>();
    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();

    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TreeTableView productsReport;
    @FXML
    private TreeTableColumn<ProductDto, Number> arrival;
    @FXML
    private TreeTableColumn<ProductDto, Number> sold;
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

    @Autowired
    private WarehouseProductService warehouseProductService;
    @Autowired
    private WarehouseProductHistoryService wphService;

    @FXML
    public void export() {
    }

    @FXML
    public void showReport() {
        LocalDate startLocaleDate = startDate.getValue();
        LocalDate endLocaleDate = endDate.getValue();

        if (startLocaleDate == null || endLocaleDate == null) {
            alert(Alert.AlertType.WARNING, "Ошибка периода", null, "Пожалуйста укажите период");
        }
        Date startDateUtil = Date.from(startLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDateUtil = getEndOfDay(Date.from(endLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        root = new TreeItem<>();
        categoryTreeItem = new TreeItem<>();
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

        ProductDto rootProductDto = new ProductDto();
        rootProductDto.setName("");
        rootProductDto.setUnitSymbol("");
        rootProductDto.setOldAmount(ZERO);
        rootProductDto.setResidue(ZERO);
        rootProductDto.setArrival(ZERO);
        rootProductDto.setSoldAmount(ZERO);
        rootProductDto.setFinalPrice(BigDecimal.ZERO);
        rootProductDto.setPrice(BigDecimal.ZERO);
        rootProductDto.setSumOfCostPrice(BigDecimal.ZERO);
        rootProductDto.setSumOfShopPrice(BigDecimal.ZERO);
        root.setValue(rootProductDto);

        for (Map.Entry<String, List<WarehouseProduct>> map : categoryProductsMap.entrySet()) {
            String categoryName = map.getKey();
            List<WarehouseProduct> warehouseProductsValue = map.getValue();
            ProductDto productDtoKey = new ProductDto();
            productDtoKey.setName(categoryName);
            productDtoKey.setAmount(ZERO);
            productDtoKey.setOldAmount(ZERO);
            productDtoKey.setResidue(ZERO);
            productDtoKey.setArrival(ZERO);
            productDtoKey.setSoldAmount(ZERO);
            productDtoKey.setUnitSymbol("");
            productDtoKey.setFinalPrice(BigDecimal.ZERO);
            productDtoKey.setPrice(BigDecimal.ZERO);
            productDtoKey.setSumOfCostPrice(BigDecimal.ZERO);
            productDtoKey.setSumOfShopPrice(BigDecimal.ZERO);

            TreeItem<ProductDto> categoryItem = new TreeItem<>(productDtoKey);
            root.getChildren().add(categoryItem);

            List<ProductDto> productDtos = new ArrayList<>();
            for (WarehouseProduct warehouseProduct : warehouseProductsValue) {
                ProductDto productDtoValue = new ProductDto();
                productDtoValue.setArrival(0);
                productDtoValue.setId(warehouseProduct.getId());
                productDtoValue.setName(warehouseProduct.getProduct().getName());

                List<WarehouseProductHistory> startWPHistories = wphService.
                        findPastNearestDate(startDateUtil, warehouseProduct.getProduct().getId());
                List<WarehouseProductHistory> endWPHistories = wphService.
                        findPastNearestDate(endDateUtil, warehouseProduct.getProduct().getId());

                if (startWPHistories.size() != ZERO && endWPHistories.size() == ZERO) {
                    WarehouseProductHistory startNearestDate = startWPHistories.get(ZERO);
                    productDtoValue.setOldAmount(startNearestDate.getResidue());
                    productDtoValue.setResidue(ZERO);
                } else if (startWPHistories.size() == ZERO && endWPHistories.size() != ZERO) {
                    WarehouseProductHistory endNearestDate = endWPHistories.get(endWPHistories.size() - ONE);
                    productDtoValue.setOldAmount(ZERO);
                    productDtoValue.setResidue(endNearestDate.getResidue());
                } else if (startWPHistories.size() != ZERO && endWPHistories.size() != ZERO) {
                    WarehouseProductHistory endNearestDate = endWPHistories.get(endWPHistories.size() - ONE);
                    WarehouseProductHistory startNearestDate = startWPHistories.get(ZERO);
                    productDtoValue.setResidue(endNearestDate.getResidue());
                    productDtoValue.setOldAmount(startNearestDate.getResidue());
                } else {
                    productDtoValue.setOldAmount(ZERO);
                    productDtoValue.setResidue(ZERO);
                }

                List<WarehouseProductHistory> productHistories = wphService.
                        findByDateBetweenAndProductId(startDateUtil, endDateUtil, warehouseProduct.getProduct().getId());
                productDtoValue.setArrival(ZERO);
                for (WarehouseProductHistory productHistory : productHistories) {
                    productDtoValue.setArrival(productDtoValue.getArrival() + productHistory.getArrival());
                }

                productDtoValue.setSoldAmount(productDtoValue.getOldAmount() + productDtoValue.getArrival()
                        - productDtoValue.getResidue());
                productDtoValue.setUnitSymbol(warehouseProduct.getProduct().getUnit().getSymbol());
                productDtoValue.setFinalPrice(warehouseProduct.getFinalPrice());
                productDtoValue.setPrice(warehouseProduct.getInitialPrice());
                productDtoValue.setSumOfCostPrice(warehouseProduct.getInitialPrice()
                        .multiply(BigDecimal.valueOf(warehouseProduct.getResidue())));
                productDtoValue.setSumOfShopPrice(warehouseProduct.getFinalPrice()
                        .multiply(BigDecimal.valueOf(warehouseProduct.getResidue())));

                TreeItem<ProductDto> productItem = new TreeItem<>(productDtoValue);
                productDtos.add(productDtoValue);
                if (!productHistories.isEmpty()) {
                    categoryItem.getChildren().add(productItem);
                }
            }
            writeProductsToExcel(productDtos);
        }

        productsReport.setRoot(root);
        categoryTreeItem.setExpanded(true);

        initializeTableFields();
    }

    private void initializeTableFields() {
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
        arrival.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getArrival()));
        sold.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getSoldAmount()));
    }
}
