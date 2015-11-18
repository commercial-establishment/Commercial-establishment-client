package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class MenuController {

    @FXML
    private Button btnStock;
    @FXML
    private Node node;

    @Autowired
    private MainController mainController;

    @FXML
    private void show(ActionEvent event) throws IOException {
        node = (Node) FXMLLoader.load(getClass().getResource("/view/sidebar-menu.fxml"));
        mainController.getPaneContainer().getChildren().setAll(node);
    }
}
