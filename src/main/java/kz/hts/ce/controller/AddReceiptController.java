package kz.hts.ce.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.model.entity.Invoice;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.*;
import kz.hts.ce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static kz.hts.ce.util.JavaFxUtil.alert;
import static kz.hts.ce.util.JavaUtil.createProductDtoFromProduct;
import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;
import static kz.hts.ce.util.SpringUtil.getPrincipal;

@Controller
public class AddReceiptController implements Initializable {

    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();
    private ObservableList<ProductDto> productDtosByCategory = FXCollections.observableArrayList();

    @FXML
    private TableView<ProductDto> productsTable;
    @FXML
    private TableColumn<ProductDto, Number> barcodeColumn;
    @FXML
    private TableColumn<ProductDto, String> nameColumn;
    @FXML
    private TableColumn<ProductDto, String> categoryNameColumn;
    @FXML
    private TableColumn<ProductDto, BigDecimal> priceColumn;
    @FXML
    private TableColumn<ProductDto, Number> amountColumn;
    @FXML
    private TableColumn<ProductDto, String> unitOfMeasureColumn;
    @FXML
    private TableColumn<ProductDto, BigDecimal> totalPriceColumn;

    @FXML
    private Button deleteRowColumn;
    @FXML
    private Button add;
    @FXML
    private ComboBox<String> productComboBox;
    @FXML
    private TextField barcode;
    @FXML
    private Spinner<Integer> amount;
    @FXML
    private TextField price;
    @FXML
    private ComboBox<String> unitOfMeasure;
    @FXML
    private ComboBox<String> categories;
    @FXML
    private Spinner<Integer> postponement;
    @FXML
    private CheckBox vat;
    @FXML
    private DatePicker date;
    @FXML
    private ComboBox<String> providers;

    @FXML
    private VBox vBox;

    @Autowired
    private ShopProviderService shopProviderService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoiceWarehouseProductService invoiceWarehouseProductService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private WarehouseProductService warehouseProductService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private MainController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productsData.clear();

        initializeTableColumns();

