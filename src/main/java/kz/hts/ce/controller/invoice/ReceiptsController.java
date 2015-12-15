package kz.hts.ce.controller.invoice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.controller.MainController;
import kz.hts.ce.controller.SettingsController;
import kz.hts.ce.model.dto.InvoiceDto;
import kz.hts.ce.model.entity.Invoice;
import kz.hts.ce.model.entity.ShopProvider;
import kz.hts.ce.service.InvoiceService;
import kz.hts.ce.service.ProviderService;
import kz.hts.ce.service.ShopProviderService;
import kz.hts.ce.util.spring.JsonUtil;
import kz.hts.ce.util.spring.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaUtil.countDays;
import static kz.hts.ce.util.JavaUtil.createInvoiceDtoFromInvoice;
import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class ReceiptsController implements Initializable {

    public static final String GREEN_COLOR = "greenColor";
    public static final String ORANGE_COLOR = "orangeColor";
    public static final String RED_COLOR = "redColor";
    public static final String ALL_PROVIDERS_RU = "Все поставщики";

    private ObservableList<InvoiceDto> invoiceData = FXCollections.observableArrayList();
    private List<ShopProvider> shopProviders;
    private long shopId;

    @FXML
    private TableView<InvoiceDto> receiptsTable;
    @FXML
    private TableColumn<InvoiceDto, Number> id;
    @FXML
    private TableColumn<InvoiceDto, String> providerCompanyName;
    @FXML
    private TableColumn<InvoiceDto, String> date;
    @FXML
    private TableColumn<InvoiceDto, Number> postponement;
    @FXML
    private TableColumn<InvoiceDto, Boolean> vat;
    @FXML
    private ComboBox<String> providers;

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ShopProviderService shopProviderService;
    @Autowired
    private ProviderService providerService;

    @Autowired
    private MainController mainController;
    @Autowired
    private SettingsController settingsController;
    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private SpringUtil springUtil;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shopId = springUtil.getEmployee().getShop().getId();
        shopProviders = shopProviderService.findByShopId(shopId);
        providers.getItems().add(ALL_PROVIDERS_RU);
        for (ShopProvider shopProvider : shopProviders) {
            providers.getItems().add(shopProvider.getProvider().getCompanyName());
        }

        initializeTableColumns();
        changeRowColor();

        if (receiptsTable != null) showEditReceiptPage();
    }

    private void initializeTableColumns() {
        id.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        providerCompanyName.setCellValueFactory(cellData -> cellData.getValue().providerCompanyNameProperty());
        date.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        postponement.setCellValueFactory(cellData -> cellData.getValue().postponementProperty());
        vat.setCellValueFactory(cellData -> cellData.getValue().vatProperty());
    }

    private void changeRowColor() {
        receiptsTable.setRowFactory(new Callback<TableView<InvoiceDto>, TableRow<InvoiceDto>>() {
            @Override
            public TableRow<InvoiceDto> call(TableView<InvoiceDto> tableView) {
                return new TableRow<InvoiceDto>() {
                    @Override
                    protected void updateItem(InvoiceDto invoiceDto, boolean empty) {
                        super.updateItem(invoiceDto, empty);
                        Integer invoiceMinInt = settingsController.getInvoiceMinInt();
                        Integer invoiceMaxInt = settingsController.getInvoiceMaxInt();
                        if (invoiceMaxInt == null && invoiceMinInt == null) {
                            settingsController.setInvoiceMinInt(jsonUtil.getInvoiceMinInt());
                            settingsController.setInvoiceMaxInt(jsonUtil.getInvoiceMaxInt());
                        }
                        if (!empty && invoiceMaxInt != null && invoiceMinInt != null) {
                            int diff = countDays(invoiceDto.getDate(), invoiceDto.getPostponement());
                            if (diff <= invoiceMinInt) {
                                getStyleClass().removeAll(Collections.singleton(GREEN_COLOR));
                                getStyleClass().removeAll(Collections.singleton(ORANGE_COLOR));
                                getStyleClass().add(RED_COLOR);
                            } else if (diff > invoiceMinInt && diff <= invoiceMaxInt) {
                                getStyleClass().removeAll(Collections.singleton(GREEN_COLOR));
                                getStyleClass().removeAll(Collections.singleton(RED_COLOR));
                                getStyleClass().add(ORANGE_COLOR);
                            } else if (diff > invoiceMaxInt) {
                                getStyleClass().removeAll(Collections.singleton(ORANGE_COLOR));
                                getStyleClass().removeAll(Collections.singleton(RED_COLOR));
                                getStyleClass().add(GREEN_COLOR);
                            }
                        } else {
                            getStyleClass().removeAll(Collections.singleton(ORANGE_COLOR));
                            getStyleClass().removeAll(Collections.singleton(RED_COLOR));
                            getStyleClass().removeAll(Collections.singleton(GREEN_COLOR));
                        }
                    }
                };
            }
        });
    }

    private void showEditReceiptPage() {
        receiptsTable.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                try {
                    springUtil.setId(receiptsTable.getSelectionModel().getSelectedItem().getId());
                    Node node = getPagesConfiguration().editReceipt();
                    mainController.getContentContainer().getChildren().setAll(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void showCreateReceiptPage() throws IOException {
        PagesConfiguration screens = getPagesConfiguration();
        mainController.getContentContainer().getChildren().setAll(screens.addReceipt());
    }

    public void findReceiptsByProvider(ActionEvent event) {
        ComboBox<String> source = (ComboBox<String>) event.getSource();
        String providerCompanyName = source.getValue();
        invoiceData.clear();
        receiptsTable.getItems().clear();
        List<Invoice> invoices;
        if (providerCompanyName.equals(ALL_PROVIDERS_RU)) {
            invoices = invoiceService.findByWarehouseShopId(shopId);
        } else {
            long providerId = providerService.findByCompanyName(providerCompanyName).getId();
            invoices = invoiceService.findByWarehouseShopIdAndProviderId(shopId, providerId);
        }
        for (Invoice invoice : invoices) {
            InvoiceDto invoiceDto = createInvoiceDtoFromInvoice(invoice, null);
            invoiceData.add(invoiceDto);
        }
        receiptsTable.setItems(invoiceData);
    }
}
