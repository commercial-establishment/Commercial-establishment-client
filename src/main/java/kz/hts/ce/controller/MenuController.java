package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class MenuController {

    @Autowired
    private MainController mainController;
    @FXML
    private Button btnStock;
    private Node node;

    @FXML
    private void show(ActionEvent event) throws IOException {
        node = (Node)FXMLLoader.load(getClass().getResource("/view/sidebar-menu.fxml"));
        mainController.getPaneContainer().getChildren().setAll(node);
    }
}
