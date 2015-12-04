package kz.hts.ce.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;
import kz.hts.ce.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class SettingsController implements Initializable {

    public Spinner<Integer> invoiceMin;
    public Spinner<Integer> invoiceMax;
    private Integer productMinInt;
    private Integer productMaxInt;

    @Autowired
    private JsonUtil jsonUtil;

    @FXML
    private Spinner<Integer> productMin;
    @FXML
    private Spinner<Integer> productMax;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productMin.getEditor().setText(String.valueOf(jsonUtil.getMin()));
        productMax.getEditor().setText(String.valueOf(jsonUtil.getMax()));
        productMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, jsonUtil.getMin()));
        productMax.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, jsonUtil.getMax()));
    }

    @FXML
    public void update() {
        String textMin = productMin.getEditor().getText();
        SpinnerValueFactory<Integer> valueFactoryMin = productMin.getValueFactory();
        if (valueFactoryMin != null) {
            StringConverter<Integer> converterMin = valueFactoryMin.getConverter();
            if (converterMin != null) {
                Integer valueMin = converterMin.fromString(textMin);
                valueFactoryMin.setValue(valueMin);
                productMinInt = valueFactoryMin.getValue();
                jsonUtil.setMin(Integer.parseInt(textMin));
            }
        }
        String textMax = productMax.getEditor().getText();
        SpinnerValueFactory<Integer> valueFactoryMax = productMax.getValueFactory();
        if (valueFactoryMax != null) {
            StringConverter<Integer> converterMax = valueFactoryMax.getConverter();
            if (converterMax != null) {
                Integer valueMax = converterMax.fromString(textMax);
                valueFactoryMax.setValue(valueMax);
                productMaxInt = valueFactoryMax.getValue();
                jsonUtil.setMax(Integer.parseInt(textMax));
            }
        }
        jsonUtil.update(jsonUtil.getMin(), jsonUtil.getMax());
    }

    public Integer getProductMaxInt() {
        return productMaxInt;
    }

    public Integer getProductMinInt() {
        return productMinInt;
    }

    public void setProductMinInt(Integer productMinInt) {
        this.productMinInt = productMinInt;
    }

    public void setProductMaxInt(Integer productMaxInt) {
        this.productMaxInt = productMaxInt;
    }
}
