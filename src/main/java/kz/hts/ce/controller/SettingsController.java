package kz.hts.ce.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import kz.hts.ce.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class SettingsController implements Initializable {

    private Integer min;
    private Integer max;

    @Autowired
    private JsonUtil jsonUtil;

    @FXML
    private Spinner<Integer> minValue;
    @FXML
    private Spinner<Integer> maxValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        minValue.getEditor().setText(String.valueOf(jsonUtil.getMin()));
        maxValue.getEditor().setText(String.valueOf(jsonUtil.getMax()));
        minValue.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, jsonUtil.getMin()));
        maxValue.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, jsonUtil.getMax()));
    }

    @FXML
    public void update() {
        String textMin = minValue.getEditor().getText();
        SpinnerValueFactory<Integer> valueFactoryMin = minValue.getValueFactory();
        if (valueFactoryMin != null) {
            StringConverter<Integer> converterMin = valueFactoryMin.getConverter();
            if (converterMin != null) {
                Integer valueMin = converterMin.fromString(textMin);
                valueFactoryMin.setValue(valueMin);
                min = valueFactoryMin.getValue();
                jsonUtil.setMin(Integer.parseInt(textMin));
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
                jsonUtil.setMax(Integer.parseInt(textMax));
            }
        }
        jsonUtil.update(jsonUtil.getMin(), jsonUtil.getMax());
    }

    public Integer getMax() {
        return max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public void setMax(Integer max) {
        this.max = max;
    }
}
