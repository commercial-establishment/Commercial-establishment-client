package kz.hts.ce.controller.reports;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import kz.hts.ce.model.dto.ProductDto;
import kz.hts.ce.model.entity.Category;
import kz.hts.ce.model.entity.Product;
import kz.hts.ce.service.CategoryService;
import kz.hts.ce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class ProductsReportController {
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TreeTableView productsReport;
    @FXML
    private TreeTableColumn<ProductDto, String> categoryProduct;
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

    private TreeItem<ProductDto> parentRoot = new TreeItem<>();
    private TreeItem<Map<Category, List<ProductDto>>> categoryTreeItem = new TreeItem<>();
    private TreeItem<ProductDto> productTreeItem = new TreeItem<>();


    private ObservableList<ProductDto> productsData = FXCollections.observableArrayList();

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @FXML
    public void showReport() {
//        if (!startDate.getEditor().getText().equals("") && !endDate.getEditor().getText().equals("")){
//        }
        List<Category> all = categoryService.findAll();
        List<Product> productList = productService.findAll();
        parentRoot.setExpanded(true);

        ProductDto productDto = new ProductDto();
        productDto.setName("asdasdasd");
        productDto.setAmount(111);



        categoryTreeItem.getChildren().add(new TreeItem<>());
//        categoryTreeItem.setValue("");
//        parentRoot.getChildren().add(categoryTreeItem);
//        parentRoot.getChildren().add(new TreeItem<>(all.get(1).getName()));

        categoryProduct.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductDto, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getName()));
        amount.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue().getAmount()));
        productsReport.setRoot(parentRoot);
        productsReport.setShowRoot(false);
    }
}
