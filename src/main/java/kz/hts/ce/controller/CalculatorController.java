package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.ShopProduct;
import kz.hts.ce.service.ShopProductService;
import kz.hts.ce.util.AppContextSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import static kz.hts.ce.util.JavaFxUtil.calculator;
import static kz.hts.ce.util.JavaFxUtil.readProductFields;
import static kz.hts.ce.util.JavaUtil.createProductDtoFromShopProduct;

@Controller
public class CalculatorController {

    @FXML
    private TextField txtAdditionalDisplay;
    @FXML
    private TextField txtDisplay;
    @Autowired
    private AddProductController addProductController;
    @Autowired
    private ShopProductService shopProductService;
    @Autowired
    private ProductsController productsController;

    @FXML
    public void handleOnAnyButtonClicked(ActionEvent evt) {
        Button button = (Button) evt.getSource();
        final String buttonText = button.getText();
        if (buttonText.matches("^[0-9C\\s[×+－=.]\\s]*$")) {
            calculator(buttonText, txtDisplay, txtAdditionalDisplay);
        }
    }

    @FXML
    public void addProductPage() {
        String displayText = txtDisplay.getText();
        if (!displayText.equals("") && displayText.matches("^[1-9C\\s[.]\\s]*$")) {
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
        String barcode = txtDisplay.getText();
        ShopProduct shopProduct = shopProductService.findByProductBarcode(Long.parseLong(barcode));
        if (txtAdditionalDisplay.getText().equals("")) {
            txtAdditionalDisplay.setText("×1");
        }
        String[] splittedAmount = txtAdditionalDisplay.getText().split("×");
        ProductDto productDto = createProductDtoFromShopProduct(shopProduct, Integer.parseInt(splittedAmount[1]));
        productsController.setProductDtoToProductsDto(productDto);
        productsController.addProductsToTable();
    }
}
