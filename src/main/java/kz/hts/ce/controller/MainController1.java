package kz.hts.ce.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.springframework.ui.Model;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController1 implements Initializable{

    @FXML
    private Button button;
    @FXML
    private Label dateLabel, label;
    @FXML
    private Stage stage;

    public MainController1() {
    }

    @FXML
    public void change(ActionEvent event) throws IOException {
        dateLabel.setText("You are clicked button");
        System.out.println("You clicked me!");
        //Here I want to swap the screen!

        Parent parent = FXMLLoader.load(getClass().getResource("/RootLayout.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }


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
    //endregion
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        watch.setCycleCount(Timeline.INDEFINITE);
        watch.play();
    }
}