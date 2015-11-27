package kz.hts.ce.controller;

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
        TextField price = new TextField();
        TextField priceAmount = new TextField();

        productName.setPrefWidth(362);
        price.setPrefWidth(118);
        unitOfMeasure.setPrefWidth(89);
        amount.setPrefWidth(89);
        priceAmount.setPrefWidth(112);
        box.getChildren().addAll(productName, price, unitOfMeasure, amount, priceAmount);
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
