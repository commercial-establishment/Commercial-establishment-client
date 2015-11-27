package kz.hts.ce.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.Category;
import kz.hts.ce.model.entity.ProductProvider;
import kz.hts.ce.model.entity.Provider;
import kz.hts.ce.model.entity.ShopProvider;
import kz.hts.ce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static kz.hts.ce.util.SpringUtil.getPrincipal;

@Controller
public class CreateProductController implements Initializable {

    private Provider provider;
    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();

    @FXML
    private Spinner<Integer> amount;
    @FXML
    private TextField shopPrice;
    @FXML
    private TextField priceResult;
    @FXML
    private TextField unit;
    @FXML
    private TextField productName;

    @FXML
    private ComboBox<String> categories;
    @FXML
    private ComboBox<String> providers;

    @FXML
    private TableView<ProductDto> productTable;
    @FXML
    private TableColumn<ProductDto, String> name;
    @FXML
    private TableColumn<ProductDto, Number> barcode;

    @Autowired
    private ShopProviderService shopProviderService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private ProductProviderService productProviderService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductCategoryController productCategoryController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        amount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0));

        productCategoryController.getProductsData().clear();

        List<ShopProvider> shopProviders = findShopProvidersByEmployeeUsername();

        List<String> providersNameFromDB = shopProviders.stream().map(shopProvider ->
                shopProvider.getProvider().getCompanyName()).collect(Collectors.toList());
        providers.getItems().addAll(providersNameFromDB);

        if (productTable != null) {
            productTable.setOnMousePressed(event -> {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                    Integer initialAmount = amount.getValue();
                    if (initialAmount != 0) {
                        amount.decrement(initialAmount - 1);
                    } else amount.increment(1);

                    ProductDto productDto = productTable.getSelectionModel().getSelectedItem();
                    productName.setText(productDto.getName());
                    unit.setText(productDto.getUnit());
                }
            });
        }
    }

    public void findCategoriesByProvider(ActionEvent event) {
        List<ShopProvider> shopProviders = findShopProvidersByEmployeeUsername();

        ComboBox<String> source = (ComboBox<String>) event.getSource();
        String companyName = source.getValue();

        ShopProvider shopProvider = null;
        for (ShopProvider shopProviderFromDb : shopProviders) {
            if (shopProviderFromDb.getProvider().getCompanyName().equals(companyName)) {
                shopProvider = shopProviderFromDb;
            }
        }

        Provider provider = providerService.findById(shopProvider.getProvider().getId());
        this.provider = provider;

        List<ProductProvider> productsProvider = productProviderService.findByProviderId(provider.getId());

        Set<String> uniqueCategories = new HashSet<>();
        for (ProductProvider productProvider : productsProvider) {
            String categoryName = productProvider.getProduct().getCategory().getName();
            uniqueCategories.add(categoryName);
        }

        categories.getItems().addAll(uniqueCategories);
        categories.setDisable(false);
    }

    public void findProductsByCategoriesAndProvider(ActionEvent event) {
        productsData.clear();
        ComboBox<String> source = (ComboBox<String>) event.getSource();
        String categoryName = source.getValue();
        Category category = categoryService.findByName(categoryName);

        List<ProductProvider> productsProvider = productProviderService.findByProviderIdAndProductCategoryId(provider.getId(), category.getId());

        for (ProductProvider productProvider : productsProvider) {
            ProductDto productDto = new ProductDto();
            productDto.setName(productProvider.getProduct().getName());
            productDto.setBarcode(productProvider.getProduct().getBarcode());
            productDto.setUnit(productProvider.getProduct().getUnit().getSymbol());
            productsData.add(productDto);
        }

        name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        barcode.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());

        productTable.setItems(productsData);
    }

    public List<ShopProvider> findShopProvidersByEmployeeUsername() {
        long shopId = employeeService.findByUsername(getPrincipal()).getShop().getId();
        return shopProviderService.findByShopId(shopId);
    }

    @FXML
    public void calculatePrice(ActionEvent event) {
        TextField source = (TextField) event.getSource();
        String text = source.getText();
        System.out.println(text);
    }
}

