package kz.hts.ce.controller.reports;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import kz.hts.ce.model.dto.CheckDto;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.*;
import kz.hts.ce.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static kz.hts.ce.util.JavaUtil.getEndOfDay;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;

@Controller
public class ChecksReportController {

    private TreeItem<CheckDto> root = null;
    private TreeItem<CheckDto> checkItem = null;

    @FXML
    private TreeTableView checksReport;
    @FXML
    private TreeTableColumn<CheckDto, String> fullName;
    @FXML
    private TreeTableColumn<CheckDto, Number> checkNumber;
    @FXML
    private TreeTableColumn<CheckDto, String> dateOfSale;
    @FXML
    private TreeTableColumn<CheckDto, BigDecimal> totalPrice;

    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;

    @Autowired
    private CheckService checkService;

    @FXML
    public void showReport() {
        LocalDate startLocaleDate = startDate.getValue();
        LocalDate endLocaleDate = endDate.getValue();

        if (startLocaleDate == null || endLocaleDate == null) {
            alert(Alert.AlertType.WARNING, "Ошибка периода", null, "Пожалуйста укажите период");
        }

        root = new TreeItem<>();
        checkItem = new TreeItem<>();
        root.setExpanded(true);
        checksReport.setShowRoot(false);

        Date startDateUtil = Date.from(startLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDateUtil = getEndOfDay(Date.from(endLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        List<Check> checks = checkService.findByDateBetween(startDateUtil, endDateUtil);
        Set<Employee> employees = checks.stream().map(check -> check.getEmployee())
                .collect(Collectors.toSet());

        Map<String, List<Check>> checkEmployeeMap = new HashMap<>();
        for (Employee employee : employees) {
            List<Check> checkByEmployee = checkService.findByEmployeeUsername(employee.getUsername());
            checkEmployeeMap.put(employee.getFirstName() + " " + employee.getSurname(), checkByEmployee);
        }
        CheckDto checkDto = new CheckDto();
        checkDto.setFullName("");
        checkDto.setCheckNumber(0);
        checkDto.setDate("");
        checkDto.setTotalPrice(BigDecimal.ZERO);
        root.setValue(checkDto);

        for (Map.Entry<String, List<Check>> map : checkEmployeeMap.entrySet()) {
            String fullName = map.getKey();
            List<Check> checkList = map.getValue();
            CheckDto checkDtoKey = new CheckDto();
            checkDtoKey.setFullName(fullName);
            checkDtoKey.setCheckNumber(0);
            checkDtoKey.setDate("");
            checkDtoKey.setTotalPrice(BigDecimal.ZERO);

            TreeItem<CheckDto> fullNameItem = new TreeItem<>(checkDtoKey);
            root.getChildren().add(fullNameItem);
            fullNameItem.setExpanded(true);

            for (Check check : checkList) {
                CheckDto checkDtoValue = new CheckDto();
                checkDtoValue.setFullName(fullName);
                checkDtoValue.setCheckNumber(check.getCheckNumber());
                checkDtoValue.setDate(String.valueOf(check.getDate()));
                checkDtoValue.setTotalPrice(check.getPrice());

                TreeItem<CheckDto> checkItem = new TreeItem<>(checkDtoValue);
                fullNameItem.getChildren().add(checkItem);
            }
        }
        checksReport.setRoot(root);
        initializeTableFields();
    }

    private void initializeTableFields() {
        fullName.setCellValueFactory((TreeTableColumn.CellDataFeatures<CheckDto, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getFullName()));
        checkNumber.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getCheckNumber()));
        dateOfSale.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getDate()));
        totalPrice.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getTotalPrice()));
    }

}


