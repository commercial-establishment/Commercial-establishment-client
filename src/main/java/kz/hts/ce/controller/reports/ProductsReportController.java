package kz.hts.ce.controller.reports;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.Category;
import kz.hts.ce.model.entity.Product;
import kz.hts.ce.service.CategoryService;
import kz.hts.ce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Controller
public class ProductsReportController {
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TreeTableView<String> productsReport;
    @FXML
    private TreeTableColumn<Category, ProductDto> categoryProduct;
    @FXML
    private TreeTableColumn<ProductDto, Number> amount;
    @FXML
    private TreeTableColumn<ProductDto, String> unitOfMeasure;
    @FXML
    private TreeTableColumn<ProductDto, Number> residueInitial;
    @FXML
    private TreeTableColumn<ProductDto, Number> residueFinal;
    @FXML
    private TreeTableColumn<ProductDto, BigDecimal> costPrice;
    @FXML
    private TreeTableColumn<ProductDto, BigDecimal> shopPrice;


    private TreeItem<String> categoryTreeItem = new TreeItem<>();
    private TreeItem<String> productTreeItem = new TreeItem<>();


    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @FXML
    public void showReport() {
//        if (!startDate.getEditor().getText().equals("") && !endDate.getEditor().getText().equals("")){
//        }
        List<Category> categories = categoryService.findAll();
        categoryTreeItem.setExpanded(true);
        TreeItem<String> name = new TreeItem<>();
        for (Category category : categories) {
            name.setValue(category.getName());
            categoryTreeItem.setValue(category.getName());
            categoryTreeItem.getChildren().addAll(name);
        }
//        TreeItem<String> ch1 = new TreeItem<>();
//        TreeItem<String> ch2 = new TreeItem<>();
//        ch1.setValue("1");
//        ch2.setValue("2");
//        categoryTreeItem.setExpanded(true);
//        categoryTreeItem.getChildren().setAll(ch1, ch2);
//        categoryProduct.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> p) -> new ReadOnlyStringWrapper(
//                p.getValue().getValue()));
//        productsReport.setRoot(categoryTreeItem);
    }
}
