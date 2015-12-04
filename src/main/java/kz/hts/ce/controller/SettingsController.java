package kz.hts.ce.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;
import kz.hts.ce.util.spring.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class SettingsController implements Initializable {

    private Integer productMinInt;
    private Integer productMaxInt;
    private Integer invoiceMinInt;
    private Integer invoiceMaxInt;

    @Autowired
    private JsonUtil jsonUtil;

    @FXML
    private Spinner<Integer> productMin;
    @FXML
    private Spinner<Integer> productMax;
    @FXML
    private Spinner<Integer> invoiceMin;
    @FXML
    private Spinner<Integer> invoiceMax;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productMin.getEditor().setText(String.valueOf(jsonUtil.getProductMinInt()));
        productMax.getEditor().setText(String.valueOf(jsonUtil.getProductMaxInt()));
        productMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, jsonUtil.getProductMinInt()));
        productMax.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, jsonUtil.getProductMaxInt()));
        invoiceMin.getEditor().setText(String.valueOf(jsonUtil.getInvoiceMinInt()));
        invoiceMax.getEditor().setText(String.valueOf(jsonUtil.getInvoiceMaxInt()));
        invoiceMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, jsonUtil.getInvoiceMinInt()));
        invoiceMax.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, jsonUtil.getInvoiceMaxInt()));
    }

    @FXML
    public void update() {
        String productMin = this.productMin.getEditor().getText();
        SpinnerValueFactory<Integer> productValueFactoryMin = this.productMin.getValueFactory();
        if (productValueFactoryMin != null) {
            StringConverter<Integer> converterMin = productValueFactoryMin.getConverter();
            if (converterMin != null) {
                Integer valueMin = converterMin.fromString(productMin);
                productValueFactoryMin.setValue(valueMin);
                productMinInt = productValueFactoryMin.getValue();
                jsonUtil.setProductMinInt(Integer.parseInt(productMin));
            }
        }
        String productMax = this.productMax.getEditor().getText();
        SpinnerValueFactory<Integer> productValueFactoryMax = this.productMax.getValueFactory();
        if (productValueFactoryMax != null) {
            StringConverter<Integer> converterMax = productValueFactoryMax.getConverter();
            if (converterMax != null) {
                Integer valueMax = converterMax.fromString(productMax);
                productValueFactoryMax.setValue(valueMax);
                productMaxInt = productValueFactoryMax.getValue();
                jsonUtil.setProductMaxInt(Integer.parseInt(productMax));
            }
        }
        String invoiceMin = this.invoiceMin.getEditor().getText();
        SpinnerValueFactory<Integer> invoiceValueFactoryMin = this.invoiceMin.getValueFactory();
        if (invoiceValueFactoryMin != null) {
            StringConverter<Integer> converterMin = invoiceValueFactoryMin.getConverter();
            if (converterMin != null) {
                Integer valueMin = converterMin.fromString(invoiceMin);
                invoiceValueFactoryMin.setValue(valueMin);
                invoiceMinInt = invoiceValueFactoryMin.getValue();
                jsonUtil.setInvoiceMinInt(Integer.parseInt(invoiceMin));
            }
        }
        String invoiceMax = this.invoiceMax.getEditor().getText();
        SpinnerValueFactory<Integer> invoiceValueFactoryMax = this.invoiceMax.getValueFactory();
        if (invoiceValueFactoryMax != null) {
            StringConverter<Integer> converterMax = invoiceValueFactoryMax.getConverter();
            if (converterMax != null) {
                Integer valueMax = converterMax.fromString(invoiceMax);
                invoiceValueFactoryMax.setValue(valueMax);
                invoiceMaxInt = invoiceValueFactoryMax.getValue();
                jsonUtil.setInvoiceMaxInt(Integer.parseInt(invoiceMax));
            }
        }
        jsonUtil.update(jsonUtil.getProductMinInt(), jsonUtil.getProductMaxInt(),
                jsonUtil.getInvoiceMinInt(), jsonUtil.getInvoiceMaxInt());
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

    public Integer getInvoiceMaxInt() {
        return invoiceMaxInt;
    }

    public void setInvoiceMaxInt(Integer invoiceMaxInt) {
        this.invoiceMaxInt = invoiceMaxInt;
    }

    public Integer getInvoiceMinInt() {
        return invoiceMinInt;
    }

    public void setInvoiceMinInt(Integer invoiceMinInt) {
        this.invoiceMinInt = invoiceMinInt;
    }
}
