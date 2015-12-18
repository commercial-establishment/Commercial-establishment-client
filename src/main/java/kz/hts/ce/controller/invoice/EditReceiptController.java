package kz.hts.ce.controller.invoice;

import com.sun.javafx.scene.control.skin.TableViewSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import kz.hts.ce.controller.ControllerException;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.*;
import kz.hts.ce.service.*;
import kz.hts.ce.util.javafx.EditingBigDecimalCell;
import kz.hts.ce.util.javafx.EditingNumberCell;
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
public class EditReceiptController implements Initializable {

    public static final double VAT = 1.12;
    public static final int ZERO = 0;
    public static final int ONE = 1;

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
    @FXML
    private AnchorPane editPane;

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
    private WarehouseProductHistoryService wphService;

    @Autowired
    private ReceiptPageController receiptPageController;

    @Autowired
    private SpringUtil springUtil;
    @Autowired
    private JsonUtil jsonUtil;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceFromDB = invoiceService.findById(springUtil.getId());

        barcodes = new HashSet<>();
        List<Product> products = productService.findAll();
        barcodes.addAll(products.stream().map(Product::getBarcode).collect(Collectors.toList()));
        productsData.clear();

        initializeTableColumns();

        List<Unit> units = unitService.findAll();
        List<String> unitNames = units.stream().map(Unit::getName).collect(Collectors.toList());
        unitOfMeasure.getItems().addAll(unitNames);

        long shopId = springUtil.getEmployee().getShop().getId();
        List<ShopProvider> shopProviders = shopProviderService.findByShopId(shopId);
        List<String> providerNames = shopProviders.stream().map(shopProvider -> shopProvider.getProvider()
                .getCompanyName()).collect(Collectors.toList());
        providers.getItems().addAll(providerNames);

