package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import kz.hts.ce.config.PagesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

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

    @FXML
    private void show(ActionEvent event) throws IOException {
        Node node;
        if (event.getSource() == btnCreateProduct) {
            node = pagesConfiguration.createProducts();
            mainController.getcontentContainer().getChildren().setAll(node);
        } else if (event.getSource() == btnSales) {
            node = pagesConfiguration.sales();
            mainController.getcontentContainer().getChildren().setAll(node);
        }
    }
}
