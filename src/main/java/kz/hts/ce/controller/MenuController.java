package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.controller.sale.CalculatorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class MenuController {

    @FXML
    private Button addProvider;
    @FXML
    private Button settings;
    @FXML
    private Button products;
    @FXML
    private Button createProduct;
    @FXML
    private Button sales;
    @FXML
    private Button receipts;
    @FXML
    private Button reportSales;
    @FXML
    private Button reportChecks;
    @FXML
    private Button reportProducts;
    @FXML
    private Button reportProviders;

    @Autowired
    private MainController mainController;
    @Autowired
    private CalculatorController calculatorController;

    @FXML
    private void showContent(ActionEvent event) throws IOException {
        PagesConfiguration screens = getPagesConfiguration();
        Node node;
        if (event.getSource() == sales) {
            //node = screens.sales();
//            mainController.getContentContainer().getChildren().setAll(node);
//            calculatorController.startEventHandler(node.getScene());
            Stage stage = new Stage();
            screens.setSecondaryStage(stage);
            screens.sales();
            screens.main().close();

//            calculatorController.startEventHandler(screens.sales().getScene());
        } else if (event.getSource() == receipts) {
            node = screens.receipts();
            mainController.getContentContainer().getChildren().setAll(node);
//            calculatorController.stopEventHandler(node.getScene());
        } else if (event.getSource() == products) {
            node = screens.shopProducts();
            mainController.getContentContainer().getChildren().setAll(node);
//            calculatorController.stopEventHandler(node.getScene());
        } else if (event.getSource() == settings) {
            node = screens.settings();
            mainController.getContentContainer().getChildren().setAll(node);
//            calculatorController.stopEventHandler(node.getScene());
        } else if (event.getSource() == reportChecks) {
            node = screens.reportChecks();
            mainController.getContentContainer().getChildren().setAll(node);
//            calculatorController.stopEventHandler(node.getScene());
        } else if (event.getSource() == reportProducts) {
            node = screens.reportProducts();
            mainController.getContentContainer().getChildren().setAll(node);
//            calculatorController.stopEventHandler(node.getScene());
        } else if (event.getSource() == reportProviders) {
            node = screens.reportProviders();
            mainController.getContentContainer().getChildren().setAll(node);
//            calculatorController.stopEventHandler(node.getScene());
        } else if (event.getSource() == reportSales) {
            node = screens.reportSales();
            mainController.getContentContainer().getChildren().setAll(node);
//            calculatorController.stopEventHandler(node.getScene());
        } else if (event.getSource() == addProvider) {
            node = screens.addProvider();
            mainController.getContentContainer().getChildren().setAll(node);
//            calculatorController.stopEventHandler(node.getScene());
        }
    }
}
