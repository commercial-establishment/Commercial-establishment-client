package kz.hts.ce.controller.reports;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import kz.hts.ce.model.dto.CheckDto;
import kz.hts.ce.model.dto.ShiftDto;
import kz.hts.ce.model.entity.Check;
import kz.hts.ce.model.entity.CheckProduct;
import kz.hts.ce.model.entity.Shift;
import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.service.CheckProductService;
import kz.hts.ce.service.CheckService;
import kz.hts.ce.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static kz.hts.ce.util.JavaUtil.getEndOfDay;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;

@Controller
public class ShiftReportController {

    private TreeItem<ShiftDto> root = null;
    private TreeItem<ShiftDto> shiftItem = null;
    @FXML
    private TreeTableView shiftsReport;
    @FXML
    private TreeTableColumn<ShiftDto, String> fullName;
    @FXML
    private TreeTableColumn<ShiftDto, String> openingDate;
    @FXML
    private TreeTableColumn<ShiftDto, String> closingDate;
    @FXML
    private TreeTableColumn<ShiftDto, BigDecimal> averagePrice;
    @FXML
    private TreeTableColumn<ShiftDto, BigDecimal> sum;
    @FXML
    private TreeTableColumn<ShiftDto, BigDecimal> profitability;
    @FXML
    private TreeTableColumn<ShiftDto, BigDecimal> costPrice;

    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;

    @Autowired
    private ShiftService shiftService;
    @Autowired
    private CheckService checkService;
    @Autowired
    private CheckProductService checkProductService;

    @FXML
    private void showReport() {
        LocalDate startLocaleDate = startDate.getValue();
        LocalDate endLocaleDate = endDate.getValue();

        if (startLocaleDate == null || endLocaleDate == null) {
            alert(Alert.AlertType.WARNING, "Ошибка периода", null, "Пожалуйста укажите период");
        }

        root = new TreeItem<>();
        shiftItem = new TreeItem<>();
        root.setExpanded(true);
        shiftsReport.setShowRoot(false);

        Date startDateUtil = Date.from(startLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDateUtil = getEndOfDay(Date.from(endLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        List<Shift> shifts = shiftService.findByDatesBetween(startDateUtil, endDateUtil);

        for (Shift shift : shifts) {
            ShiftDto shiftDto = new ShiftDto();
            String fullName = shift.getEmployee().getFirstName() + " " + shift.getEmployee().getSurname();
            shiftDto.setFullName(fullName);
            shiftDto.setDateOfOpenShift(String.valueOf(shift.getStart()));
            shiftDto.setDateOfCloseShift(String.valueOf(shift.getEnd()));

            List<Check> checks = checkService.findByDateBetweenAndEmployeeId(
                    shift.getStart(), shift.getEnd(), shift.getEmployee().getId());

            BigDecimal sum = new BigDecimal(0);
            BigDecimal initialPrice = new BigDecimal(0);
            for (Check check : checks) {
                sum = sum.add(check.getPrice());
                List<CheckProduct> checkProducts = checkProductService.findByCheckId(check.getId());
                for (CheckProduct checkProduct : checkProducts) {
                    initialPrice = initialPrice.add(checkProduct.getWarehouseProduct().getInitialPrice());
                }
            }
            if (checks.size() != 0) {
                BigDecimal average = sum.divide(BigDecimal.valueOf(checks.size()));
                shiftDto.setAveragePrice(average);
            } else {
                shiftDto.setAveragePrice(BigDecimal.ZERO);
            }
            shiftDto.setSumOfChecks(sum);
            shiftDto.setCostPrice(initialPrice);
            shiftDto.setProfitability(sum.subtract(initialPrice));
            TreeItem<ShiftDto> item = new TreeItem<>(shiftDto);
            root.getChildren().add(item);
        }
        shiftsReport.setRoot(root);
        initializeTableFields();
    }

    private void initializeTableFields() {
        fullName.setCellValueFactory((TreeTableColumn.CellDataFeatures<ShiftDto, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getFullName()));
        openingDate.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getDateOfOpenShift()));
        closingDate.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getDateOfCloseShift()));
        averagePrice.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getAveragePrice()));
        sum.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getSumOfChecks()));
        profitability.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getProfitability()));
        costPrice.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getCostPrice()));

    }


}
