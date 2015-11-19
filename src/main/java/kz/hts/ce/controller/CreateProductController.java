package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import kz.hts.ce.model.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CreateProductController {
    @FXML
    private Button btnCreate;
    @FXML
    private TextField name;
    @FXML
    private TextField barCode;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private TextField price;
    @FXML
    private Spinner<Integer> amount;


    public void createProduct(ActionEvent event){

    }
}

