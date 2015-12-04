package kz.hts.ce.controller.invoice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.controller.MainController;
import kz.hts.ce.controller.SettingsController;
import kz.hts.ce.model.dto.InvoiceDto;
import kz.hts.ce.model.entity.Employee;
import kz.hts.ce.model.entity.Invoice;
import kz.hts.ce.service.EmployeeService;
import kz.hts.ce.service.InvoiceService;
import kz.hts.ce.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import static kz.hts.ce.util.JavaUtil.countDays;
import static kz.hts.ce.util.JavaUtil.createInvoiceDtoFromInvoice;
import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;
import static kz.hts.ce.util.SpringUtil.getPrincipal;

@Controller
public class ReceiptsController implements Initializable {

    public static final String GREEN_COLOR = "greenColor";
    public static final String ORANGE_COLOR = "orangeColor";
    public static final String RED_COLOR = "redColor";

    private ObservableList<InvoiceDto> invoiceData = FXCollections.observableArrayList();

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

    @Autowired
    private MainController mainController;
    @Autowired
    private SettingsController settingsController;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private JsonUtil jsonUtil;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceData.clear();

        Employee employee = employeeService.findByUsername(getPrincipal());
        List<Invoice> invoicesFromDB = invoiceService.findByWarehouseShopId(employee.getShop().getId());

        Set<Invoice> invoices = invoicesFromDB.stream().collect(Collectors.toSet());

        id.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        providerCompanyName.setCellValueFactory(cellData -> cellData.getValue().providerCompanyNameProperty());
        date.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        postponement.setCellValueFactory(cellData -> cellData.getValue().postponementProperty());
        vat.setCellValueFactory(cellData -> cellData.getValue().vatProperty());

        for (Invoice invoice : invoices) {
            InvoiceDto invoiceDto = createInvoiceDtoFromInvoice(invoice, null);
            invoiceData.add(invoiceDto);
        }

        receiptsTable.getItems().addAll(invoiceData);

        receiptsTable.setRowFactory(new Callback<TableView<InvoiceDto>, TableRow<InvoiceDto>>() {
            @Override
            public TableRow<InvoiceDto> call(TableView<InvoiceDto> tableView) {
                return new TableRow<InvoiceDto>() {
                    @Override
                    protected void updateItem(InvoiceDto invoiceDto, boolean empty) {
                        super.updateItem(invoiceDto, empty);
                        if (settingsController.getProductMaxInt() == null && settingsController.getProductMinInt() == null) {
                            settingsController.setInvoiceMinInt(jsonUtil.getInvoiceMinInt());
                            settingsController.setInvoiceMaxInt(jsonUtil.getInvoiceMaxInt());
                        }
                        if (!empty && settingsController.getInvoiceMaxInt() != null && settingsController.getInvoiceMinInt() != null) {
                            int diff = countDays(invoiceDto.getDate(), invoiceDto.getPostponement());
                            if (diff <= settingsController.getInvoiceMinInt()) {
                                getStyleClass().removeAll(Collections.singleton(GREEN_COLOR));
                                getStyleClass().removeAll(Collections.singleton(ORANGE_COLOR));
                                getStyleClass().add(RED_COLOR);
                            } else if (diff > settingsController.getInvoiceMinInt() && diff <= settingsController.getInvoiceMinInt()) {
                                getStyleClass().removeAll(Collections.singleton(GREEN_COLOR));
                                getStyleClass().removeAll(Collections.singleton(RED_COLOR));
                                getStyleClass().add(ORANGE_COLOR);
                            } else if (diff > settingsController.getInvoiceMaxInt()) {
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

    @FXML
    public void createReceipt() throws IOException {
        PagesConfiguration screens = getPagesConfiguration();
        mainController.getContentContainer().getChildren().setAll(screens.addReceipt());
    }
}
