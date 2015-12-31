package kz.hts.ce.config;

import javafx.scene.Node;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import kz.hts.ce.controller.*;
import kz.hts.ce.controller.invoice.ReceiptController;
import kz.hts.ce.controller.invoice.ReceiptsController;
import kz.hts.ce.controller.payment.PaymentController;
import kz.hts.ce.controller.products.EditProductController;
import kz.hts.ce.controller.products.ShopProductsController;
import kz.hts.ce.controller.provider.AddProviderController;
import kz.hts.ce.controller.sale.CalculatorController;
import kz.hts.ce.controller.sale.ProductCategoryController;
import kz.hts.ce.controller.sale.ProductsController;
import kz.hts.ce.util.spring.JsonUtil;
import kz.hts.ce.util.spring.SpringFxmlLoader;
import kz.hts.ce.util.spring.SpringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.beans.EventHandler;
import java.io.IOException;

import static kz.hts.ce.util.spring.SpringFxmlLoader.showStage;

@Lazy
@Configuration
public class PagesConfiguration {

    private Stage primaryStage;
    private Stage secondaryStage;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Stage getSecondaryStage() {
        return secondaryStage;
    }

    public void setSecondaryStage(Stage secondaryStage) {
        this.secondaryStage = secondaryStage;
    }

    @PostConstruct
    public void initialize() {
        boolean isChecked = jsonUtil().checkJsonFile();
        if (!isChecked) {
            jsonUtil().create(10, 30, 3, 10, false, "");
        } else {
            jsonUtil().fillFields();
        }
    }

    @Bean
    @Scope("prototype")
    public Stage main() {
        showStage(primaryStage, "/view/main.fxml");
        return primaryStage;
    }

    @Bean
    @Scope("prototype")
    public Stage sales(){
        showStage(secondaryStage, "/view/sale.fxml");
        secondaryStage.setFullScreenExitHint("");
        secondaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        secondaryStage.setFullScreen(true);
//        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        return secondaryStage;
    }

    @Bean
    public Stage login() {
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setResizable(false);
        showStage(primaryStage, "/view/login.fxml");
        return primaryStage;
    }

    @Bean
    @Scope("prototype")
    public Stage payment() {
        showStage(primaryStage, "/view/payment.fxml");
        return primaryStage;
    }

//    @Bean
//    public Node sales() throws IOException {
//        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
//        return (Node) springFxmlLoader.load("/view/sales.fxml");
//    }

    @Bean
    @Scope("prototype")
    public Node receipts() throws IOException {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        primaryStage.getScene().getStylesheets().add("style.css");
        return (Node) springFxmlLoader.load("/view/receipts.fxml");
    }

    @Bean
    @Scope("prototype")
    public Node receipt() throws IOException {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        return (Node) springFxmlLoader.load("/view/receipt.fxml");
    }

    @Bean
    @Scope("prototype")
    public Node editProduct() throws IOException {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        return (Node) springFxmlLoader.load("/view/product-edit.fxml");
    }


    @Bean
    @Scope("prototype")
    public Node shopProducts() throws IOException {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        primaryStage.getScene().getStylesheets().add("style.css");
        return (AnchorPane) springFxmlLoader.load("/view/shop-products.fxml");
    }

    @Bean
    @Scope("prototype")
    public Node reportChecks() throws IOException {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        return (Node) springFxmlLoader.load("/view/report-by-check.fxml");
    }

    @Bean
    @Scope("prototype")
    public Node reportProducts() throws IOException {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        return (Node) springFxmlLoader.load("/view/report-by-products.fxml");
    }

    @Bean
    @Scope("prototype")
    public Node reportProviders() throws IOException {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        return (Node) springFxmlLoader.load("/view/report-by-providers.fxml");
    }

    @Bean
    @Scope("prototype")
    public Node reportSales() throws IOException {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        return (Node) springFxmlLoader.load("/view/report-by-sales.fxml");
    }

    @Bean
    @Scope("prototype")
    public Node addProvider() {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        return (AnchorPane) springFxmlLoader.load("/view/provider-add.fxml");
    }

    @Bean
    @Scope("prototype")
    public Node createProvider() {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        return (AnchorPane) springFxmlLoader.load("/view/provider-create.fxml");
    }

    @Bean
    public Node settings() {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        return (AnchorPane) springFxmlLoader.load("/view/settings.fxml");
    }

    @Bean
    public LoginController loginController() {
        return new LoginController();
    }

    @Bean
    public MainController mainController() {
        return new MainController();
    }

    @Bean
    public SalesController salesController(){
        return new SalesController();
    }

    @Bean
    public CalculatorController calculatorController() {
        return new CalculatorController();
    }

    @Bean
    public PaymentController paymentController() {
        return new PaymentController();
    }

    @Bean
    public ProductsController productsController() {
        return new ProductsController();
    }

    @Bean
    public ProductCategoryController productCategoryController() {
        return new ProductCategoryController();
    }

    @Bean
    public ShopProductsController shopProductsController() {
        return new ShopProductsController();
    }

    @Bean
    public ReceiptsController receiptsController() {
        return new ReceiptsController();
    }

    @Bean
    public ReceiptController receiptController() {
        return new ReceiptController();
    }

    @Bean
    public SettingsController settingsController() {
        return new SettingsController();
    }

    @Bean
    @Scope("prototype")
    public AddProviderController addProviderController() {
        return new AddProviderController();
    }

    @Bean
    @Scope("prototype")
    public EditProductController editProductController() {
        return new EditProductController();
    }

    @Bean
    public JsonUtil jsonUtil() {
        return new JsonUtil();
    }

    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }
}
