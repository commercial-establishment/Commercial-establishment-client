package kz.hts.ce.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;
import kz.hts.ce.config.FXMLDialog;
import kz.hts.ce.config.ScreensConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import static kz.hts.ce.util.SpringUtils.getPrincipal;

@Component
public class MainController implements DialogController, Initializable{

    public Label lblUsername, dateLabel;
    private FXMLDialog dialog;

    //region TODO Digital watch realization
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss", Locale.getDefault());

    Timeline watch = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
//            System.out.println("this is called every 5 seconds on UI thread");
            dateLabel.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));
        }
    }));

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
        watch.setCycleCount(Timeline.INDEFINITE);
        watch.play();
    }
}
