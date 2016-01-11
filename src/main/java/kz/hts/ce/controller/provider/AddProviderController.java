package kz.hts.ce.controller.provider;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.controller.MainController;
import kz.hts.ce.model.dto.ProviderDto;
import kz.hts.ce.model.entity.Provider;
import kz.hts.ce.model.entity.Shop;
import kz.hts.ce.model.entity.ShopProvider;
import kz.hts.ce.service.ProviderService;
import kz.hts.ce.service.ShopProviderService;
import kz.hts.ce.util.spring.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static kz.hts.ce.util.JavaUtil.createProviderDtoFromProvider;
import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class AddProviderController implements Initializable {

    @FXML
    private Button add;
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
    private TableColumn<ProviderDto, String> iin;
    @FXML
    private TableColumn<ProviderDto, String> bin;

    @Autowired
    private ShopProviderService shopProviderService;
    @Autowired
    private ProviderService providerService;

    @Autowired
    private MainController mainController;

    @Autowired
    private SpringUtil springUtil;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<ShopProvider> shopProviders = shopProviderService.findByShopId(springUtil.getEmployee().getShop().getId());

        for (ShopProvider shopProvider : shopProviders) {
            ProviderDto providerDto = createProviderDtoFromProvider(shopProvider.getProvider());
            providerTable.getItems().add(providerDto);
        }

        addProviderCompanyNames();
        initializeTableColumns();
    }

    private void addProviderCompanyNames() {
        List<String> providerCompanyNames = new ArrayList<>();
        ObservableList<ProviderDto> providerDtosInTable = providerTable.getItems();
        providerDtosInTable.stream().filter(providerDto -> !providerCompanyNames.contains(providerDto.getCompanyName()))
                .forEach(providerDto -> providerCompanyNames.add(providerDto.getCompanyName()));

        List<Provider> providers = providerService.findAll();
        this.providers.getItems().clear();
        ObservableList<String> providerNames = this.providers.getItems();
        providerNames.addAll(providers.stream().filter(provider -> !providerCompanyNames.
                contains(provider.getCompanyName())).map(Provider::getCompanyName).collect(Collectors.toList()));
        if (providerNames.size() == 0) {
            this.providers.setDisable(true);
            this.add.setDisable(true);
        }
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

    @FXML
    private void unlockAddProviderButton() {
        if (add.isDisable()) add.setDisable(false);
    }

    public void addProviderToTable() {
        String companyName = providers.getValue();
        Provider provider = providerService.findByCompanyName(companyName);
        ProviderDto providerDto = createProviderDtoFromProvider(provider);
        providerTable.getItems().add(providerDto);

        addProviderCompanyNames();
    }

    @FXML
    private void save() {
        ObservableList<ProviderDto> providers = providerTable.getItems();

        Shop shop = springUtil.getEmployee().getShop();

        List<ShopProvider> shopProviders = shopProviderService.findByShopId(shop.getId());
        List<String> companyNames = shopProviders.stream().map(shopProvider -> shopProvider.getProvider().getCompanyName()).collect(Collectors.toList());
        providers.stream().filter(provider -> !companyNames.contains(provider.getCompanyName())).forEach(provider -> {
            ShopProvider shopProvider = new ShopProvider();
            shopProvider.setShop(shop);
            Provider providerFromDB = providerService.findByCompanyName(provider.getCompanyName());
            shopProvider.setProvider(providerFromDB);
            shopProvider.setBlocked(false);
            shopProviderService.save(shopProvider);
        });
        /*TODO success alert*/
    }

    @FXML
    private void createProviderPage() {
        PagesConfiguration screens = getPagesConfiguration();
        mainController.getContentContainer().getChildren().setAll(screens.createProvider());
    }
}
