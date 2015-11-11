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

import static kz.hts.ce.util.JavaFxUtil.calculator;

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

            addProductController.getPrice().setText(txtDisplay.getText());
            String additionalDisplayText = txtAdditionalDisplay.getText();
            String[] splittedAdditionalDisplay = additionalDisplayText.split("×");
            addProductController.getAmount().setText(splittedAdditionalDisplay[1]);
            addProductController.getVat().setText("10");
            addProductController.getPriceWithVat().setText(String.valueOf(0.1 * 88 * 9));
            addProductController.getTotalPrice().setText(String.valueOf(0.1 * 88 * 9));
        }
    }
}
