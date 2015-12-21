package kz.hts.ce.controller.invoice;

import com.sun.javafx.scene.control.skin.TableViewSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import kz.hts.ce.controller.ControllerException;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.*;
import kz.hts.ce.service.*;
import kz.hts.ce.util.spring.JsonUtil;
import kz.hts.ce.util.spring.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static kz.hts.ce.util.JavaUtil.createProductDtoFromProduct;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;

@Controller
public class AddReceiptController implements Initializable {

    public static final int ZERO = 0;
    public static final int ONE = 1;
    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();
    private ObservableList<ProductDto> productDtosByCategory = FXCollections.observableArrayList();
    private Set<String> barcodes;

    @FXML
    private TableView<ProductDto> productsTable;
    @FXML
    private TableColumn<ProductDto, String> barcodeColumn;
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
    private Spinner<Integer> margin;

    @Autowired
    private ShopProviderService shopProviderService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private WarehouseProductService warehouseProductService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private InvoiceProductService invoiceProductService;
    @Autowired
    private WarehouseProductHistoryService warehouseProductHistoryService;

    @Autowired
    private ReceiptPageController receiptPageController;

    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private SpringUtil springUtil;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productsData.clear();

        initializeTableColumns();

        postponement.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(ZERO, 1000, ZERO));
        amount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(ONE, 10000, ONE));
        margin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(ZERO, 100, ZERO));

        List<Unit> units = unitService.findAll();
        List<String> unitNames = units.stream().map(Unit::getName).collect(Collectors.toList());
        unitOfMeasure.getItems().addAll(unitNames);

        long shopId = springUtil.getEmployee().getShop().getId();
        List<ShopProvider> shopProviders = shopProviderService.findByShopId(shopId);
        List<String> providerNames = shopProviders.stream().map(shopProvider -> shopProvider.getProvider().getCompanyName()).collect(Collectors.toList());
        providers.getItems().addAll(providerNames);

        List<Category> categoriesFromDB = categoryService.findAll();
        List<String> categoryNames = categoriesFromDB.stream().map(Category::getName).collect(Collectors.toList());
        categories.getItems().addAll(categoryNames);

        productComboBoxListener();
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
        price.setText(String.valueOf(ZERO));

        ComboBox<String> source = (ComboBox<String>) event.getSource();
        List<Product> products = productService.findByCategoryName(source.getValue());

        for (Product product : products) {
            ProductDto productDto = createProductDtoFromProduct(product);
            productDtosByCategory.add(productDto);
        }
    }

    @FXML
    @Transactional
    private void saveInvoice() {
        try {
            String providerCompanyName = providers.getValue();
            LocalDate localDate = this.date.getValue();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            boolean vat = this.vat.isSelected();
            Integer postponement = this.postponement.getValue();
            String margin = this.margin.getEditor().getText();
            long shopId = springUtil.getEmployee().getShop().getId();
            Warehouse warehouse = warehouseService.findByShopId(shopId);

            Invoice invoice = new Invoice();
            invoice.setDate(date);
            invoice.setPostponement(postponement);
            invoice.setProvider(providerService.findByCompanyName(providerCompanyName));
            invoice.setVat(vat);
            invoice.setWarehouse(warehouse);
            invoice.setMargin(Integer.parseInt(margin));
            invoice.setVersion(ONE);
            Invoice savedInvoice = invoiceService.save(invoice);

            String marginPercentage = String.valueOf((Double.valueOf(margin) / 100) + ONE);
            BigDecimal priceWithMargin = new BigDecimal(marginPercentage);

            for (ProductDto productDto : productsData) {
                Product product = productService.findByBarcode(productDto.getBarcode());
                InvoiceProduct invoiceProduct = new InvoiceProduct();
                invoiceProduct.setInvoice(savedInvoice);
                invoiceProduct.setAmount(productDto.getAmount());
                if (jsonUtil.isVatBoolean() && !vat) {
                    priceWithMargin = (priceWithMargin.multiply(productDto.getPrice())).multiply(BigDecimal.valueOf(1.12));
                } else {
                    priceWithMargin = priceWithMargin.multiply(productDto.getPrice());
                }
                productDto.setMargin(Integer.parseInt(margin));
                productDto.setVat(vat);
                productDto.setFinalPrice(priceWithMargin);
//                invoiceProduct.setMargin(productDto.getMargin());
                invoiceProduct.setFinalPrice(productDto.getFinalPrice());
                invoiceProduct.setInitialPrice(productDto.getPrice());
                invoiceProduct.setVersion(ONE);

                WarehouseProduct warehouseProduct = new WarehouseProduct();
                warehouseProduct.setInitialPrice(productDto.getPrice());
                warehouseProduct.setVat(productDto.getVat());
                warehouseProduct.setMargin(productDto.getMargin());
                warehouseProduct.setFinalPrice(productDto.getFinalPrice());
                warehouseProduct.setWarehouse(warehouse);
//                warehouseProduct.setArrival(productDto.getAmount());
                warehouseProduct.setResidue(productDto.getResidue());
                warehouseProduct.setVersion(ONE);
//                warehouseProduct.setDate(date);
                if (product != null) {
                    warehouseProduct.setProduct(product);
                    invoiceProduct.setProduct(product);
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
                    invoiceProduct.setProduct(createdProduct);
                }
                WarehouseProduct warehouseProductFromDB = warehouseProductService.findByProductBarcode(warehouseProduct.getProduct().getBarcode());
                if (warehouseProductFromDB == null) {
                    WarehouseProduct savedWP = warehouseProductService.save(warehouseProduct);

                    WarehouseProductHistory wphCurrentVersion = new WarehouseProductHistory();
                    wphCurrentVersion.setWarehouseProduct(savedWP);
                    wphCurrentVersion.setDate(date);
                    wphCurrentVersion.setArrival(productDto.getAmount());
                    wphCurrentVersion.setResidue(warehouseProduct.getResidue());
                    wphCurrentVersion.setInitialPrice(warehouseProduct.getInitialPrice());
                    wphCurrentVersion.setFinalPrice(warehouseProduct.getFinalPrice());
                    wphCurrentVersion.setVersion(warehouseProduct.getVersion());
//                    wphCurrentVersion.setArrival(warehouseProduct.getArrival());
//                    wphCurrentVersion.setDate(warehouseProduct.getDate());
//                    wphCurrentVersion.setProvider(invoice.getProvider());
                    warehouseProductHistoryService.save(wphCurrentVersion);
                } else {
                    if (warehouseProductFromDB.getVersion() != ONE) {
                        WarehouseProductHistory wphPreviousVersion = new WarehouseProductHistory();
                        wphPreviousVersion.setWarehouseProduct(warehouseProductFromDB);
                        wphPreviousVersion.setDate(new Date());
                        wphPreviousVersion.setVersion(warehouseProductFromDB.getVersion());
                        wphPreviousVersion.setArrival(productDto.getAmount());
                        wphPreviousVersion.setResidue(warehouseProductFromDB.getResidue());
                        wphPreviousVersion.setInitialPrice(warehouseProductFromDB.getInitialPrice());
                        warehouseProductFromDB.setFinalPrice(warehouseProductFromDB.getFinalPrice());
//                        wphPreviousVersion.setArrival(warehouseProductFromDB.getArrival());
//                        wphPreviousVersion.setDate(warehouseProductFromDB.getDate());
//                        wphPreviousVersion.setProvider(invoice.getProvider());
                        warehouseProductHistoryService.save(wphPreviousVersion);
                    }

                    warehouseProductFromDB.setVersion(warehouseProductFromDB.getVersion() + ONE);
//                    warehouseProductFromDB.setArrival(warehouseProduct.getArrival());
                    warehouseProductFromDB.setResidue(warehouseProductFromDB.getResidue() + warehouseProduct.getResidue());
                    warehouseProductFromDB.setInitialPrice(warehouseProduct.getInitialPrice());
                    warehouseProductFromDB.setFinalPrice(warehouseProduct.getFinalPrice());
                    warehouseProductFromDB.setMargin(warehouseProduct.getMargin());
                    warehouseProductFromDB.setVat(warehouseProduct.isVat());
//                    warehouseProductFromDB.setDate(date);
                    warehouseProductService.save(warehouseProductFromDB);

                    WarehouseProductHistory wphCurrentVersion = new WarehouseProductHistory();
                    wphCurrentVersion.setWarehouseProduct(warehouseProductFromDB);
                    wphCurrentVersion.setDate(date);
                    wphCurrentVersion.setArrival(productDto.getAmount());
                    wphCurrentVersion.setResidue(warehouseProductFromDB.getResidue());
                    wphCurrentVersion.setInitialPrice(warehouseProductFromDB.getInitialPrice());
                    wphCurrentVersion.setFinalPrice(warehouseProductFromDB.getFinalPrice());
                    wphCurrentVersion.setVersion(warehouseProductFromDB.getVersion());
//                    wphCurrentVersion.setArrival(warehouseProduct.getArrival());
//                    wphCurrentVersion.setProvider(invoice.getProvider());
                    warehouseProductHistoryService.save(wphCurrentVersion);
                }
                invoiceProductService.save(invoiceProduct);
            }

            receiptPageController.showReceiptsPage();
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

        if (barcodes == null) barcodes = new HashSet<>();
        barcodes.clear();
        for (ProductDto dto : productsData) {
            barcodes.add(dto.getBarcode());
            if (barcode.equals(dto.getBarcode())) {
                dto.setAmount(dto.getAmount() + amount);
                dto.setPrice(price);
                productsTable.getProperties().put(TableViewSkin.RECREATE, Boolean.TRUE);
            }
        }

        if (!barcodes.contains(barcode)) {
            ProductDto productDto = new ProductDto();
            productDto.setBarcode(barcode);
            productDto.setCategoryName(categoryName);
            productDto.setName(productName);
            productDto.setPrice(price);
            productDto.setAmount(amount);
            productDto.setResidue(amount);
            productDto.setUnitName(unit);
            productsData.add(productDto);
            productsTable.setItems(productsData);
        }

        deleteRowColumn.setDisable(false);
        productComboBox.setValue("");
        this.barcode.setText("");
        this.unitOfMeasure.getEditor().setText("");
        this.price.setText(String.valueOf(ZERO));
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

    @FXML
    private void enableAllFields() {
        margin.setDisable(false);
        date.setDisable(false);
        postponement.setDisable(false);
        vat.setDisable(false);
        categories.setDisable(false);
    }

    public void productComboBoxListener() {
        productComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            for (ProductDto productDto : productDtosByCategory) {
                String name = productDto.getName();
                if (name.toLowerCase().contains(newValue.toLowerCase())) {
                    if (name.toLowerCase().equals(newValue.toLowerCase())) {
                        productComboBox.getEditor().setText(name);
                        barcode.setText(String.valueOf(Long.valueOf(productDto.getBarcode())));
                        String unitName = productDto.getUnitName();
                        unitOfMeasure.getEditor().setText(unitName);
                        barcode.setDisable(true);
                        unitOfMeasure.setDisable(true);
                    } else if (newValue.equals("")) {
                        clearData();
                        barcode.setDisable(false);
                        unitOfMeasure.setDisable(false);
                    } else {
                        ObservableList<String> items = productComboBox.getItems();
                        barcode.setDisable(false);
                        unitOfMeasure.setDisable(false);
                        if (!items.contains(name)) {
                            items.add(name);
                        }

                        items.stream().filter(item -> !item.toLowerCase().contains(newValue.toLowerCase()))
                                .forEach(item -> productComboBox.getItems().remove(item));
                    }
                } else {
                    barcode.setDisable(false);
                    unitOfMeasure.setDisable(false);
                    productDtosByCategory.stream().filter(dto -> dto.getName().toLowerCase().contains(newValue.toLowerCase())).forEach(dto -> {
                        barcode.setDisable(true);
                        unitOfMeasure.setDisable(true);
                    });
                    productDtosByCategory.stream().filter(dto -> name.toLowerCase().equals(dto.getName().toLowerCase()))
                            .forEach(dto -> productComboBox.getItems().remove(name));
                }
            }
            productComboBox.show();
        });
    }

    public Spinner<Integer> getPostponement() {
        return postponement;
    }

    public CheckBox getVat() {
        return vat;
    }

    public DatePicker getDate() {
        return date;
    }
}
