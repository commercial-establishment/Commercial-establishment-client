package kz.hts.ce.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import kz.hts.ce.config.PagesConfiguration;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import static kz.hts.ce.util.JavaFxUtil.getWatch;
import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class MainController implements Initializable {

    public Label dateLabel;
    public Button button;
    @FXML
    private SplitPane splitPane;
    private boolean flag;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getWatch(dateLabel);
        PagesConfiguration screens = getPagesConfiguration();
        screens.getPrimaryStage().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                splitPane.lookupAll(".split-pane-divider").stream().forEach(div->div.setMouseTransparent(true));
                if(flag)
                    screens.getPrimaryStage().removeEventHandler(EventType.ROOT, this);
                flag = true;
            }
        });
    }
}
