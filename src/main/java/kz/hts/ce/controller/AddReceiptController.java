package kz.hts.ce.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import org.springframework.stereotype.Controller;

import static kz.hts.ce.util.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class AddReceiptController {

    @FXML
    private VBox vBox;

    private int cnt=-1;

    @FXML
    public void add(){
        HBox box = new HBox();
        TextField productName = new TextField();
        TextField unitOfMeasure = new TextField();
        TextField amount = new TextField();
        productName.setPrefWidth(424);
        unitOfMeasure.setPrefWidth(156);
        amount.setPrefWidth(101);
        box.getChildren().addAll(productName, unitOfMeasure, amount);
        vBox.getChildren().add(box);
        cnt++;
    }
    @FXML
    public void delete(ActionEvent event){
        if(cnt!=-1) {
            vBox.getChildren().remove(cnt--);
        }
    }
}
