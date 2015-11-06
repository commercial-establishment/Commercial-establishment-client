package kz.hts.ce.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import kz.hts.ce.config.FXMLDialog;
import kz.hts.ce.config.ScreensConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static kz.hts.ce.util.SpringUtils.getPrincipal;

@Component
public class MainController implements DialogController, Initializable{

    public Label lblUsername;
    private FXMLDialog dialog;

    @Autowired
    private ScreensConfiguration screens;

    public MainController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUsername.setText(getPrincipal());
    }
}
