package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import kz.hts.ce.model.entity.ShopProvider;
import kz.hts.ce.service.EmployeeService;
import kz.hts.ce.service.ShopProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static kz.hts.ce.util.SpringUtil.getPrincipal;

@Controller
public class AddReceiptController implements Initializable {

    @FXML
    private TextField priceWithVat;
    @FXML
    private TextField price;
    @FXML
    private CheckBox vat;
    @FXML
    private Spinner postponement;
    @FXML
    private ComboBox<String> provider;
    @FXML
    private DatePicker dateOfCreation;
    @FXML
    private TextField number;

    @Autowired
    private ShopProviderService shopProviderService;
    @Autowired
    private EmployeeService employeeService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        long shopId = employeeService.findByUsername(getPrincipal()).getShop().getId();
        List<ShopProvider> shopProviders = shopProviderService.findByShopId(shopId);

        List<String> providers = shopProviders.stream().map(shopProvider -> shopProvider.getProvider().getCompanyName()).collect(Collectors.toList());

        provider.getItems().addAll(providers);
    }

    public void checkPrice(ActionEvent event) {
        System.out.println(vat.selectedProperty().getValue());
    }
}
