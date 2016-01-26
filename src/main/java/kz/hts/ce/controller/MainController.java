package kz.hts.ce.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.model.entity.Employee;
import kz.hts.ce.model.entity.Shift;
import kz.hts.ce.service.ShiftService;
import kz.hts.ce.util.spring.SpringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static kz.hts.ce.util.javafx.JavaFxUtil.getWatch;
import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class MainController implements Initializable {

    private boolean flag;

    @FXML
    private StackPane sales;
    @FXML
    private Label dateLabel;
    @FXML
    private Label role;
    @FXML
    private SplitPane splitPane;
    @FXML
    private StackPane contentContainer;

    @Autowired
    private ShiftService shiftService;

    @Lazy
    @Autowired
    private SpringHelper springHelper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PagesConfiguration screens = getPagesConfiguration();
        getWatch(dateLabel);
        screens.getPrimaryStage().getIcons().add(new Image("favicon.png"));
        screens.getPrimaryStage().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                splitPane.lookupAll(".split-pane-divider").stream().forEach(div -> div.setMouseTransparent(true));
                if (flag)
                    screens.getPrimaryStage().removeEventHandler(EventType.ROOT, this);
                flag = true;
            }
        });
        screens.getPrimaryStage().setOnCloseRequest(event -> {
            logout();
        });
        Employee employee = springHelper.getEmployee();
        role.setText(employee.getRole().getName());
    }

    public void logout() {
        PagesConfiguration screens = getPagesConfiguration();
        Shift shift = springHelper.getShift();
        shift.setEnd(new Date());
        shiftService.save(shift);
        springHelper.transmitAndReceiveData();
        screens.main().close();
        screens.login().show();
    }

    public StackPane getContentContainer() {
        return contentContainer;
    }
}
