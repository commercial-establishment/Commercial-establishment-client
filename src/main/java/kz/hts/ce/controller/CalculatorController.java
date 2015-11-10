package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.util.AppContextSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import static kz.hts.ce.util.JavaFxUtil.calculator;

@Controller
public class CalculatorController {

    @FXML
    private TextField display;

    @FXML
    public void handleOnAnyButtonClicked(ActionEvent evt) {
        Button button = (Button) evt.getSource();
        final String buttonText = button.getText();
        if (buttonText.matches("^[0-9C\\s[×+－=.]\\s]*$")) {
            calculator(buttonText, display);
        }
    }

    @FXML
    public void addProductPage() {
        String displayText = display.getText();
        if (!displayText.equals("") && displayText.matches("^[0-9C\\s[.]\\s]*$")) {
            ApplicationContext context = AppContextSingleton.getInstance();
            PagesConfiguration screens = context.getBean(PagesConfiguration.class);
            Stage stage = new Stage();
            screens.setPrimaryStage(stage);
            screens.addProduct();
        }
    }
}
