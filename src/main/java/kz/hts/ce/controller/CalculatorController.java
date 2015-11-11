package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.util.AppContextSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static kz.hts.ce.util.JavaFxUtil.calculator;
import static kz.hts.ce.util.JavaUtil.StringToBigDecimal;
import static kz.hts.ce.util.JavaUtil.calculateCost;

@Controller
public class CalculatorController {

    @FXML
    public TextField txtAdditionalDisplay;
    @FXML
    private TextField txtDisplay;
    @Autowired
    private AddProductController addProductController;

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

            TextField txtVat = addProductController.getVat();
            TextField txtAmount = addProductController.getAmount();
            TextField txtPrice = addProductController.getPrice();

            txtPrice.setText(txtDisplay.getText());
            String additionalDisplayText = txtAdditionalDisplay.getText();
            String[] splittedAdditionalDisplay = additionalDisplayText.split("×");
            txtAmount.setText(splittedAdditionalDisplay[1]);

            txtVat.setText("10");
            int vat = Integer.parseInt(txtVat.getText());
            int amount = Integer.parseInt(txtAmount.getText());
            BigDecimal price = StringToBigDecimal(txtPrice.getText());

            List<Integer> integerParameters = new ArrayList<>();
            integerParameters.add(vat / 100);
            integerParameters.add(amount);
            BigDecimal total = calculateCost(integerParameters, price);
            addProductController.getPriceWithVat().setText(String.valueOf(total));
            addProductController.getTotalPrice().setText(String.valueOf(total));
        }
    }
}
