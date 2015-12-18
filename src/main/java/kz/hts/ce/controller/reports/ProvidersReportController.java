package kz.hts.ce.controller.reports;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.*;
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
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;

@Controller
public class ProvidersReportController {

    public static final int ZERO = 0;
    public static final int ONE = 1;
    private TreeItem<ProductDto> root = null;
    private TreeItem<ProductDto> providerTreeItem = null;
    List<ProductDto> productDtos;

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

        Date startDateUtil = Date.from(startLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDateUtil = getEndOfDay(Date.from(endLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

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
        root.setValue(rootProductDto);

        for (Map.Entry<String, List<ProductProvider>> map : providerProductMap.entrySet()) {
            String providersName = map.getKey();
            List<ProductProvider> productProvidersValue = map.getValue();
            ProductDto productDtoKey = new ProductDto();
            productDtoKey.setName(providersName);
            productDtoKey.setUnitSymbol("");
            productDtoKey.setArrival(ZERO);
            TreeItem<ProductDto> providerItem = new TreeItem<>(productDtoKey);
            root.getChildren().add(providerItem);
            providerItem.setExpanded(true);

            for (ProductProvider productProvider : productProvidersValue) {
                ProductDto productDtoValue = new ProductDto();
                productDtoValue.setName(productProvider.getProduct().getName());
                productDtoValue.setUnitSymbol(productProvider.getProduct().getUnit().getSymbol());
                List<WarehouseProductHistory> productHistories =  wphService.findByDateBetweenAndProductId(
                        startDateUtil, endDateUtil, productProvider.getProduct().getId());
                WarehouseProduct byProductId = wpService
                        .findByProductId(productProvider.getProduct().getId());
                for (WarehouseProductHistory productHistory : productHistories) {
                    productDtoValue.setArrival(byProductId.getArrival() + productHistory.getArrival());
                }
                TreeItem<ProductDto> productItem = new TreeItem<>(productDtoValue);
                providerItem.getChildren().add(productItem);
            }
        }
        providersReport.setRoot(root);
        providerTreeItem.setExpanded(true);

        initializeTableFields();
    }
    public void initializeTableFields(){
        providerProduct.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductDto, String> p)->
                new ReadOnlyStringWrapper(p.getValue().getValue().getName()));
        unitOfMeasure.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductDto, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getUnitSymbol()));
        arrival.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getArrival()));

    }
}