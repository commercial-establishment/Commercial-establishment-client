package kz.hts.ce.model.dto;

import javafx.beans.property.*;

import java.math.BigDecimal;

import static kz.hts.ce.util.JavaUtil.multiplyIntegerAndBigDecimal;

public class ProductDto {

    private LongProperty id;
    private StringProperty name;
    private IntegerProperty amount;
    private ObjectProperty<BigDecimal> price;
    private ObjectProperty<BigDecimal> totalPrice;
    private IntegerProperty residue;
    private LongProperty barcode;
    private StringProperty unitName;
    private StringProperty categoryName;

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        if (this.id == null) {
            this.id = new SimpleLongProperty();
        }
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        if (this.name == null) {
            this.name = new SimpleStringProperty();
        }
        this.name.set(name);
    }

    public String getCategoryName() {
        return categoryName.get();
    }

    public StringProperty categoryNameProperty() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        if (this.categoryName == null) {
            this.categoryName = new SimpleStringProperty();
        }
        this.categoryName.set(categoryName);
    }

    public String getUnitName() {
        return unitName.get();
    }

    public StringProperty unitNameProperty() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        if (this.unitName == null) {
            this.unitName = new SimpleStringProperty();
        }
        this.unitName.set(unitName);
    }

    public int getAmount() {
        return amount.get();
    }

    public IntegerProperty amountProperty() {
        return amount;
    }

    public void setAmount(int amount) {
        if (this.amount == null) {
            this.amount = new SimpleIntegerProperty();
        }
        this.amount.set(amount);
    }

    public int getResidue() {
        return residue.get();
    }

    public IntegerProperty residueProperty() {
        return residue;
    }

    public void setResidue(int residue) {
        if (this.residue == null) {
            this.residue = new SimpleIntegerProperty();
        }
        this.residue.set(residue);
    }

    public long getBarcode() {
        return barcode.get();
    }

    public LongProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        if (this.barcode == null) {
            this.barcode = new SimpleLongProperty();
        }
        this.barcode.set(barcode);
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public ObjectProperty<BigDecimal> priceProperty() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (this.price == null) {
            this.price = new SimpleObjectProperty<>();
        }
        this.price.set(price);
    }

    public BigDecimal getTotalPrice() {
        BigDecimal price = getPrice();
        int amount = getAmount();
        return multiplyIntegerAndBigDecimal(amount, price);
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        if (this.totalPrice == null) {
            this.totalPrice = new SimpleObjectProperty<>();
        }
        this.totalPrice.set(totalPrice);
    }

    public ObjectProperty<BigDecimal> totalPriceProperty() {
        BigDecimal price = getPrice();
        int amount = getAmount();
        BigDecimal totalPriceBD = multiplyIntegerAndBigDecimal(amount, price);
        setTotalPrice(totalPriceBD);
        return totalPrice;
    }
}
