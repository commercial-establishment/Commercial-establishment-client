package kz.hts.ce.controller.provider;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import kz.hts.ce.model.entity.City;
import kz.hts.ce.model.entity.Provider;
import kz.hts.ce.model.entity.Role;
import kz.hts.ce.service.CityService;
import kz.hts.ce.service.ProviderService;
import kz.hts.ce.service.RoleService;
import kz.hts.ce.util.javafx.fields.IntegerTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static kz.hts.ce.util.javafx.JavaFxUtil.alert;

@Controller
public class CreateProviderController implements Initializable {

    public static final String PROVIDER = "PROVIDER";
    @FXML
    private ComboBox<String> cities;
    @FXML
    private TextField companyName;
    @FXML
    private TextField contactPerson;
    @FXML
    private TextField email;
    @FXML
    private TextField address;
    @FXML
    private IntegerTextField iin;
    @FXML
    private IntegerTextField bin;

    @Autowired
    private ProviderService providerService;
    @Autowired
    private CityService cityService;
    @Autowired
    private RoleService roleService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<City> cities = cityService.findAll();
        List<String> cityNames = cities.stream().map(City::getName).collect(Collectors.toList());
        this.cities.getItems().addAll(cityNames);
    }

    @FXML
    private void save() {
        try {
            Provider provider = new Provider();
            provider.setRole(roleService.findByName(PROVIDER));
            provider.setStartWorkDate(new Date());
            provider.setCompanyName(companyName.getText());
            provider.setContactPerson(contactPerson.getText());
            provider.setEmail(email.getText());
            provider.setAddress(address.getText());
            provider.setBlocked(false);
            provider.setCity(cityService.findByName(this.cities.getEditor().getText()));
            String iin = this.iin.getText();
            provider.setIin(Integer.parseInt(iin));
//            if (!iin.isEmpty())
            String bin = this.bin.getText();
            if (!bin.isEmpty()) provider.setBin(Integer.parseInt(bin));
            providerService.save(provider);
        } catch (RuntimeException e) {
            alert(Alert.AlertType.WARNING, "Поля заполнены неверно", null, "Пожалуйста, введите данные корректно.");
        }
    }
}
