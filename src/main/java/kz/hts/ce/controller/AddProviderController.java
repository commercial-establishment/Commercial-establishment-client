package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.ShopProvider;
import kz.hts.ce.service.ShopProviderService;
import kz.hts.ce.util.spring.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class AddProviderController implements Initializable {

    @FXML
    private TableView<ProductDto> providerTable;
    @FXML
    private ComboBox<String> providers;
    @FXML
    private TableColumn<ProductDto, String> companyName;
    @FXML
    private TableColumn<ProductDto, String> contactPerson;
    @FXML
    private TableColumn<ProductDto, String> email;
    @FXML
    private TableColumn<ProductDto, String> city;
    @FXML
    private TableColumn<ProductDto, String> address;
    @FXML
    private TableColumn<ProductDto, Number> iin;
    @FXML
    private TableColumn<ProductDto, Number> bin;

    @Autowired
    private ShopProviderService shopProviderService;
    @Autowired
    private SpringUtil springUtil;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<ShopProvider> shopProviders = shopProviderService.findByShopId(springUtil.getEmployee().getShop().getId());


    }

    @FXML
    private void addProvider(ActionEvent event) {

    }
}
