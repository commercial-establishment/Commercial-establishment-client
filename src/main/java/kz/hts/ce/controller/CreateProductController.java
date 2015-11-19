package kz.hts.ce.controller;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.*;
import kz.hts.ce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static kz.hts.ce.util.SpringUtil.getPrincipal;

@Controller
public class CreateProductController implements Initializable {

    @FXML
    private ComboBox<String> providers;

    @Autowired
    private ShopProviderService shopProviderService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ProductProviderService productProviderService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProviderService providerService;

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
    }
//
//    public ChangeListener<String> productsProviderListener(Provider provider) {
//            return new ChangeListener<String>() {
//                @Override
//                public void changed(ObservableValue<? extends String> ov, String oldVal, String newVal) {
//                    if (productCategoryController.isFlag()) {
//                        productCategoryController.getProductsData().clear();
//                        Category category = categoryService.findByName(newVal);
//                        List<ProductProvider> productsProvider = productProviderService.
//                                findByProviderIdAndProductCategoryId(provider.getId(), category.getId());
//                        for (ProductProvider productProvider : productsProvider) {
//                            ProductDto productDto = new ProductDto();
//                            productDto.setName(productProvider.getProduct().getName());
//                            productDto.setPrice(productProvider.getPrice());
//                            productDto.setResidue(0);
//                            productCategoryController.getProductsData().add(productDto);
//                        }
//                        productCategoryController.getName().setCellValueFactory(cellData -> cellData.getValue().nameProperty());
//                        productCategoryController.getPrice().setCellValueFactory(cellData -> cellData.getValue().priceProperty());
//                        productCategoryController.getResidue().setCellValueFactory(cellData -> cellData.getValue().residueProperty());
//                        productCategoryController.getCategoryProductsTable().setItems(productCategoryController.getProductsData());
//                    }
//                    productCategoryController.setFlag(true);
//                }
//            };
//    }

    public List<ShopProvider> findShopProvidersByEmployeeUsername() {
        long shopId = employeeService.findByUsername(getPrincipal()).getShop().getId();
        return shopProviderService.findByShopId(shopId);
    }
}

