package kz.hts.ce.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import org.springframework.stereotype.Controller;

import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class AddReceiptPageController {

    @FXML
    public void handle(){
        PagesConfiguration screens = getPagesConfiguration();
        Stage stage = new Stage();
        screens.setPrimaryStage(stage);
        screens.addReceipt();
    }
}
