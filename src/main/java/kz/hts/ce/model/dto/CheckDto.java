package kz.hts.ce.model.dto;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.util.UUID;

public class CheckDto {
    private StringProperty id;
    private StringProperty fullName;
    private LongProperty checkNumber;
    private StringProperty date;
    private ObjectProperty<BigDecimal> totalPrice;

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(UUID id) {
        if (this.id == null) {
            this.id = new SimpleStringProperty();
        }
        this.id.set(String.valueOf(id));
    }

    public String getFullName() {
        return fullName.get();
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (this.fullName == null) {
            this.fullName = new SimpleStringProperty();
        }
        this.fullName.set(fullName);
    }

    public long getCheckNumber() {
        return checkNumber.get();
    }

    public LongProperty checkNumberProperty() {
        return checkNumber;
    }

    public void setCheckNumber(long checkNumber) {
        if (this.checkNumber == null) {
            this.checkNumber = new SimpleLongProperty();
        }
        this.checkNumber.set(checkNumber);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        if (this.date == null) {
            this.date = new SimpleStringProperty();
        }
        this.date.set(date);
    }

    public BigDecimal getTotalPrice() {
        return totalPrice.get();
    }

    public ObjectProperty<BigDecimal> totalPriceProperty() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        if (this.totalPrice == null) {
            this.totalPrice = new SimpleObjectProperty<>();
        }
        this.totalPrice.set(totalPrice);
    }
}