        postponement.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0));
        amount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 1));

        List<Unit> units = unitService.findAll();
        List<String> unitNames = units.stream().map(Unit::getName).collect(Collectors.toList());
        unitOfMeasure.getItems().addAll(unitNames);

        long shopId = employeeService.findByUsername(getPrincipal()).getShop().getId();
        List<ShopProvider> shopProviders = shopProviderService.findByShopId(shopId);
        List<String> providerNames = shopProviders.stream().map(shopProvider -> shopProvider.getProvider().getCompanyName()).collect(Collectors.toList());
        providers.getItems().addAll(providerNames);

        List<Category> categoriesFromDB = categoryService.findAll();
        List<String> categoryNames = categoriesFromDB.stream().map(Category::getName).collect(Collectors.toList());
        categories.getItems().addAll(categoryNames);

        productComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            for (ProductDto productDto : productDtosByCategory) {
                String name = productDto.getName();
                if (name.toLowerCase().contains(newValue) || name.equals(newValue)) {
                    if (name.equals(newValue)) {
                        productComboBox.getEditor().setText(name);
                        barcode.setText(String.valueOf(Long.valueOf(productDto.getBarcode())));
                        String unitName = productDto.getUnitName();
                        unitOfMeasure.getEditor().setText(unitName);
                        unitOfMeasure.setDisable(true);
                    } else if (newValue.equals("")) {
                        clearData();
                        unitOfMeasure.setDisable(false);
                    } else {
                        ObservableList<String> items = productComboBox.getItems();
                        unitOfMeasure.setDisable(false);
                        if (!items.contains(name)) {
                            items.add(name);
                        }
                        items.stream().filter(item -> !item.contains(newValue)).forEach(item -> productComboBox.getItems().remove(item));
                    }
                }
            }
            productComboBox.show();
        });
    }

    public void findProductsByCategory(ActionEvent event) {
        productDtosByCategory.clear();
        clearData();

        productComboBox.setDisable(false);
        barcode.setDisable(false);
        unitOfMeasure.setDisable(false);
        price.setDisable(false);
        amount.setDisable(false);
        add.setDisable(false);
        price.setText("0");

        ComboBox<String> source = (ComboBox<String>) event.getSource();
        List<Product> products = productService.findByCategoryName(source.getValue());

        for (Product product : products) {
            ProductDto productDto = createProductDtoFromProduct(product);
            productDtosByCategory.add(productDto);
        }
    }

    @FXML
    @Transactional
    private void saveInvoice(ActionEvent event) {
        try {
            String providerCompanyName = providers.getValue();
            LocalDate localDate = this.date.getValue();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            boolean vat = this.vat.isSelected();
            Integer postponement = this.postponement.getValue();

            Invoice invoiceEntity = new Invoice();
            invoiceEntity.setDate(date);
            invoiceEntity.setPostponement(postponement);
            invoiceEntity.setProvider(providerService.findByCompanyName(providerCompanyName));
            invoiceEntity.setVat(vat);
            Invoice invoice = invoiceService.save(invoiceEntity);

            for (ProductDto productDto : productsData) {
                Product product = productService.findByBarcode(productDto.getBarcode());
                WarehouseProduct warehouseProduct = new WarehouseProduct();
                warehouseProduct.setArrival(productDto.getAmount());
                warehouseProduct.setInitialPrice(productDto.getPrice());

                //TODO price with/without VAT?!?!?!
                warehouseProduct.setPrice(productDto.getPrice());

                warehouseProduct.setResidue(productDto.getAmount());
                if (product != null) {
                    warehouseProduct.setProduct(product);
                } else {
                    Product newProduct = new Product();
                    newProduct.setBarcode(productDto.getBarcode());
                    newProduct.setBlocked(false);
                    Category category = categoryService.findByName(productDto.getCategoryName());
                    newProduct.setCategory(category);
                    String productName = productDto.getName();
                    newProduct.setName(productName);
                    Unit unit = unitService.findByName(productDto.getUnitName());
                    newProduct.setUnit(unit);

                    Product createdProduct = productService.save(newProduct);
                    warehouseProduct.setProduct(createdProduct);
                }
                long shopId = employeeService.findByUsername(getPrincipal()).getShop().getId();
                warehouseProduct.setWarehouse(warehouseService.findByShopId(shopId));
                warehouseProduct.setVersion(1);
                warehouseProductService.save(warehouseProduct);

                InvoiceWarehouseProduct invoiceWarehouseProduct = new InvoiceWarehouseProduct();
                invoiceWarehouseProduct.setInvoice(invoice);
                invoiceWarehouseProduct.setWarehouseProduct(warehouseProduct);
                invoiceWarehouseProductService.save(invoiceWarehouseProduct);

                showReceiptsPage();
            }
        } catch (ControllerException e) {
            alert(Alert.AlertType.ERROR, "Внутренная ошибка", null, "Пожалуйста, проверьте корректность введённых данных");
        }
    }

    @FXML
    private void addProductToTable() {
        String categoryName = categories.getValue();
        String productName = productComboBox.getEditor().getText();
        String unit = unitOfMeasure.getEditor().getText();
        BigDecimal price = new BigDecimal(this.price.getText());
        Integer amount = this.amount.getValue();
        String barcode = this.barcode.getText();

        ProductDto productDto = new ProductDto();
        productDto.setBarcode(Long.parseLong(barcode));
        productDto.setCategoryName(categoryName);
        productDto.setName(productName);
        productDto.setPrice(price);
        productDto.setAmount(amount);
        productDto.setUnitName(unit);
        productsData.add(productDto);

        productsTable.setItems(productsData);
        deleteRowColumn.setDisable(false);

        productComboBox.setValue("");
        this.barcode.setText("");
        this.unitOfMeasure.getEditor().setText("");
        this.price.setText("0");
    }

    @FXML
    private void enableAllFields() {
        date.setDisable(false);
        postponement.setDisable(false);
        vat.setDisable(false);
        categories.setDisable(false);
    }

    @FXML
    private void deleteProductFromTable() {
        ProductDto productDto = productsTable.getSelectionModel().getSelectedItem();
        if (productDto == null) {
            alert(Alert.AlertType.WARNING, "Товар не выбран", null, "Пожалуйста, выберите товар, который хотите удалить.");
        } else {
            productsData.remove(productDto);
            productsTable.getItems().remove(productDto);
        }
    }

    public void initializeTableColumns() {
        barcodeColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        categoryNameColumn.setCellValueFactory(cellData -> cellData.getValue().categoryNameProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        unitOfMeasureColumn.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty());
        totalPriceColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty());
    }

    public void clearData() {
        productComboBox.getItems().clear();
        productComboBox.getEditor().setText("");
        barcode.setText("");
        unitOfMeasure.getEditor().setText("");
    }

    public void showReceiptsPage() {
        PagesConfiguration screens = getPagesConfiguration();
        try {
            mainController.getContentContainer().getChildren().setAll(screens.receipts());
        } catch (IOException e) {
            throw new ControllerException(e);
        }
    }
}
