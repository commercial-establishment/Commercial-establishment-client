package kz.hts.ce.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class SettingsController implements Initializable{

    private Integer min;
    private Integer max;

    @FXML
    private TextField minValue;
    @FXML
    private TextField maxValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    @FXML
    public void update(){
        min = Integer.parseInt(minValue.getText());
        max = Integer.parseInt(maxValue.getText());
    }
    public Integer getMax() {
        return max;
    }

    public Integer getMin() {
        return min;
    }
}
