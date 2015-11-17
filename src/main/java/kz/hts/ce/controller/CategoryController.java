package kz.hts.ce.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import kz.hts.ce.model.entity.Category;
import kz.hts.ce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Controller
public class CategoryController implements Initializable {

    private ObservableList<String> categoriesData = FXCollections.observableArrayList();

    @FXML
    public ListView<String> categories;

    @Autowired
    private CategoryService categoryService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Category> categoriesFromDb = categoryService.findAll();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categoriesFromDb) {
            String categoryName = category.getName();
            categoryNames.add(categoryName);
        }

        categoriesData.addAll(categoryNames.stream().collect(Collectors.toList()));
        categories.setItems(categoriesData);
    }
}
