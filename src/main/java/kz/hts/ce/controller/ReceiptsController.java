package kz.hts.ce.controller;

import javafx.fxml.FXML;
import kz.hts.ce.config.PagesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class ReceiptsController {

    private PagesConfiguration screens;
    @Autowired
    private MainController mainController;
    @FXML
    public void createReceipt() throws IOException {
        screens = getPagesConfiguration();
        mainController.getContentContainer().getChildren().setAll(screens.addReceipt());
    }
}
