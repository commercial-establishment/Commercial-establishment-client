package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.Map;

public class SampleController {


    @FXML
    private Label label;
    @FXML
    private Button button;
    @FXML
    private void handler(ActionEvent event){
        button.setText("Clicked");
    }

}
