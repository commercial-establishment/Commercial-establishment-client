package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        calculator(evt, display);
    }

    @FXML
    public void addProductPage(ActionEvent event) {
        ApplicationContext context = AppContextSingleton.getInstance();
        PagesConfiguration screens = context.getBean(PagesConfiguration.class);
        Stage stage = new Stage();
        screens.setPrimaryStage(stage);
        screens.addProduct();
    }
}
