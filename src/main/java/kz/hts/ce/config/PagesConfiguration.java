package kz.hts.ce.config;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kz.hts.ce.controller.*;
import kz.hts.ce.controller.invoice.AddReceiptController;
import kz.hts.ce.controller.invoice.EditReceiptController;
import kz.hts.ce.controller.invoice.ReceiptsController;
import kz.hts.ce.controller.payment.PaymentController;
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
import java.io.IOException;

import static kz.hts.ce.util.spring.SpringFxmlLoader.showStage;

@Lazy
@Configuration
public class PagesConfiguration {

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @PostConstruct
    public void initialize() {
        boolean isChecked = jsonUtil().checkJsonFile();
        if (!isChecked) {
            jsonUtil().create(10, 30, 3, 10, false);
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
    public Stage login() {
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setResizable(false);
        showStage(primaryStage, "/view/login.fxml");
        return primaryStage;
    }

    @Bean
    @Scope("prototype")
    public Stage payment() {
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setResizable(false);
        showStage(primaryStage, "/view/payment.fxml");
        return primaryStage;
    }

    @Bean
    @Scope("prototype")
    public Node receipts() throws IOException {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        return (Node) springFxmlLoader.load("/view/receipts.fxml");
    }

    @Bean
    @Scope("prototype")
    public Node addReceipt() throws IOException {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        return (Node) springFxmlLoader.load("/view/add-receipt.fxml");
    }

    @Bean
    @Scope("prototype")
    public Node editReceipt() throws IOException {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        return (Node) springFxmlLoader.load("/view/receipt-edit.fxml");
    }

    @Bean
    @Scope("prototype")
    public Node shopProducts() throws IOException {
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        primaryStage.getScene().getStylesheets().add("style.css");
        return (AnchorPane) springFxmlLoader.load("/view/shop-products.fxml");
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
    public AddReceiptController addReceiptController() {
        return new AddReceiptController();
    }

    @Bean
    public EditReceiptController editReceiptController() {
        return new EditReceiptController();
    }

    @Bean
    public ReceiptsController receiptsController() {
        return new ReceiptsController();
    }

    @Bean
    public SettingsController settingsController() {
        return new SettingsController();
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
