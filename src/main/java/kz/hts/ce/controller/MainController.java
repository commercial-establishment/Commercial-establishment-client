package kz.hts.ce.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    @FXML
    private Button button;
    @FXML
    public Label dateLabel;

    @FXML
    public void change(ActionEvent event){
    dateLabel.setText("changed");
    }


    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss", Locale.getDefault());

    Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
//            System.out.println("this is called every 5 seconds on UI thread");
            dateLabel.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));
        }
    }));
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }
}