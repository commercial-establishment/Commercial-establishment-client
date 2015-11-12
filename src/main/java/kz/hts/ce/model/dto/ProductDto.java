package kz.hts.ce.model.dto;

import javafx.beans.property.*;

import java.math.BigDecimal;

public class ProductDto {

    private StringProperty name;
    private IntegerProperty amount;
    private ObjectProperty<BigDecimal> price;
    private ObjectProperty<BigDecimal> totalPrice;

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

    public BigDecimal getPrice() {
        return price.get();
    }

    public ObjectProperty<BigDecimal> priceProperty() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (this.price == null) {
            this.price = new SimpleObjectProperty<BigDecimal>();
        }
        this.price.set(price);
    }

    public ObjectProperty<BigDecimal> totalPriceProperty() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice.set(totalPrice);
    }
//
//    public BigDecimal getTotalPrice() {
//        return multiplyIntegerAndBigDecimal(amount, price);
//    }
}
