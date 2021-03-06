package kz.hts.ce.config;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kz.hts.ce.controller.LoginController;
import kz.hts.ce.controller.MainController;
import kz.hts.ce.controller.SalesController;
import kz.hts.ce.controller.SettingsController;
import kz.hts.ce.controller.receipt.ReceiptController;
import kz.hts.ce.controller.receipt.ReceiptsController;
import kz.hts.ce.controller.payment.PaymentController;
import kz.hts.ce.controller.products.EditProductController;
import kz.hts.ce.controller.products.ShopProductsController;
import kz.hts.ce.controller.provider.AddProviderController;
import kz.hts.ce.controller.provider.CreateProviderController;
import kz.hts.ce.util.spring.JsonUtil;
import kz.hts.ce.util.spring.SpringFxmlLoader;
import kz.hts.ce.util.spring.SpringHelper;
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
    public Stage sales() {
        showStage(secondaryStage, "/view/sale.fxml");
        secondaryStage.setFullScreenExitHint("");
        secondaryStage.setFullScreen(true);
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
    public SpringHelper springHelper() {
        return new SpringHelper();
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
    public SalesController salesController() {
        return new SalesController();
    }

    @Bean
    public PaymentController paymentController() {
        return new PaymentController();
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
    public CreateProviderController createProviderController() {
        return new CreateProviderController();
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
}
