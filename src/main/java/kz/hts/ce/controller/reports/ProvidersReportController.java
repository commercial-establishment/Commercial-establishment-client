package kz.hts.ce.controller.reports;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.ProductProvider;
import kz.hts.ce.model.entity.Provider;
import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.model.entity.WarehouseProductHistory;
import kz.hts.ce.service.ProductProviderService;
import kz.hts.ce.service.WarehouseProductHistoryService;
import kz.hts.ce.service.WarehouseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static kz.hts.ce.util.JavaUtil.getEndOfDay;
import static kz.hts.ce.util.JavaUtil.getStartOfDay;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;

@Controller
public class ProvidersReportController {

    public static final int ZERO = 0;
    public static final int ONE = 1;
    List<ProductDto> productDtos;
    private TreeItem<ProductDto> root = null;
    private TreeItem<ProductDto> providerTreeItem = null;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TreeTableView providersReport;
    @FXML
    private TreeTableColumn<ProductDto, String> providerProduct;
    @FXML
    private TreeTableColumn<ProductDto, String> unitOfMeasure;
    @FXML
    private TreeTableColumn<ProductDto, Number> arrival;
    @FXML
    private TreeTableColumn<ProductDto, Number> sold;
    @FXML
    private TreeTableColumn<ProductDto, BigDecimal> initialPrice;
    @FXML
    private TreeTableColumn<ProductDto, BigDecimal> sumInitialPrice;
    @FXML
    private TreeTableColumn<ProductDto, BigDecimal> finalPrice;
    @FXML
    private TreeTableColumn<ProductDto, BigDecimal> sumFinalPrice;
    @FXML
    private TreeTableColumn<ProductDto, Number> initialResidue;
    @FXML
    private TreeTableColumn<ProductDto, Number> finalResidue;

    @Autowired
    private ProductProviderService productProviderService;
    @Autowired
    private WarehouseProductHistoryService wphService;
    @Autowired
    private WarehouseProductService wpService;

