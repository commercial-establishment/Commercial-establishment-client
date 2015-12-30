package kz.hts.ce.controller.reports;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.Category;
import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.model.entity.WarehouseProductHistory;
import kz.hts.ce.service.InvoiceProductService;
import kz.hts.ce.service.WarehouseProductHistoryService;
import kz.hts.ce.service.WarehouseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static kz.hts.ce.util.JavaUtil.getEndOfDay;
import static kz.hts.ce.util.JavaUtil.getStartOfDay;
import static kz.hts.ce.util.WriteExcelFileExample.writeProductsToExcel;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;

@Controller
public class ProductsReportController {

    private static final int ZERO = 0;
    private List<ProductDto> productDtos;
    private TreeItem<ProductDto> root = null;
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
    @FXML
    private TreeTableColumn<ProductDto, Number> dropped;

    @Autowired
    private WarehouseProductService warehouseProductService;
    @Autowired
    private WarehouseProductHistoryService wphService;
    @Autowired
    private InvoiceProductService invoiceProductService;

    @FXML
    private void export() {
        writeProductsToExcel(productDtos);
    }

    @FXML
    private void showReport() {
        LocalDate startLocaleDate = startDate.getValue();
        LocalDate endLocaleDate = endDate.getValue();
        if (startLocaleDate == null || endLocaleDate == null)
            alert(Alert.AlertType.WARNING, "Ошибка периода", null, "Пожалуйста укажите период");

        root = new TreeItem<>();
        root.setExpanded(true);
        productsReport.setShowRoot(false);
        List<WarehouseProduct> warehouseProducts = warehouseProductService.findAll();
        Set<Category> categories = warehouseProducts.stream().map(warehouseProduct -> warehouseProduct.getProduct().getCategory())
                .collect(Collectors.toSet());

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
        rootProductDto.setDropped(ZERO);
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
            productDtoKey.setDropped(ZERO);
            productDtoKey.setUnitSymbol("");
            productDtoKey.setFinalPrice(BigDecimal.ZERO);
            productDtoKey.setPrice(BigDecimal.ZERO);
            productDtoKey.setSumOfCostPrice(BigDecimal.ZERO);
            productDtoKey.setSumOfShopPrice(BigDecimal.ZERO);

            TreeItem<ProductDto> categoryItem = new TreeItem<>(productDtoKey);
            root.getChildren().add(categoryItem);
            categoryItem.setExpanded(true);

            for (WarehouseProduct warehouseProduct : warehouseProductsValue) {
                ProductDto productDtoValue = new ProductDto();
                productDtoValue.setArrival(ZERO);
                productDtoValue.setId(warehouseProduct.getId());
                productDtoValue.setName(warehouseProduct.getProduct().getName());

                Date startDateUtil = Date.from(startLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date endDateUtil = getEndOfDay(Date.from(endLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                List<WarehouseProductHistory> startWPHistories = wphService.
                        findPastNearestAndEqualsDate(getStartOfDay(startDateUtil), warehouseProduct.getProduct().getId());

                productDtoValue.setOldAmount(ZERO);
                for (WarehouseProductHistory startWPHistory : startWPHistories) {
                    if (startWPHistory.getDropped() == 0) {
                        productDtoValue.setOldAmount(productDtoValue.getOldAmount() + startWPHistory.getArrival());
                    } else {
                        productDtoValue.setOldAmount(productDtoValue.getOldAmount() - startWPHistory.getDropped());
                    }
                }

                List<WarehouseProductHistory> productHistories = wphService.
                        findByDatesBetween(startDateUtil, endDateUtil, warehouseProduct.getProduct().getId());
                productDtoValue.setArrival(ZERO);
                productDtoValue.setDropped(ZERO);
                for (WarehouseProductHistory productHistory : productHistories) {
                    if (productHistory.getDropped() != 0 && productHistory.getArrival() == 0) {
                        productDtoValue.setArrival(productDtoValue.getArrival() - productHistory.getDropped());
                    } else {
                        productDtoValue.setArrival(productDtoValue.getArrival() + productHistory.getArrival());
                    }
                }
                productDtoValue.setResidue(ZERO);
                productDtoValue.setSoldAmount(ZERO);
                List<WarehouseProductHistory> endWPHistories = wphService.
                        findPastNearestAndEqualsDate(endDateUtil, warehouseProduct.getProduct().getId());
                for (WarehouseProductHistory endWPHistory : endWPHistories) {
                    if (endWPHistory.getDropped() != 0 && endWPHistory.getArrival() == 0) {
                        productDtoValue.setResidue(productDtoValue.getResidue() - endWPHistory.getDropped());
                    } else {
                        productDtoValue.setResidue(productDtoValue.getResidue() + endWPHistory.getArrival() - endWPHistory.getSold());
                    }
                    productDtoValue.setSoldAmount(productDtoValue.getSoldAmount() + endWPHistory.getSold());
                }
                productDtoValue.setUnitSymbol(warehouseProduct.getProduct().getUnit().getSymbol());
                productDtoValue.setFinalPrice(warehouseProduct.getFinalPrice());
                productDtoValue.setPrice(warehouseProduct.getInitialPrice());
                productDtoValue.setSumOfCostPrice(warehouseProduct.getInitialPrice()
                        .multiply(BigDecimal.valueOf(warehouseProduct.getResidue())));
                productDtoValue.setSumOfShopPrice(warehouseProduct.getFinalPrice()
                        .multiply(BigDecimal.valueOf(warehouseProduct.getResidue())));

                TreeItem<ProductDto> productItem = new TreeItem<>(productDtoValue);
                if (productDtos == null) productDtos = new ArrayList<>();
                productDtos.add(productDtoValue);
                categoryItem.getChildren().add(productItem);
            }
        }

        productsReport.setRoot(root);
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
        dropped.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getDropped()));
    }
}
