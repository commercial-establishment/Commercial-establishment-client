package kz.hts.ce.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.model.dto.InvoiceDto;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.Employee;
import kz.hts.ce.model.entity.Invoice;
import kz.hts.ce.model.entity.InvoiceWarehouseProduct;
import kz.hts.ce.service.EmployeeService;
import kz.hts.ce.service.InvoiceWarehouseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static kz.hts.ce.util.JavaUtil.createInvoiceDtoFromInvoice;
import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;
import static kz.hts.ce.util.SpringUtil.getPrincipal;

@Controller
public class ReceiptsController implements Initializable {

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
    private InvoiceWarehouseProductService invoiceWarehouseProductService;
    @Autowired
    private EmployeeService employeeService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceData.clear();

        Employee employee = employeeService.findByUsername(getPrincipal());
        List<InvoiceWarehouseProduct> invoicesWarehouseProducts = invoiceWarehouseProductService.
                findByWarehouseProductWarehouseShopId(employee.getShop().getId());

        Set<Invoice> invoices = invoicesWarehouseProducts.stream().map(InvoiceWarehouseProduct::getInvoice).collect(Collectors.toSet());

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
    }

    @FXML
    public void createReceipt() throws IOException {
        PagesConfiguration screens = getPagesConfiguration();
        mainController.getContentContainer().getChildren().setAll(screens.addReceipt());
    }
}
