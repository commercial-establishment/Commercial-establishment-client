package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Controller;

@Controller
public class AddReceiptController {

    private int cnt=-1;

    @FXML
    private VBox vBox;

    @FXML
    private void addRowForNewProduct(){
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
    private void deleteProductRow(){
        if(cnt!=-1) {
            vBox.getChildren().remove(cnt--);
        }
    }
}
