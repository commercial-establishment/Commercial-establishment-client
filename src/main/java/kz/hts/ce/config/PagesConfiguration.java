package kz.hts.ce.config;

import javafx.stage.Stage;
import kz.hts.ce.controller.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import static kz.hts.ce.util.SpringFxmlLoader.showStage;

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

    @Bean
    @Scope("prototype")
    public Stage main() {
        showStage(primaryStage, "/view/main.fxml");
        return primaryStage;
    }

    @Bean
    @Scope("prototype")
    public Stage login() {
        showStage(primaryStage, "/view/login.fxml");
        return primaryStage;
    }

    @Bean
    @Scope("prototype")
    public Stage addProduct() {
        showStage(primaryStage, "/view/add-product.fxml");
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
    public Stage products() {
        showStage(primaryStage, "/view/products.fxml");
        return primaryStage;
    }

    @Bean
    @Scope("singleton")
    public LoginController loginController() {
        return new LoginController();
    }

    @Bean
    @Scope("singleton")
    public MainController mainController() {
        return new MainController();
    }

    @Bean
    @Scope("singleton")
    public CalculatorController calculatorController() {
        return new CalculatorController();
    }

    @Bean
    @Scope("singleton")
    public PaymentController paymentController() {
        return new PaymentController();
    }

    @Bean
    @Scope("singleton")
    public ProductsController productsController() {
        return new ProductsController();
    }

    @Bean
    @Scope("singleton")
    public AddProductController addProductController() {
        return new AddProductController();
    }
}
