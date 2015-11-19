package kz.hts.ce.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import kz.hts.ce.config.PagesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class MenuController {

    @FXML
    private Button btnCreateProduct;
    @FXML
    private Button btnSales;

    @Autowired
    private MainController mainController;
    @Autowired
    private PagesConfiguration pagesConfiguration;
    @Autowired
    private CalculatorController calculatorController;

    @FXML
    private void show(ActionEvent event) throws IOException {
        Node node;
        PagesConfiguration screens = getPagesConfiguration();
        if (event.getSource() == btnCreateProduct) {
            node = pagesConfiguration.createProducts();
            mainController.getContentContainer().getChildren().setAll(node);
            screens.getPrimaryStage().focusedProperty().removeListener(calculatorController.getChangeListener());
            calculatorController.stopListening();
        } else if (event.getSource() == btnSales) {
            node = pagesConfiguration.sales();
            mainController.getContentContainer().getChildren().setAll(node);
        }
    }
}
