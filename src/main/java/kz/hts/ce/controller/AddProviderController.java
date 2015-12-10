package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import kz.hts.ce.model.dto.ProviderDto;
import kz.hts.ce.model.entity.Provider;
import kz.hts.ce.model.entity.ShopProvider;
import kz.hts.ce.service.ProviderService;
import kz.hts.ce.service.ShopProviderService;
import kz.hts.ce.util.spring.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaUtil.createProviderDtoFromProvider;

@Controller
public class AddProviderController implements Initializable {

    @FXML
    private ComboBox<String> providers;
    @FXML
    private TableView<ProviderDto> providerTable;
    @FXML
    private TableColumn<ProviderDto, String> companyName;
    @FXML
    private TableColumn<ProviderDto, String> contactPerson;
    @FXML
    private TableColumn<ProviderDto, String> email;
    @FXML
    private TableColumn<ProviderDto, String> city;
    @FXML
    private TableColumn<ProviderDto, String> address;
    @FXML
    private TableColumn<ProviderDto, Number> iin;
    @FXML
    private TableColumn<ProviderDto, Number> bin;

    @Autowired
    private ShopProviderService shopProviderService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private SpringUtil springUtil;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Provider> providers = providerService.findAll();
        for (Provider provider : providers) {
            this.providers.getItems().add(provider.getCompanyName());
        }

        List<ShopProvider> shopProviders = shopProviderService.findByShopId(springUtil.getEmployee().getShop().getId());

        initializeTableColumns();

        for (ShopProvider shopProvider : shopProviders) {
            ProviderDto providerDto = createProviderDtoFromProvider(shopProvider.getProvider());
            providerTable.getItems().add(providerDto);
        }
    }

    @FXML
    private void addProvider(ActionEvent event) {

    }

    private void initializeTableColumns() {
        companyName.setCellValueFactory(cellData -> cellData.getValue().companyNameProperty());
        contactPerson.setCellValueFactory(cellData -> cellData.getValue().contactPersonProperty());
        email.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        city.setCellValueFactory(cellData -> cellData.getValue().cityNameProperty());
        address.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        iin.setCellValueFactory(cellData -> cellData.getValue().iinProperty());
        bin.setCellValueFactory(cellData -> cellData.getValue().binProperty());
    }
}
