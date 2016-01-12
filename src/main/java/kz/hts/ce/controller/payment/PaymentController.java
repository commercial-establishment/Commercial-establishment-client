package kz.hts.ce.controller.payment;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.controller.ControllerException;
import kz.hts.ce.controller.SalesController;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.Check;
import kz.hts.ce.model.entity.CheckProduct;
import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.model.entity.WarehouseProductHistory;
import kz.hts.ce.service.CheckProductService;
import kz.hts.ce.service.CheckService;
import kz.hts.ce.service.WarehouseProductHistoryService;
import kz.hts.ce.service.WarehouseProductService;
import kz.hts.ce.util.spring.SpringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

import static kz.hts.ce.util.JavaUtil.stringToBigDecimal;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;
import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;

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
    private WarehouseProductHistoryService warehouseProductHistoryService;
    @Autowired
    private CheckService checkService;
    @Autowired
    private CheckProductService checkProductService;

    @Lazy
    @Autowired
    private SpringHelper springHelper;

    @Lazy
    @Autowired
    private SalesController salesController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        total.setText(salesController.getPriceResult().getText());
        shortage.setText(total.getText());
        given.setText(ZERO);
        change.setText(ZERO);
        given.textProperty().addListener((observable, oldValue, newValue) -> {
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
            } else {
                given.setText("");
                shortage.setText(total.getText());
            }
        });
    }

    @FXML
    public void success() {
        try {
            if (shortage.getText().equals(ZERO)) {
                ObservableList<ProductDto> productsData = salesController.getProductsData();

                Check check = new Check();
                check.setDate(Calendar.getInstance().getTime());
                check.setEmployee(springHelper.getEmployee());
                check.setShop(springHelper.getEmployee().getShop());
                check.setCheckNumber("123");
                checkService.save(check);

                for (ProductDto productDto : productsData) {
                    String productId = productDto.getId();
                    WarehouseProduct warehouseProduct = warehouseProductService.findByProductId(UUID.fromString(productId));

                    Date newDate = new Date();
                    int productAmount = productDto.getAmount();
                    int residue = productDto.getResidue();
                    warehouseProduct.setResidue(residue - productAmount);

                    warehouseProduct.setVersion(warehouseProduct.getVersion() + 1);
                    warehouseProductService.save(warehouseProduct);

                    WarehouseProductHistory wphCurrentVersion = new WarehouseProductHistory();
                    wphCurrentVersion.setWarehouseProduct(warehouseProduct);
                    wphCurrentVersion.setVersion(warehouseProduct.getVersion());
                    wphCurrentVersion.setDate(newDate);
                    wphCurrentVersion.setResidue(warehouseProduct.getResidue());
                    wphCurrentVersion.setSold(productAmount);
                    wphCurrentVersion.setInitialPrice(warehouseProduct.getInitialPrice());
                    wphCurrentVersion.setFinalPrice(warehouseProduct.getFinalPrice());
                    warehouseProductHistoryService.save(wphCurrentVersion);

                    CheckProduct checkProduct = new CheckProduct();
                    checkProduct.setCheck(check);
                    checkProduct.setWarehouseProduct(warehouseProduct);
                    checkProduct.setAmount(productAmount);
                    checkProductService.save(checkProduct);
                }
                alert(Alert.AlertType.INFORMATION, "Товар успешно продан", null, "Сдача: " + change.getText() + " тенге");
                salesController.deleteAllProductsFromTable();
                salesController.getProductsDto().clear();
                PagesConfiguration screens = getPagesConfiguration();
                screens.payment().close();
            } else {
                alert(Alert.AlertType.WARNING, "Недостаточно средств", null, "Недостаточно средств для оплаты товара");
            }
        } catch (RuntimeException e) {
            alert(Alert.AlertType.ERROR, "Внутренняя ошибка", null, "Оплата не произведена.");
            throw new ControllerException(e);
        }
    }

    @FXML
    public void print(ActionEvent event) {

    }

    @FXML
    public void cancel() {
        PagesConfiguration screens = getPagesConfiguration();
        screens.payment().hide();
    }

    public TextField getGiven() {
        return given;
    }
}
