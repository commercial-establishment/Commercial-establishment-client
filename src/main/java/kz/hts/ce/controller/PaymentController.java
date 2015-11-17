package kz.hts.ce.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.ProductHistory;
import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.service.EmployeeService;
import kz.hts.ce.service.ProductHistoryService;
import kz.hts.ce.service.WarehouseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaFxUtil.alert;
import static kz.hts.ce.util.JavaUtil.multiplyIntegerAndBigDecimal;
import static kz.hts.ce.util.JavaUtil.stringToBigDecimal;
import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;
import static kz.hts.ce.util.SpringUtil.getPrincipal;

@Controller
public class PaymentController implements Initializable {

    public static final String ZERO = "0.00";

    @FXML
    private TextField shortage;
    @FXML
    private TextField given;
    @FXML
    private TextField change;
    @FXML
    private TextField total;

    @Autowired
    private WarehouseProductService warehouseProductService;
    @Autowired
    private ProductHistoryService productHistoryService;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProductsController productsController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        total.setText(productsController.getPriceResult().getText());
        shortage.setText(total.getText());
        given.setText(ZERO);
        change.setText(ZERO);
        given.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                BigDecimal totalBD = stringToBigDecimal(total.getText());
                if (!newValue.equals("")) {
                    BigDecimal newVal = stringToBigDecimal(newValue);
                    if (newVal.compareTo(totalBD) == 1) {
                        BigDecimal changeBD = newVal.subtract(totalBD);
                        change.setText(String.valueOf(changeBD));
                        shortage.setText(ZERO);
                    } else {
                        BigDecimal shortageBD = totalBD.subtract(newVal);
                        shortage.setText(String.valueOf(shortageBD));
                        change.setText(ZERO);
                    }
                } else given.setText("");
            }
        });
    }

    @FXML
    public void success() {
        if (shortage.getText().equals(ZERO)) {
            ObservableList<ProductDto> productsData = productsController.getProductsData();
            for (ProductDto productDto : productsData) {
                long warehouseProductId = productDto.getId();
                WarehouseProduct warehouseProduct = warehouseProductService.findById(warehouseProductId);

                ProductHistory productHistory = new ProductHistory();
                productHistory.setEmployee(employeeService.findByUsername(getPrincipal()));
                productHistory.setWarehouseProduct(warehouseProduct);
                productHistory.setVersion(warehouseProduct.getVersion());
                productHistory.setAmount(productDto.getAmount());
                productHistory.setSaleDate(new Date());
                productHistory.setTotalPrice(multiplyIntegerAndBigDecimal(productDto.getAmount(), warehouseProduct.getPrice()));
                productHistoryService.save(productHistory);

                int productAmount = productDto.getAmount();
                int residue = warehouseProduct.getResidue();
                warehouseProduct.setResidue(residue - productAmount);

                warehouseProduct.setVersion(warehouseProduct.getVersion() + 1);
                warehouseProductService.save(warehouseProduct);
            }
            alert(Alert.AlertType.INFORMATION, "Товар успешно продан", null, "Сдача: " + change.getText() + " тенге");
            productsController.deleteAllProductsFromTable();
            productsController.getProductsDto().clear();
            PagesConfiguration screens = getPagesConfiguration();
            screens.payment().close();
        } else {
            alert(Alert.AlertType.WARNING, "Недостаточно средств", null, "Недостаточно средств для оплаты товара");
        }
    }

    @FXML
    public void print(ActionEvent event) {

    }

    @FXML
    public void cancel() {
        PagesConfiguration screens = getPagesConfiguration();
        screens.payment().close();
    }

    public TextField getGiven() {
        return given;
    }

    public void setGiven(TextField given) {
        this.given = given;
    }
}
