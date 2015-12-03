package kz.hts.ce.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class SettingsController implements Initializable{

    private Integer min;
    private Integer max;

    @FXML
    private Spinner<Integer> minValue;
    @FXML
    private Spinner<Integer> maxValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        minValue.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0));
        maxValue.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0));
    }
    @FXML
    public void update(){
        String textMin = minValue.getEditor().getText();
        SpinnerValueFactory<Integer> valueFactoryMin = minValue.getValueFactory();
        if (valueFactoryMin != null) {
            StringConverter<Integer> converterMin = valueFactoryMin.getConverter();
            if (converterMin != null) {
                Integer valueMin = converterMin.fromString(textMin);
                valueFactoryMin.setValue(valueMin);
                min = valueFactoryMin.getValue();
            }
        }
        String textMax = maxValue.getEditor().getText();
        SpinnerValueFactory<Integer> valueFactoryMax = maxValue.getValueFactory();
        if (valueFactoryMax != null) {
            StringConverter<Integer> converterMax = valueFactoryMax.getConverter();
            if (converterMax != null) {
                Integer valueMax = converterMax.fromString(textMax);
                valueFactoryMax.setValue(valueMax);
                max = valueFactoryMax.getValue();
            }
        }

    }
    public Integer getMax() {
        return max;
    }

    public Integer getMin() {
        return min;
    }
}