        amount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(ONE, 10000, ONE));
        margin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(ONE, 10000, invoiceFromDB.getMargin()));

        providers.setValue(invoiceFromDB.getProvider().getCompanyName());

        Date input = invoiceFromDB.getDate();
        LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.date.setValue(date);

        postponement.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(ZERO, 1000, invoiceFromDB.getPostponement()));

        vat.selectedProperty().setValue(invoiceFromDB.isVat());

        oldInvoiceProducts = invoiceProductService.findByInvoiceId(springUtil.getId());

        for (InvoiceProduct invoiceProduct : oldInvoiceProducts) {
            ProductDto productDto = createProductDtoFromProduct(invoiceProduct.getProduct());
            productDto.setPrice(invoiceProduct.getInitialPrice());
            productDto.setAmount(invoiceProduct.getAmount());
            productDto.setId(invoiceProduct.getId());
            productsData.add(productDto);
            productsTable.getItems().add(productDto);
        }

        List<Category> categoriesFromDB = categoryService.findAll();
        List<String> categoryNames = categoriesFromDB.stream().map(Category::getName).collect(Collectors.toList());
        categories.getItems().addAll(categoryNames);

        productComboBoxListener();

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
    private void addProductToTable() {
        String categoryName = categories.getValue();
        String productName = productComboBox.getEditor().getText();
        String unit = unitOfMeasure.getEditor().getText();
        BigDecimal price = new BigDecimal(this.price.getText());
        Integer amount = this.amount.getValue();
        String barcode = this.barcode.getText();

        if (!productName.equals("") && !barcode.equals("") && !unit.equals("")) {
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
                productDto.setId(ZERO);
                productsData.add(productDto);
                productsTable.setItems(productsData);
            }
            deleteRowColumn.setDisable(false);

            productComboBox.setValue("");
            this.barcode.setText("");
            this.unitOfMeasure.getEditor().setText("");
            this.price.setText(String.valueOf(ZERO));
        } else alert(Alert.AlertType.WARNING, "Ошибка добавления", null, "Пожалуйста, заполните все поля правильно.");
    }

    @FXML
    @Transactional
    private void updateInvoice() {
        try {
            LocalDate localDate = this.date.getValue();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (productsData.size() != ZERO) {
                Employee employee = springUtil.getEmployee();
                long shopId = employee.getShop().getId();
                Warehouse warehouse = warehouseService.findByShopId(shopId);

                Integer postponement = this.postponement.getValue();
                boolean vat = this.vat.isSelected();
                String margin = String.valueOf(this.margin.getValue());

                invoiceFromDB.setMargin(Integer.parseInt(margin));
                invoiceFromDB.setPostponement(postponement);
                invoiceFromDB.setVat(vat);

                Invoice invoice = invoiceService.save(invoiceFromDB);

                String marginPercentage = String.valueOf((Double.valueOf(margin) / 100) + ONE);
                for (ProductDto productDto : productsData) {
                    long id = productDto.getId();
                    for (InvoiceProduct oldInvoiceProduct : oldInvoiceProducts) {
                        if (id == oldInvoiceProduct.getId()) {
                            oldInvoiceProduct.setInitialPrice(productDto.getPrice());
                            BigDecimal priceWithMargin = new BigDecimal(marginPercentage);
                            if (jsonUtil.isVatBoolean() && !vat) {
                                priceWithMargin = (priceWithMargin.multiply(productDto.getPrice()))
                                        .multiply(BigDecimal.valueOf(VAT));
                            } else {
                                priceWithMargin = priceWithMargin.multiply(productDto.getPrice());
                            }
                            oldInvoiceProduct.setMargin(Integer.parseInt(margin));
                            oldInvoiceProduct.setFinalPrice(priceWithMargin);
                            oldInvoiceProduct.setFinalPrice(priceWithMargin);

                            WarehouseProduct warehouseProduct = warehouseProductService
                                    .findByProductBarcode(oldInvoiceProduct.getProduct().getBarcode());
                            warehouseProduct.setArrival(productDto.getAmount() - oldInvoiceProduct.getAmount());
                            if (warehouseProduct.getArrival() != ZERO && !productDto.getPrice().equals(oldInvoiceProduct.getFinalPrice())) {
                                warehouseProduct.setDate(date);
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
                                warehouseProductHistory.setArrival(warehouseProduct.getArrival());
                                warehouseProductHistory.setResidue(warehouseProduct.getResidue());
                                warehouseProductHistory.setDate(date);
                                warehouseProductHistory.setVersion(warehouseProduct.getVersion());
                                warehouseProductHistory.setProvider(invoice.getProvider());
                                wphService.save(warehouseProductHistory);
                            }

                            oldInvoiceProduct.setAmount(productDto.getAmount());
                            invoiceProductService.save(oldInvoiceProduct);
                        }
                    }
                    if (id == ZERO) {
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
                        invoiceProduct.setMargin(productDto.getMargin());
                        invoiceProduct.setInitialPrice(productDto.getPrice());

                        WarehouseProduct warehouseProduct = new WarehouseProduct();
                        warehouseProduct.setInitialPrice(productDto.getPrice());
                        warehouseProduct.setMargin(productDto.getMargin());
                        warehouseProduct.setVat(productDto.getVat());
                        warehouseProduct.setFinalPrice(productDto.getFinalPrice());
                        warehouseProduct.setWarehouse(warehouse);
                        warehouseProduct.setArrival(productDto.getAmount());
                        warehouseProduct.setResidue(productDto.getResidue());
                        warehouseProduct.setVersion(ONE);
                        warehouseProduct.setDate(date);
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
                        WarehouseProduct warehouseProductFromDB = warehouseProductService
                                .findByProductBarcode(warehouseProduct.getProduct().getBarcode());
                        if (warehouseProductFromDB == null) {
                            WarehouseProduct savedWP = warehouseProductService.save(warehouseProduct);

                            WarehouseProductHistory wphCurrentVersion = new WarehouseProductHistory();
                            wphCurrentVersion.setWarehouseProduct(savedWP);
                            wphCurrentVersion.setVersion(warehouseProduct.getVersion());
                            wphCurrentVersion.setArrival(warehouseProduct.getArrival());
                            wphCurrentVersion.setResidue(warehouseProduct.getResidue());
                            wphCurrentVersion.setDate(warehouseProduct.getDate());
                            wphCurrentVersion.setProvider(invoice.getProvider());
                            wphService.save(wphCurrentVersion);
                        } else {
                            if (warehouseProductFromDB.getVersion() != ONE) {
                                WarehouseProductHistory wphPreviousVersion = wphService.findByVersion(warehouseProductFromDB.getVersion());
                                wphPreviousVersion.setWarehouseProduct(warehouseProductFromDB);
                                wphPreviousVersion.setArrival(warehouseProductFromDB.getArrival());
                                wphPreviousVersion.setResidue(warehouseProductFromDB.getResidue());
                                wphPreviousVersion.setDate(warehouseProductFromDB.getDate());
                                wphPreviousVersion.setProvider(invoice.getProvider());

                                wphService.save(wphPreviousVersion);
                            }

                            warehouseProductFromDB.setVersion(warehouseProductFromDB.getVersion() + ONE);
                            warehouseProductFromDB.setArrival(warehouseProduct.getArrival());
                            warehouseProductFromDB.setResidue(warehouseProductFromDB.getResidue() + warehouseProduct.getResidue());
                            warehouseProductFromDB.setInitialPrice(warehouseProduct.getInitialPrice());
                            warehouseProductFromDB.setFinalPrice(warehouseProduct.getFinalPrice());
                            warehouseProductFromDB.setVat(warehouseProduct.isVat());
                            warehouseProductFromDB.setMargin(warehouseProduct.getMargin());
                            warehouseProductFromDB.setDate(date);
                            warehouseProductService.save(warehouseProductFromDB);

                            WarehouseProductHistory wphCurrentVersion = new WarehouseProductHistory();
                            wphCurrentVersion.setWarehouseProduct(warehouseProductFromDB);
                            wphCurrentVersion.setVersion(warehouseProductFromDB.getVersion() + ONE);
                            wphCurrentVersion.setArrival(warehouseProduct.getArrival());
                            wphCurrentVersion.setResidue(warehouseProductFromDB.getResidue() + warehouseProduct.getResidue());
                            wphCurrentVersion.setDate(date);
                            wphCurrentVersion.setProvider(invoice.getProvider());
                        }
                        invoiceProductService.save(invoiceProduct);
                    }
                }

                if (removedProducts != null) {
                    for (ProductDto productDto : removedProducts) {
                        long id = productDto.getId();
                        for (InvoiceProduct oldInvoiceProduct : oldInvoiceProducts) {
                            if (oldInvoiceProduct.getId() == id) {
                                invoiceProductService.delete(oldInvoiceProduct.getId());

                                WarehouseProduct warehouseProduct = warehouseProductService
                                        .findByProductBarcode(oldInvoiceProduct.getProduct().getBarcode());

                                WarehouseProductHistory warehouseProductHistory = new WarehouseProductHistory();
                                warehouseProductHistory.setWarehouseProduct(warehouseProduct);
                                warehouseProductHistory.setVersion(warehouseProduct.getVersion());
                                warehouseProductHistory.setArrival(warehouseProduct.getArrival());
                                warehouseProductHistory.setResidue(warehouseProduct.getResidue());
                                warehouseProductHistory.setDate(date);
                                warehouseProductHistory.setProvider(invoice.getProvider());
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
            receiptPageController.showReceiptsPage();
        } catch (ControllerException e) {
            alert(Alert.AlertType.ERROR, "Внутренная ошибка", null, "Пожалуйста, проверьте корректность введённых данных");
        }
    }

    @FXML
    private void deleteProductFromTable() {
        if (removedProducts == null) removedProducts = new ArrayList<>();
        ProductDto productDto = productsTable.getSelectionModel().getSelectedItem();
        if (productDto == null) {
            alert(Alert.AlertType.WARNING, "Товар не выбран", null, "Пожалуйста, выберите товар, который хотите удалить.");
        } else {
            removedProducts.add(productDto);
            productsData.remove(productDto);
            productsTable.getItems().remove(productDto);
        }
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
                    productDtosByCategory.stream().filter(dto -> dto.getName().toLowerCase().contains(newValue
                            .toLowerCase())).forEach(dto -> {
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
}
