package kz.hts.ce.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.model.entity.Employee;
import kz.hts.ce.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaFxUtil.getWatch;
import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;
import static kz.hts.ce.util.SpringUtil.getPrincipal;

@Controller
public class MainController implements Initializable {

    @FXML
    private Label dateLabel;
    @FXML
    private Label role;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getWatch(dateLabel);
        Employee employee = employeeService.findByUsername(getPrincipal());
        role.setText(employee.getRole().getName());
    }

    public void logout() {
        PagesConfiguration screens = getPagesConfiguration();
        screens.main().close();
        screens.login().show();
    }
}
