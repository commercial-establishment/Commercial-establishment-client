package kz.hts.ce.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import kz.hts.ce.config.ScreensConfiguration;
import kz.hts.ce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaFxUtil.getWatch;

@Component
public class MainController implements Initializable {

    public Label dateLabel;
    public Button button;
    @Autowired
    private ScreensConfiguration screens;
    @Autowired
    private CalculatorController calculatorController;
    @Autowired
    private CategoryService categoryService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getWatch(dateLabel);
    }
}
