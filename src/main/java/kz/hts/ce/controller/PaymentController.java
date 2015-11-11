package kz.hts.ce.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.util.AppContextSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;



@Controller
public class PaymentController {
    @FXML
    private TextField given;
    @FXML
    private TextField change;
    @FXML
    private Button accept, cancel, print;

    @FXML
    private void initialize(){
        given.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                System.out.println("TextField Text Changed (newValue: " + newValue + ")\n");

            }
        });
    }


}
