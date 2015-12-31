package kz.hts.ce.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class SalesController implements Initializable {

    @FXML
    private SplitPane splitPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DoubleProperty dividerPositionProperty = splitPane.getDividers().get(0).positionProperty();
        double[] pos = splitPane.getDividerPositions();
        dividerPositionProperty.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            splitPane.setDividerPositions(pos);
        });

    }

    @FXML
    public void exit() {
        getPagesConfiguration().sales().close();
    }

    @FXML
    public void changeMode() {
        getPagesConfiguration().sales().close();
        getPagesConfiguration().main();
    }
}