    @FXML
    public void showReport() {
        LocalDate startLocaleDate = startDate.getValue();
        LocalDate endLocaleDate = endDate.getValue();

        if (startLocaleDate == null || endLocaleDate == null) {
            alert(Alert.AlertType.WARNING, "Ошибка периода", null, "Пожалуйста укажите период");
        }

        root = new TreeItem<>();
        providerTreeItem = new TreeItem<>();
        root.setExpanded(true);
        providersReport.setShowRoot(false);

        List<ProductProvider> productProviders = productProviderService.findAll();
        Set<Provider> providers = new HashSet<>();
        for (ProductProvider productProvider : productProviders) {
            providers.add(productProvider.getProvider());
        }
        Map<String, List<ProductProvider>> providerProductMap = new HashMap<>();
        for (Provider provider : providers) {
            List<ProductProvider> productsByProvider = productProviderService.findByProviderId(provider.getId());
            providerProductMap.put(provider.getCompanyName(), productsByProvider);
        }

        ProductDto rootProductDto = new ProductDto();
        rootProductDto.setName("");
        rootProductDto.setUnitSymbol("");
        rootProductDto.setArrival(ZERO);
        rootProductDto.setSoldAmount(ZERO);
        rootProductDto.setOldAmount(ZERO);
        rootProductDto.setResidue(ZERO);
        rootProductDto.setPrice(BigDecimal.ZERO);
        rootProductDto.setFinalPrice(BigDecimal.ZERO);
        rootProductDto.setSumOfCostPrice(BigDecimal.ZERO);
        rootProductDto.setSumOfShopPrice(BigDecimal.ZERO);
        root.setValue(rootProductDto);

        for (Map.Entry<String, List<ProductProvider>> map : providerProductMap.entrySet()) {
            String providersName = map.getKey();
            List<ProductProvider> productProvidersValue = map.getValue();
            ProductDto productDtoKey = new ProductDto();
            productDtoKey.setName(providersName);
            productDtoKey.setUnitSymbol("");
            productDtoKey.setArrival(ZERO);
            productDtoKey.setSoldAmount(ZERO);
            productDtoKey.setOldAmount(ZERO);
            productDtoKey.setResidue(ZERO);
            productDtoKey.setPrice(BigDecimal.ZERO);
            productDtoKey.setFinalPrice(BigDecimal.ZERO);
            productDtoKey.setSumOfCostPrice(BigDecimal.ZERO);
            productDtoKey.setSumOfShopPrice(BigDecimal.ZERO);


            TreeItem<ProductDto> providerItem = new TreeItem<>(productDtoKey);
            root.getChildren().add(providerItem);
            providerItem.setExpanded(true);

            for (ProductProvider productProvider : productProvidersValue) {
                ProductDto productDtoValue = new ProductDto();
                productDtoValue.setName(productProvider.getProduct().getName());
                productDtoValue.setUnitSymbol(productProvider.getProduct().getUnit().getSymbol());

                Date startDateUtil = Date.from(startLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date endDateUtil = getEndOfDay(Date.from(endLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                List<WarehouseProductHistory> startWPHistories = wphService.
                        findPastNearestAndEqualsDate(getStartOfDay(startDateUtil), productProvider.getProduct().getId());

                productDtoValue.setOldAmount(ZERO);
                for (WarehouseProductHistory startWPHistory : startWPHistories) {
                    if (startWPHistory.getDropped() == 0) {
                        productDtoValue.setOldAmount(productDtoValue.getOldAmount() + startWPHistory.getArrival());
                    } else {
                        productDtoValue.setOldAmount(productDtoValue.getOldAmount() - startWPHistory.getDropped());
                    }
                }

                List<WarehouseProductHistory> productHistories = wphService.
                        findByDatesBetween(startDateUtil, endDateUtil, productProvider.getProduct().getId());
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
                        findPastNearestAndEqualsDate(endDateUtil, productProvider.getProduct().getId());
                for (WarehouseProductHistory endWPHistory : endWPHistories) {
                    if (endWPHistory.getDropped() != 0 && endWPHistory.getArrival() == 0) {
                        productDtoValue.setResidue(productDtoValue.getResidue() - endWPHistory.getDropped());
                    } else {
                        productDtoValue.setResidue(productDtoValue.getResidue() + endWPHistory.getArrival() - endWPHistory.getSold());
                    }
                    productDtoValue.setSoldAmount(productDtoValue.getSoldAmount() + endWPHistory.getSold());
                }
                WarehouseProduct warehouseProduct = wpService.findByProductBarcode(productProvider.getProduct().getBarcode());
                productDtoValue.setPrice(warehouseProduct.getInitialPrice());
                productDtoValue.setFinalPrice(warehouseProduct.getFinalPrice());
                productDtoValue.setPrice(warehouseProduct.getInitialPrice());
                productDtoValue.setSumOfCostPrice(warehouseProduct.getInitialPrice()
                        .multiply(BigDecimal.valueOf(warehouseProduct.getResidue())));
                productDtoValue.setSumOfShopPrice(warehouseProduct.getFinalPrice()
                        .multiply(BigDecimal.valueOf(warehouseProduct.getResidue())));

                TreeItem<ProductDto> productItem = new TreeItem<>(productDtoValue);
                providerItem.getChildren().add(productItem);

            }
        }
        providersReport.setRoot(root);
        providerTreeItem.setExpanded(true);

        initializeTableFields();
    }

    public void initializeTableFields() {
        providerProduct.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductDto, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getName()));
        unitOfMeasure.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductDto, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getUnitSymbol()));
        arrival.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getArrival()));
        sold.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getSoldAmount()));
        initialResidue.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getOldAmount()));
        finalResidue.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getResidue()));
        initialPrice.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getPrice()));
        finalPrice.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getFinalPrice()));
        sumInitialPrice.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getSumOfCostPrice()));
        sumFinalPrice.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getSumOfShopPrice()));
    }
}