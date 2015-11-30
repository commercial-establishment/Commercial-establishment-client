package kz.hts.ce.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.*;
import kz.hts.ce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static kz.hts.ce.util.JavaFxUtil.alert;
import static kz.hts.ce.util.JavaUtil.createProductDtoFromProduct;
import static kz.hts.ce.util.SpringUtil.getPrincipal;

@Controller
public class AddReceiptController implements Initializable {

    private ObservableList<ProductDto> tableData = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> productComboBox;
    @FXML
    private TextField barcode;
    @FXML
    private Spinner<Integer> amount;
    @FXML
    private TextField price;
    @FXML
    private TextField unitOfMeasure;
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
    private TableView<ProductDto> tableView;

    @Autowired
    private ShopProviderService shopProviderService;
    @Autowired
    private ProductProviderService productProviderService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    private ObservableList<ProductDto> productDtosByCategory = FXCollections.observableArrayList();
    private List<ProductDto> productDtos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productDtos = new ArrayList<>();
        postponement.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0));
        amount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 1));

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
                        unitOfMeasure.setText(productDto.getUnitName());
                    } else if (newValue.equals("")) {
                        clearData();
                    } else {
                        ObservableList<String> items = productComboBox.getItems();
                        if (!items.contains(name)) {
                            items.add(name);
                        }
                        items.stream().filter(item -> !item.contains(newValue)).forEach(item -> productComboBox.getItems().remove(item));
                    }
                }
            }
            this.productComboBox.show();
        });
    }

    public void clearData(){
        productComboBox.getItems().clear();
        productComboBox.getEditor().setText("");
        barcode.setText("");
        unitOfMeasure.setText("");
    }

    @FXML
    public void add() {

    }

    @FXML
    private void save() {
        try {
            ObservableList<Node> products = vBox.getChildren();
            for (Node product : products) {
                HBox productHBox = (HBox) product;
                ObservableList<Node> productFields = productHBox.getChildren();
                for (Node productField : productFields) {
                    TextField productTextField = (TextField) productField;
                    String fieldText = ((TextField) productField).getText();
                }
            }
        } catch (ControllerException e) {
            alert(Alert.AlertType.WARNING, "Ошибка", null, "Не все поля заполнены верно");
        }
    }

    public void findProductsByCategory(ActionEvent event) {
        productDtosByCategory.clear();
        clearData();
        productComboBox.setDisable(false);

        ComboBox<String> source = (ComboBox<String>) event.getSource();
        List<Product> products = productService.findByCategoryName(source.getValue());

        for (Product product : products) {
            ProductDto productDto = createProductDtoFromProduct(product);
            productDtosByCategory.add(productDto);
        }
    }

    @FXML
    private void productSearch(ActionEvent event) {
        ComboBox source = (ComboBox) event.getSource();
        source.getValue();
    }
}
