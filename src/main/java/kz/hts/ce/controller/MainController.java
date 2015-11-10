package kz.hts.ce.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import kz.hts.ce.config.FXMLDialog;
import kz.hts.ce.config.ScreensConfiguration;
import kz.hts.ce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class MainController implements DialogController, Initializable {

    private FXMLDialog dialog;
    public Label dateLabel;
    public Button button;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

    @Autowired
    private ScreensConfiguration screens;
    @Autowired
    private CalculatorController calculatorController;
    @Autowired
    private CategoryService categoryService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getWatch();
    }

    public void getWatch() {
        Timeline watch = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dateLabel.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));
            }
        }));
        watch.setCycleCount(Timeline.INDEFINITE);
        watch.play();

    }
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
}
