package kz.hts.ce.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.ShopProduct;
import kz.hts.ce.service.ShopProductService;
import kz.hts.ce.util.AppContextSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaFxUtil.*;
import static kz.hts.ce.util.JavaUtil.createProductDtoFromShopProduct;
import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class CalculatorController implements Initializable {

    @FXML
    private TextField txtAdditionalDisplay;
    @FXML
    private TextField txtDisplay;
    @Autowired
    private ShopProductService shopProductService;
    @Autowired
    private AddProductController addProductController;/*TODO working via services*/
    @Autowired
    private ProductsController productsController;/*TODO working via services*/

    @FXML
    public void handleOnAnyButtonClicked(ActionEvent evt) {
        Button button = (Button) evt.getSource();
        final String buttonText = button.getText();
        if (buttonText.matches("^[0-9CE\\s[×+－=.]\\s]*$")) {
            calculator(buttonText, txtDisplay, txtAdditionalDisplay);
        }
    }

    @FXML
    public void addProductPage() {
        String displayText = txtDisplay.getText();
        if (!displayText.equals("") && displayText.matches("^[1-9CE\\s[.]\\s]*$")) {
            ApplicationContext context = AppContextSingleton.getInstance();
            PagesConfiguration screens = context.getBean(PagesConfiguration.class);
            Stage stage = new Stage();
            screens.setPrimaryStage(stage);
            screens.addProduct();

            readProductFields(addProductController, txtDisplay, txtAdditionalDisplay);
        }
    }

    @FXML
    public void paymentPage() {
        ApplicationContext context = AppContextSingleton.getInstance();
        PagesConfiguration screens = context.getBean(PagesConfiguration.class);
        Stage stage = new Stage();
        screens.setPrimaryStage(stage);
        screens.payment();
    }

    public void findAndAddProductByBarcode() {
        try {
            String barcode = txtDisplay.getText();
            ShopProduct shopProduct = shopProductService.findByProductBarcode(Long.parseLong(barcode));
            if (shopProduct != null) {
                if (txtAdditionalDisplay.getText().equals("")) {
                    txtAdditionalDisplay.setText("×1");
                }
                String[] splittedAmount = txtAdditionalDisplay.getText().split("×");
                ProductDto productDto = createProductDtoFromShopProduct(shopProduct, Integer.parseInt(splittedAmount[1]));
                productsController.setProductDtoToProductsDto(productDto);
                productsController.addProductsToTable();
            } else
                alertWarning(Alert.AlertType.WARNING, "Товар не найден", null, "Товар с данным штрих-кодом отсутствует!");
        } catch (NumberFormatException e) {
            alertWarning(Alert.AlertType.WARNING, "Неверный штрих-код", null, "Штрих-код введён неверно!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PagesConfiguration screens = getPagesConfiguration();

        EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getText() + " " + this.hashCode());
            }
        };
        ChangeListener<Boolean> changeListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("focused?" + newValue + " " + this.hashCode());
                if (newValue) {
                    screens.getPrimaryStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
                }
            }
        };

        screens.getPrimaryStage().focusedProperty().addListener(changeListener);
    }
}
