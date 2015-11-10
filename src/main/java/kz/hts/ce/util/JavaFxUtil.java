package kz.hts.ce.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class JavaFxUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

    public static void getWatch(Label dateLabel) {
        Timeline watch = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dateLabel.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));
            }
        }));
        watch.setCycleCount(Timeline.INDEFINITE);
        watch.play();

    }
}
