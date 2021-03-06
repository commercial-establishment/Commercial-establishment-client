package kz.hts.ce.controller.receipt;

import com.sun.javafx.scene.control.skin.TableViewSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.controller.ControllerException;
import kz.hts.ce.controller.MainController;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.*;
import kz.hts.ce.service.*;
import kz.hts.ce.util.javafx.EditingBigDecimalCell;
import kz.hts.ce.util.javafx.EditingNumberCell;
import kz.hts.ce.util.javafx.fields.IntegerTextField;
import kz.hts.ce.util.spring.JsonUtil;
import kz.hts.ce.util.spring.SpringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static kz.hts.ce.util.JavaUtil.createProductDtoFromProduct;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;
import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class ReceiptController implements Initializable {

    private static final double VAT = 1.12;
    private static final int ZERO = 0;
    private static final int ONE = 1;

    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();
    private ObservableList<ProductDto> productDtosByCategory = FXCollections.observableArrayList();
    private Set<String> barcodes;
    private Invoice invoiceFromDB;
    private List<InvoiceProduct> oldInvoiceProducts;
    private List<ProductDto> removedProducts;

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
    private Spinner<Integer> margin;
    @FXML
    private Button deleteRowColumn;
    @FXML
    private Button add;
    @FXML
    private ComboBox<String> productComboBox;
    @FXML
    private TextField barcode;
    @FXML
    private TextField price;
    @FXML
    private IntegerTextField amount;
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
    @FXML
    private AnchorPane editPane;

    @Autowired
    private ProductService productService;
    @Autowired
    private ShopProviderService shopProviderService;
    @Autowired
    private CategoryService categoryService;
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
    private InvoiceHistoryService invoiceHistoryService;
    @Autowired
    private WarehouseProductHistoryService wphService;
    @Autowired
    private InvoiceProductHistoryService iphService;
    @Autowired
    private WarehouseProductHistoryService warehouseProductHistoryService;
    @Autowired
    private ProductProviderService productProviderService;

    @Autowired
    private MainController mainController;

    @Autowired
    private SpringHelper springHelper;
    @Autowired
    private JsonUtil jsonUtil;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UUID shopId = springHelper.getEmployee().getShop().getId();
        List<Unit> units = unitService.findAll();
        List<String> unitNames = units.stream().map((unit) -> unit.getName()).collect(Collectors.toList());
        unitOfMeasure.getItems().addAll(unitNames);
        List<ShopProvider> shopProviders = shopProviderService.findByShopId(shopId);
        List<String> providerNames = shopProviders.stream().map(shopProvider -> shopProvider.getProvider().getCompanyName()).collect(Collectors.toList());
        providers.getItems().addAll(providerNames);
//        amount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(ONE, 10000, ONE));
        List<Category> categoriesFromDB = categoryService.findAll();
        List<String> categoryNames = categoriesFromDB.stream().map(Category::getName).collect(Collectors.toList());
        categories.getItems().addAll(categoryNames);
        productsData.clear();
        productComboBoxListener();
        initializeTableColumns();

        amountColumn.setCellFactory(p -> new EditingNumberCell());
        amountColumn.setOnEditCommit(t -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setAmount((Integer) t.getNewValue());
            productsTable.getProperties().put(TableViewSkin.RECREATE, Boolean.TRUE);
        });
        priceColumn.setCellFactory(p -> new EditingBigDecimalCell());
        priceColumn.setOnEditCommit(t -> {
            t.getTableView().getItems().get(t.getTablePosition().getRow()).setPrice(t.getNewValue());
            productsTable.getProperties().put(TableViewSkin.RECREATE, Boolean.TRUE);
        });
        if (springHelper.isNewInvoice()) {
            postponement.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(ZERO, 1000, ZERO));
            margin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(ZERO, 100, ZERO));
        } else {
            providers.setDisable(true);
            invoiceFromDB = invoiceService.findById(UUID.fromString(springHelper.getId()));

            barcodes = new HashSet<>();
            List<Product> products = productService.findAll();
            barcodes.addAll(products.stream().map(Product::getBarcode).collect(Collectors.toList()));

            providers.setValue(invoiceFromDB.getProvider().getCompanyName());

            Date input = invoiceFromDB.getDate();
            LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            this.date.setValue(date);

            postponement.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(ZERO, 1000, invoiceFromDB.getPostponement()));
            margin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(ONE, 10000, invoiceFromDB.getMargin()));

            vat.selectedProperty().setValue(invoiceFromDB.isVat());

            oldInvoiceProducts = invoiceProductService.findByInvoiceId(UUID.fromString(springHelper.getId()));

            for (InvoiceProduct invoiceProduct : oldInvoiceProducts) {
                ProductDto productDto = createProductDtoFromProduct(invoiceProduct.getProduct());
                productDto.setPrice(invoiceProduct.getInitialPrice());
                productDto.setAmount(invoiceProduct.getAmount());
                productDto.setId(invoiceProduct.getId());
                productsData.add(productDto);
                productsTable.getItems().add(productDto);
            }
        }
    }

    @FXML
    private void findProductsByCategory(ActionEvent event) {
        productDtosByCategory.clear();
        clearData();

        productComboBox.setDisable(false);
        barcode.setDisable(false);
        unitOfMeasure.setDisable(false);
        price.setDisable(false);
        amount.setDisable(false);
        add.setDisable(false);

        ComboBox<String> source = (ComboBox<String>) event.getSource();
        List<Product> products = productService.findByCategoryName(source.getValue());

        for (Product product : products) {
            ProductDto productDto = createProductDtoFromProduct(product);
            productDtosByCategory.add(productDto);
        }
    }

    private void clearData() {
        productComboBox.getItems().clear();
        productComboBox.getEditor().setText("");
        barcode.setText("");
        unitOfMeasure.getEditor().setText("");
    }

    private void initializeTableColumns() {
        barcodeColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        categoryNameColumn.setCellValueFactory(cellData -> cellData.getValue().categoryNameProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        unitOfMeasureColumn.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty());
        totalPriceColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty());
    }

    @FXML
    private void enableAllFields() {
        margin.setDisable(false);
        date.setDisable(false);
        postponement.setDisable(false);
        vat.setDisable(false);
        categories.setDisable(false);
    }

    private void productComboBoxListener() {
        productComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            for (ProductDto productDto : productDtosByCategory) {
                String name = productDto.getName();
                if (name.toLowerCase().contains(newValue.toLowerCase())) {
                    if (name.toLowerCase().equals(newValue.toLowerCase())) {
                        productComboBox.getEditor().setText(name);
                        barcode.setText(productDto.getBarcode());
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
                    productDtosByCategory.stream().filter(dto -> dto.getName().toLowerCase().contains(newValue.toLowerCase()))
                            .forEach(dto -> {
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

    private void setAmountAndPriceToProductDto(String barcode, Integer amount, BigDecimal price) {
        for (ProductDto dto : productsData) {
            barcodes.add(dto.getBarcode());
            if (barcode.equals(dto.getBarcode())) {
                dto.setAmount(dto.getAmount() + amount);
                dto.setPrice(price);
                productsTable.getProperties().put(TableViewSkin.RECREATE, Boolean.TRUE);
            }
        }
    }

    @FXML
    @Transactional
    private void save() {
        LocalDate localDate = this.date.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Integer postponement = this.postponement.getValue();
        boolean vat = this.vat.isSelected();
        UUID shopId = springHelper.getEmployee().getShop().getId();
        Warehouse warehouse = warehouseService.findByShopId(shopId);
        if (springHelper.isNewInvoice()) {
            try {
                String providerCompanyName = providers.getValue();

                String margin = this.margin.getEditor().getText();

                Invoice invoice = new Invoice();
                invoice.setDate(date);
                invoice.setPostponement(postponement);
                invoice.setProvider(providerService.findByCompanyName(providerCompanyName));
                invoice.setVat(vat);
                invoice.setWarehouse(warehouse);
                invoice.setMargin(Integer.parseInt(margin));
                invoice.setVersion(ONE);
                Invoice savedInvoice = invoiceService.save(invoice);

                InvoiceHistory invoiceHistory = new InvoiceHistory();
                invoiceHistory.setInvoice(invoice);
                invoiceHistory.setDate(invoice.getDate());
                invoiceHistory.setMargin(invoice.getMargin());
                invoiceHistory.setVat(invoice.isVat());
                invoiceHistory.setPostponement(invoice.getPostponement());
                invoiceHistory.setVersion(invoice.getVersion());
                invoiceHistoryService.save(invoiceHistory);

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
                    invoiceProduct.setFinalPrice(productDto.getFinalPrice());
                    invoiceProduct.setInitialPrice(productDto.getPrice());
                    invoiceProduct.setVersion(ONE);

                    WarehouseProduct warehouseProduct = new WarehouseProduct();
                    warehouseProduct.setInitialPrice(productDto.getPrice());
                    warehouseProduct.setVat(productDto.getVat());
                    warehouseProduct.setMargin(productDto.getMargin());
                    warehouseProduct.setFinalPrice(productDto.getFinalPrice());
                    warehouseProduct.setWarehouse(warehouse);
                    warehouseProduct.setResidue(productDto.getResidue());
                    warehouseProduct.setVersion(ONE);

                    setProductToInvoiceProduct(product, productDto, warehouseProduct, invoiceProduct);
                    setProviderAndProduct(invoice.getProvider(), warehouseProduct.getProduct());


                    WarehouseProduct warehouseProductFromDB = warehouseProductService.findByProductBarcode(warehouseProduct
                            .getProduct().getBarcode());
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
                        warehouseProductHistoryService.save(wphCurrentVersion);
                    } else {
                        warehouseProductFromDB.setVersion(warehouseProductFromDB.getVersion() + ONE);
                        warehouseProductFromDB.setResidue(warehouseProductFromDB.getResidue() + warehouseProduct.getResidue());
                        warehouseProductFromDB.setInitialPrice(warehouseProduct.getInitialPrice());
                        warehouseProductFromDB.setFinalPrice(warehouseProduct.getFinalPrice());
                        warehouseProductFromDB.setMargin(warehouseProduct.getMargin());
                        warehouseProductFromDB.setVat(warehouseProduct.isVat());
                        warehouseProductService.save(warehouseProductFromDB);

                        WarehouseProductHistory wphCurrentVersion = new WarehouseProductHistory();
                        wphCurrentVersion.setWarehouseProduct(warehouseProductFromDB);
                        wphCurrentVersion.setDate(date);
                        wphCurrentVersion.setArrival(productDto.getAmount());
                        wphCurrentVersion.setResidue(warehouseProductFromDB.getResidue());
                        wphCurrentVersion.setInitialPrice(warehouseProductFromDB.getInitialPrice());
                        wphCurrentVersion.setFinalPrice(warehouseProductFromDB.getFinalPrice());
                        wphCurrentVersion.setVersion(warehouseProductFromDB.getVersion());
                        warehouseProductHistoryService.save(wphCurrentVersion);
                    }
                    invoiceProductService.save(invoiceProduct);
                }

                showReceiptsPage();
            } catch (ControllerException e) {
                alert(Alert.AlertType.ERROR, "Внутренная ошибка", null, "Пожалуйста, проверьте корректность введённых данных");
            }
        } else {
            try {
                if (productsData.size() != ZERO) {
                    String margin = String.valueOf(this.margin.getValue());

                    invoiceFromDB.setMargin(Integer.parseInt(margin));
                    invoiceFromDB.setPostponement(postponement);
                    invoiceFromDB.setVat(vat);
                    invoiceFromDB.setVersion(invoiceFromDB.getVersion() + ONE);

                    Invoice invoice = invoiceService.save(invoiceFromDB);
                    InvoiceHistory invoiceHistory = new InvoiceHistory();
                    invoiceHistory.setInvoice(invoice);
                    invoiceHistory.setMargin(invoice.getMargin());
                    invoiceHistory.setPostponement(invoice.getPostponement());
                    invoiceHistory.setVat(invoice.isVat());
                    invoiceHistory.setDate(new Date());
                    invoiceHistory.setVersion(invoiceFromDB.getVersion());
                    invoiceHistoryService.save(invoiceHistory);

                    String marginPercentage = String.valueOf((Double.valueOf(margin) / 100) + ONE);
                    for (ProductDto productDto : productsData) {
                        String id = productDto.getId();
                        for (InvoiceProduct oldInvoiceProduct : oldInvoiceProducts) {
                            if (id != null) {
                                if (UUID.fromString(id).equals(oldInvoiceProduct.getId())) {
                                    oldInvoiceProduct.setInitialPrice(productDto.getPrice());
                                    BigDecimal priceWithMargin = new BigDecimal(marginPercentage);
                                    if (jsonUtil.isVatBoolean() && !vat) {
                                        priceWithMargin = (priceWithMargin.multiply(productDto.getPrice()))
                                                .multiply(BigDecimal.valueOf(VAT));
                                    } else {
                                        priceWithMargin = priceWithMargin.multiply(productDto.getPrice());
                                    }
                                    oldInvoiceProduct.setFinalPrice(priceWithMargin);

                                    WarehouseProduct warehouseProduct = warehouseProductService
                                            .findByProductBarcode(oldInvoiceProduct.getProduct().getBarcode());
                                    if ((productDto.getAmount() - oldInvoiceProduct.getAmount()) != ZERO && !productDto.getPrice()
                                            .equals(oldInvoiceProduct.getFinalPrice())) {
                                        warehouseProduct.setVat(vat);
                                        warehouseProduct.setMargin(Integer.parseInt(margin));
                                        warehouseProduct.setFinalPrice(priceWithMargin);
                                        warehouseProduct.setVersion(warehouseProduct.getVersion() + ONE);
                                        warehouseProduct.setInitialPrice(productDto.getPrice());
                                        warehouseProduct.setResidue(warehouseProduct.getResidue() - oldInvoiceProduct.
                                                getAmount() + productDto.getAmount());
                                        warehouseProductService.save(warehouseProduct);

                                        WarehouseProductHistory warehouseProductHistory = new WarehouseProductHistory();
                                        warehouseProductHistory.setWarehouseProduct(warehouseProduct);
                                        if (productDto.getAmount() < oldInvoiceProduct.getAmount()) {
                                            warehouseProductHistory.setArrival(0);
                                            warehouseProductHistory.setDropped(oldInvoiceProduct.getAmount() - productDto.getAmount());
                                        } else if (productDto.getAmount() > oldInvoiceProduct.getAmount()) {
                                            int arrival = productDto.getAmount() - oldInvoiceProduct.getAmount();
                                            warehouseProductHistory.setArrival(arrival);
                                            warehouseProductHistory.setDropped(0);
                                        } else {
                                            warehouseProductHistory.setArrival(0);
                                            warehouseProductHistory.setDropped(0);
                                        }
                                        warehouseProductHistory.setResidue(warehouseProduct.getResidue());
                                        warehouseProductHistory.setDate(date);
                                        warehouseProductHistory.setInitialPrice(warehouseProduct.getInitialPrice());
                                        warehouseProductHistory.setFinalPrice(warehouseProduct.getFinalPrice());
                                        warehouseProductHistory.setVersion(warehouseProduct.getVersion());
                                        wphService.save(warehouseProductHistory);
                                    }

                                    oldInvoiceProduct.setAmount(productDto.getAmount());
                                    invoiceProductService.save(oldInvoiceProduct);
                                }
                            }
                        }
                        if (id == null) {
                            Product product = productService.findByBarcode(productDto.getBarcode());

                            InvoiceProduct invoiceProduct = new InvoiceProduct();
                            invoiceProduct.setInvoice(invoice);
                            invoiceProduct.setAmount(productDto.getAmount());
                            BigDecimal priceWithMargin = new BigDecimal(marginPercentage);
                            if (jsonUtil.isVatBoolean() && !vat) {
                                priceWithMargin = (priceWithMargin.multiply(productDto.getPrice()))
                                        .multiply(BigDecimal.valueOf(VAT));
                            } else {
                                priceWithMargin = priceWithMargin.multiply(productDto.getPrice());
                            }
                            productDto.setVat(vat);
                            productDto.setMargin(Integer.parseInt(margin));
                            productDto.setFinalPrice(priceWithMargin);
                            invoiceProduct.setFinalPrice(productDto.getFinalPrice());
                            invoiceProduct.setInitialPrice(productDto.getPrice());

                            WarehouseProduct warehouseProduct = new WarehouseProduct();
                            warehouseProduct.setInitialPrice(productDto.getPrice());
                            warehouseProduct.setMargin(productDto.getMargin());
                            warehouseProduct.setVat(productDto.getVat());
                            warehouseProduct.setFinalPrice(productDto.getFinalPrice());
                            warehouseProduct.setWarehouse(warehouse);
                            warehouseProduct.setResidue(productDto.getResidue());
                            warehouseProduct.setVersion(ONE);

                            setProductToInvoiceProduct(product, productDto, warehouseProduct, invoiceProduct);
                            setProviderAndProduct(invoice.getProvider(), warehouseProduct.getProduct());

                            WarehouseProduct warehouseProductFromDB = warehouseProductService
                                    .findByProductBarcode(warehouseProduct.getProduct().getBarcode());
                            if (warehouseProductFromDB == null) {
                                WarehouseProduct savedWP = warehouseProductService.save(warehouseProduct);

                                WarehouseProductHistory wphCurrentVersion = new WarehouseProductHistory();
                                wphCurrentVersion.setWarehouseProduct(savedWP);
                                wphCurrentVersion.setVersion(warehouseProduct.getVersion());
                                wphCurrentVersion.setInitialPrice(warehouseProduct.getInitialPrice());
                                wphCurrentVersion.setFinalPrice(warehouseProduct.getFinalPrice());
                                wphCurrentVersion.setArrival(productDto.getAmount());
                                wphCurrentVersion.setResidue(warehouseProduct.getResidue());
                                wphCurrentVersion.setDate(date);
                                wphService.save(wphCurrentVersion);
                            } else {
                                if (warehouseProductFromDB.getVersion() != ONE) {
                                    WarehouseProductHistory wphPreviousVersion = wphService.findByVersion(warehouseProductFromDB.getVersion());
                                    wphPreviousVersion.setWarehouseProduct(warehouseProductFromDB);
                                    wphPreviousVersion.setArrival(productDto.getAmount());
                                    wphPreviousVersion.setResidue(warehouseProductFromDB.getResidue());
                                    wphPreviousVersion.setInitialPrice(warehouseProductFromDB.getInitialPrice());
                                    wphPreviousVersion.setFinalPrice(warehouseProductFromDB.getFinalPrice());
                                    wphPreviousVersion.setDate(new Date());

                                    wphService.save(wphPreviousVersion);
                                }

                                warehouseProductFromDB.setVersion(warehouseProductFromDB.getVersion() + ONE);
                                warehouseProductFromDB.setResidue(warehouseProductFromDB.getResidue() + warehouseProduct.getResidue());
                                warehouseProductFromDB.setInitialPrice(warehouseProduct.getInitialPrice());
                                warehouseProductFromDB.setFinalPrice(warehouseProduct.getFinalPrice());
                                warehouseProductFromDB.setVat(warehouseProduct.isVat());
                                warehouseProductFromDB.setMargin(warehouseProduct.getMargin());
                                warehouseProductService.save(warehouseProductFromDB);

                                WarehouseProductHistory wphCurrentVersion = new WarehouseProductHistory();
                                wphCurrentVersion.setWarehouseProduct(warehouseProductFromDB);
                                wphCurrentVersion.setVersion(warehouseProductFromDB.getVersion() + ONE);
                                wphCurrentVersion.setArrival(productDto.getAmount());
                                wphCurrentVersion.setResidue(warehouseProductFromDB.getResidue() + warehouseProduct.getResidue());
                                wphCurrentVersion.setDate(new Date());
                            }
                            invoiceProductService.save(invoiceProduct);
                        }
                    }

                    if (removedProducts != null) {
                        for (ProductDto productDto : removedProducts) {
                            String id = productDto.getId();
                            for (InvoiceProduct oldInvoiceProduct : oldInvoiceProducts) {
                                if (oldInvoiceProduct.getId() == UUID.fromString(id)) {
                                    invoiceProductService.delete(oldInvoiceProduct.getId());

                                    WarehouseProduct warehouseProduct = warehouseProductService
                                            .findByProductBarcode(oldInvoiceProduct.getProduct().getBarcode());

                                    WarehouseProductHistory warehouseProductHistory = new WarehouseProductHistory();
                                    warehouseProductHistory.setWarehouseProduct(warehouseProduct);
                                    warehouseProductHistory.setVersion(warehouseProduct.getVersion());
                                    warehouseProductHistory.setArrival(productDto.getAmount());
                                    warehouseProductHistory.setResidue(warehouseProduct.getResidue());
                                    warehouseProductHistory.setDate(new Date());
                                    wphService.save(warehouseProductHistory);

                                    warehouseProduct.setVersion(warehouseProduct.getVersion() + ONE);
                                    warehouseProduct.setResidue(warehouseProduct.getResidue() - oldInvoiceProduct.getAmount());
                                    warehouseProductService.save(warehouseProduct);
                                }
                            }
                        }
                    }
                } else {
                    alert(Alert.AlertType.WARNING, "Список товаров пуст", null, "Извините, " +
                            "но вы не можете сохранить изменения пока не добавите хотя бы один товар.");
                }
                showReceiptsPage();
            } catch (ControllerException e) {
                alert(Alert.AlertType.ERROR, "Внутренная ошибка", null, "Пожалуйста, проверьте корректность введённых данных");
            }
        }
    }

    private void setProductToInvoiceProduct(Product product, ProductDto productDto, WarehouseProduct wp, InvoiceProduct ip) {
        if (product != null) {
            wp.setProduct(product);
            ip.setProduct(product);
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
            ProductProvider productProvider = new ProductProvider();
            productProvider.setProduct(createdProduct);
            productProvider.setProvider(providerService.findByCompanyName(providers.getValue()));
            productProvider.setBlocked(false);
            productProviderService.save(productProvider);
            wp.setProduct(createdProduct);
            ip.setProduct(createdProduct);
        }
    }

    private void setProviderAndProduct(Provider provider, Product product) {
        ProductProvider productProvider = new ProductProvider();
        ProductProvider oldProductProvider = productProviderService.
                findByProviderIdAndProductId(provider.getId(), product.getId());
        if (oldProductProvider == null) {
            productProvider.setProvider(provider);
            productProvider.setProduct(product);
            productProviderService.save(productProvider);
        }
    }

    @FXML
    private void deleteProductFromTable() {
        if (!springHelper.isNewInvoice()) {
            if (removedProducts == null) removedProducts = new ArrayList<>();
        }
        ProductDto productDto = productsTable.getSelectionModel().getSelectedItem();
        if (productDto == null) {
            alert(Alert.AlertType.WARNING, "Товар не выбран", null, "Пожалуйста, выберите товар, который хотите удалить.");
        } else {
            if (!springHelper.isNewInvoice()) {
                removedProducts.add(productDto);
            }
            productsData.remove(productDto);
            productsTable.getItems().remove(productDto);
        }
    }

    @FXML
    private void addProductToTable() {
        String categoryName = categories.getValue();
        String productName = productComboBox.getEditor().getText();
        String unit = unitOfMeasure.getEditor().getText();
        BigDecimal price = new BigDecimal(this.price.getText());
        Integer amount = Integer.valueOf(this.amount.getText());
        String barcode = this.barcode.getText();
        if (barcodes == null) barcodes = new HashSet<>();
        barcodes.clear();

        setAmountAndPriceToProductDto(barcode, amount, price);
        if (!productName.equals("") && !barcode.equals("") && !unit.equals("")) {
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
            this.price.clear();
            this.amount.clear();
        } else
            alert(Alert.AlertType.WARNING, "Ошибка добавления", null, "Пожалуйста, заполните все поля правильно.");
    }

    private void showReceiptsPage() {
        PagesConfiguration screens = getPagesConfiguration();
        try {
            mainController.getContentContainer().getChildren().setAll(screens.receipts());
        } catch (IOException e) {
            throw new ControllerException(e);
        }
    }
}
