package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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

    @FXML
    private ComboBox<String> categories;
    @FXML
    private ComboBox<String> products;
    @FXML
    private ComboBox<String> providers;

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
        productCategoryController.getProductsData().clear();

        List<ShopProvider> shopProviders = findShopProvidersByEmployeeUsername();

        List<String> providersNameFromDB = shopProviders.stream().map(shopProvider ->
                shopProvider.getProvider().getCompanyName()).collect(Collectors.toList());
        providers.getItems().addAll(providersNameFromDB);
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
        ComboBox<String> source = (ComboBox<String>) event.getSource();
        String categoryName = source.getValue();
        Category category = categoryService.findByName(categoryName);

        List<ProductProvider> productsProvider = productProviderService.findByProviderIdAndProductCategoryId(provider.getId(), category.getId());

        Map<Long, String> productMap = new HashMap<>();
        for (ProductProvider productProvider : productsProvider) {
            long productProviderId = productProvider.getId();
            String productName = productProvider.getProduct().getName();
            productMap.put(productProviderId, productName);
        }

        products.getItems().addAll(productMap.values());
        products.setDisable(false);
    }

    public List<ShopProvider> findShopProvidersByEmployeeUsername() {
        long shopId = employeeService.findByUsername(getPrincipal()).getShop().getId();
        return shopProviderService.findByShopId(shopId);
    }
}

