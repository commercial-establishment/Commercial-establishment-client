package kz.hts.ce.model.dto;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.util.UUID;

import static kz.hts.ce.util.JavaUtil.multiplyIntegerAndBigDecimal;

public class ProductDto {

    private StringProperty id;
    private StringProperty name;
    private IntegerProperty amount;
    private IntegerProperty oldAmount;
    private ObjectProperty<BigDecimal> price;
    private ObjectProperty<BigDecimal> sumOfShopPrice;
    private ObjectProperty<BigDecimal> sumOfCostPrice;
    private IntegerProperty margin;
    private BooleanProperty vat;
    private ObjectProperty<BigDecimal> finalPrice;
    private ObjectProperty<BigDecimal> totalPrice;
    private IntegerProperty residue;
    private StringProperty barcode;
    private StringProperty unitName;
    private StringProperty unitSymbol;
    private StringProperty categoryName;
    private IntegerProperty arrival;
    private IntegerProperty soldAmount;
    private IntegerProperty dropped;

    public String getId() {
        try {
            return id.get();
        }catch (NullPointerException e){
            return null;
        }
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(UUID id) {
        if (this.id == null) this.id = new SimpleStringProperty();
        this.id.set(String.valueOf(id));
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        if (this.name == null) this.name = new SimpleStringProperty();
        this.name.set(name);
    }

    public String getCategoryName() {
        return categoryName.get();
    }

    public StringProperty categoryNameProperty() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        if (this.categoryName == null) this.categoryName = new SimpleStringProperty();
        this.categoryName.set(categoryName);
    }

    public String getUnitName() {
        return unitName.get();
    }

    public StringProperty unitNameProperty() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        if (this.unitName == null) this.unitName = new SimpleStringProperty();
        this.unitName.set(unitName);
    }

    public int getAmount() {
        return amount.get();
    }

    public IntegerProperty amountProperty() {
        return amount;
    }

    public void setAmount(int amount) {
        if (this.amount == null) this.amount = new SimpleIntegerProperty();
        this.amount.set(amount);
    }

    public int getResidue() {
        return residue.get();
    }

    public IntegerProperty residueProperty() {
        return residue;
    }

    public void setResidue(int residue) {
        if (this.residue == null) this.residue = new SimpleIntegerProperty();
        this.residue.set(residue);
    }

    public String getBarcode() {
        return barcode.get();
    }

    public StringProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        if (this.barcode == null) this.barcode = new SimpleStringProperty();
        this.barcode.set(barcode);
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public ObjectProperty<BigDecimal> priceProperty() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (this.price == null) this.price = new SimpleObjectProperty<>();
        this.price.set(price);
    }

    public int getMargin() {
        return margin.get();
    }

    public IntegerProperty marginProperty() {
        return margin;
    }

    public void setMargin(int margin) {
        if (this.margin == null) this.margin = new SimpleIntegerProperty();
        this.margin.set(margin);
    }

    public boolean getVat() {
        return vat.get();
    }

    public BooleanProperty vatProperty() {
        return vat;
    }

    public void setVat(boolean vat) {
        if (this.vat == null) this.vat = new SimpleBooleanProperty();
        this.vat.set(vat);
    }

    public BigDecimal getFinalPrice() {
        return finalPrice.get();
    }

    public ObjectProperty<BigDecimal> finalPriceProperty() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        if (this.finalPrice == null) this.finalPrice = new SimpleObjectProperty<>();
        this.finalPrice.set(finalPrice);
    }

    public BigDecimal getTotalPrice() {
        BigDecimal price = getPrice();
        int amount = getAmount();
        return multiplyIntegerAndBigDecimal(amount, price);
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        if (this.totalPrice == null) this.totalPrice = new SimpleObjectProperty<>();
        this.totalPrice.set(totalPrice);
    }

    public ObjectProperty<BigDecimal> totalPriceProperty() {
        BigDecimal price = getPrice();
        int amount = getAmount();
        BigDecimal totalPriceBD = multiplyIntegerAndBigDecimal(amount, price);
        setTotalPrice(totalPriceBD);
        return totalPrice;
    }

    public int getOldAmount() {
        return oldAmount.get();
    }

    public IntegerProperty oldAmountProperty() {
        return oldAmount;
    }

    public void setOldAmount(int oldAmount) {
        if (this.oldAmount == null) this.oldAmount = new SimpleIntegerProperty();
        this.oldAmount.set(oldAmount);
    }

    public String getUnitSymbol() {
        return unitSymbol.get();
    }

    public StringProperty unitSymbolProperty() {
        return unitSymbol;
    }

    public void setUnitSymbol(String unitSymbol) {
        if (this.unitSymbol == null) this.unitSymbol = new SimpleStringProperty();
        this.unitSymbol.set(unitSymbol);
    }

    public BigDecimal getSumOfShopPrice() {
        return sumOfShopPrice.get();
    }

    public ObjectProperty<BigDecimal> sumOfShopPriceProperty() {
        return sumOfShopPrice;
    }

    public void setSumOfShopPrice(BigDecimal sumOfShopPrice) {
        if (this.sumOfShopPrice == null) this.sumOfShopPrice = new SimpleObjectProperty<>();
        this.sumOfShopPrice.set(sumOfShopPrice);
    }

    public BigDecimal getSumOfCostPrice() {
        return sumOfCostPrice.get();
    }

    public ObjectProperty<BigDecimal> sumOfCostPriceProperty() {
        return sumOfCostPrice;
    }

    public void setSumOfCostPrice(BigDecimal sumOfCostPrice) {
        if (this.sumOfCostPrice == null) this.sumOfCostPrice = new SimpleObjectProperty<>();
        this.sumOfCostPrice.set(sumOfCostPrice);
    }

    public int getArrival() {
        return arrival.get();
    }

    public IntegerProperty arrivalProperty() {
        return arrival;
    }

    public void setArrival(int arrival) {
            if (this.arrival == null) this.arrival = new SimpleIntegerProperty();
        this.arrival.set(arrival);
    }

    public int getSoldAmount() {
        return soldAmount.get();
    }

    public IntegerProperty soldAmountProperty() {
        return soldAmount;
    }

    public void setSoldAmount(int soldAmount) {
        if (this.soldAmount == null) this.soldAmount = new SimpleIntegerProperty();
        this.soldAmount.set(soldAmount);
    }

    public int getDropped() {
        return dropped.get();
    }

    public IntegerProperty droppedProperty() {
        return dropped;
    }

    public void setDropped(int dropped) {
        if (this.dropped == null) this.dropped = new SimpleIntegerProperty();
        this.dropped.set(dropped);
    }
}
