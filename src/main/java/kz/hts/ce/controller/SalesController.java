package kz.hts.ce.controller;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import kz.hts.ce.config.PagesConfiguration;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class SalesController implements Initializable {

    @FXML
    private SplitPane splitPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DoubleProperty dividerPositionProperty = splitPane.getDividers().get(0).positionProperty();
        dividerPositionProperty.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            splitPane.setDividerPositions(.92);
        });
    }

    @FXML
    public void exit(){
        getPagesConfiguration().sales().close();
    }
    @FXML
    public void changeMode(){
        getPagesConfiguration().sales().close();
        getPagesConfiguration().main();
    }
}

