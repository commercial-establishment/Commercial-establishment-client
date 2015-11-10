package kz.hts.ce.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaFxUtil.getWatch;

@Component
public class MainController implements Initializable {

    public Label dateLabel;
    public Button button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getWatch(dateLabel);
    }
}
