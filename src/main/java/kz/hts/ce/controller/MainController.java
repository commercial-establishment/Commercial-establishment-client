package kz.hts.ce.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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

    private boolean flag;

    @FXML
    private Label dateLabel;
    @FXML
    private Label role;
    @FXML
    private SplitPane splitPane;
    @FXML
    private StackPane contentContainer;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getWatch(dateLabel);
        PagesConfiguration screens = getPagesConfiguration();
        screens.getPrimaryStage().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                splitPane.lookupAll(".split-pane-divider").stream().forEach(div -> div.setMouseTransparent(true));
                if (flag)
                    screens.getPrimaryStage().removeEventHandler(EventType.ROOT, this);
                flag = true;
            }
        });
        Employee employee = employeeService.findByUsername(getPrincipal());
        role.setText(employee.getRole().getName());
    }

    public void logout() {
        PagesConfiguration screens = getPagesConfiguration();
        screens.main().close();
        screens.login().show();
    }

    public StackPane getcontentContainer() {
        return contentContainer;
    }

    public void setcontentContainer(StackPane contentContainer) {
        this.contentContainer = contentContainer;
    }
}
