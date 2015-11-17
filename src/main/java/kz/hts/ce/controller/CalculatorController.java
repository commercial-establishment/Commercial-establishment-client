package kz.hts.ce.controller;

import javafx.beans.value.ChangeListener;
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
import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.service.WarehouseProductService;
import kz.hts.ce.util.AppContextSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaFxUtil.*;
import static kz.hts.ce.util.JavaUtil.createProductDtoFromWarehouseProduct;
import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class CalculatorController implements Initializable {

    private StringBuilder buttonState;
    private Button button = new Button();

    @FXML
    private TextField txtAdditionalDisplay;
    @FXML
    private TextField txtDisplay;

    @Autowired
    private WarehouseProductService warehouseProductService;
    @Autowired
    private AddProductController addProductController;
    @Autowired
    private ProductsController productsController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PagesConfiguration screens = getPagesConfiguration();
        buttonState = new StringBuilder("");
        EventHandler<KeyEvent> eventHandler = evt -> {
            buttonState.setLength(0);
            buttonState.append(evt.getCode().toString());
            CalculatorController.this.handleOnAnyButtonFromKeypad();
        };
        ChangeListener<Boolean> changeListener = (observable, oldValue, newValue) -> {
            if (newValue) screens.getPrimaryStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
        };

        screens.getPrimaryStage().focusedProperty().addListener(changeListener);
    }

    @FXML
    public void handleOnAnyButtonClicked(ActionEvent evt) {
        Button button = (Button) evt.getSource();
        final String buttonText = button.getText();
        if (buttonText.matches("^[0-9CE\\s[*+.]\\s]*$")) {
            calculator(buttonText, txtDisplay, txtAdditionalDisplay);
        }
    }

    public void handleOnAnyButtonFromKeypad() {
        button.setText(String.valueOf(buttonState));
        String buttonText = button.getText();
        if (buttonText.startsWith("NUMPAD")) {
            String[] splittedButtonText = buttonText.split("NUMPAD");
            buttonText = splittedButtonText[1];
        } else if (button.getText().equals("DECIMAL")) {
            buttonText = ".";
        } else if (button.getText().equals("MULTIPLY")) {
            buttonText = "*";
        } else if (button.getText().equals("BACK_SPACE")) {
            buttonText = "CE";
        }
        if (buttonText.matches("^[0-9CE\\s[*+.]\\s]*$")) {
            calculator(buttonText, txtDisplay, txtAdditionalDisplay);
        } else if (buttonText.matches("ADD")) {
            addProductPage();
        } else if (buttonText.matches("ENTER")) {
            findAndAddProductByBarcode();
        } else if (buttonText.matches("SUBTRACT")) {
            deleteSelectedProductFromTable();
        } else if (buttonText.matches("EQUALS")) {
            paymentPage();
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
        if (!productsController.getPriceResult().getText().equals("")) {
            ApplicationContext context = AppContextSingleton.getInstance();
            PagesConfiguration screens = context.getBean(PagesConfiguration.class);
            Stage stage = new Stage();
            screens.setPrimaryStage(stage);
            screens.payment();
        } else alert(Alert.AlertType.WARNING, "Товар не выбран", null, "Извините, список товаров пуст.");
    }

    public void findAndAddProductByBarcode() {
        try {
            String barcode = txtDisplay.getText();
            WarehouseProduct warehouseProduct = warehouseProductService.findByProductBarcode(Long.parseLong(barcode));
            if (warehouseProduct != null) {
                if (txtAdditionalDisplay.getText().equals("") || txtAdditionalDisplay.getText().equals("*")) {
                    txtAdditionalDisplay.setText("*1");
                }
                String[] splittedAmount = txtAdditionalDisplay.getText().split("\\*");
                ProductDto productDto = createProductDtoFromWarehouseProduct(warehouseProduct, Integer.parseInt(splittedAmount[1]));
                productsController.setProductDtoToProductsDto(productDto);
                productsController.addProductsToTable();
            } else
                alert(Alert.AlertType.WARNING, "Товар не найден", null, "Товар с данным штрих-кодом отсутствует!");
        } catch (NumberFormatException e) {
            alert(Alert.AlertType.WARNING, "Неверный штрих-код", null, "Штрих-код введён неверно!");
        }
    }

    public void deleteSelectedProductFromTable() {
        productsController.deleteSelectedProductFromTable();
    }
}
