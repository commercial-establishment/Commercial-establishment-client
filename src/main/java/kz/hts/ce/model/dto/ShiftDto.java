package kz.hts.ce.model.dto;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.util.UUID;

public class ShiftDto {

    private StringProperty id;
    private StringProperty fullName;
    private StringProperty dateOfOpenShift;
    private StringProperty dateOfCloseShift;
    private ObjectProperty<BigDecimal> averagePrice;
    private ObjectProperty<BigDecimal> sumOfChecks;
    private ObjectProperty<BigDecimal> profitability;
    private ObjectProperty<BigDecimal> costPrice;

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

    public String getDateOfOpenShift() {
        return dateOfOpenShift.get();
    }

    public StringProperty dateOfOpenShiftProperty() {
        return dateOfOpenShift;
    }

    public void setDateOfOpenShift(String dateOfOpenShift) {
        if (this.dateOfOpenShift == null) {
            this.dateOfOpenShift = new SimpleStringProperty();
        }
        this.dateOfOpenShift.set(dateOfOpenShift);
    }

    public String getDateOfCloseShift() {
        return dateOfCloseShift.get();
    }

    public StringProperty dateOfCloseShiftProperty() {
        return dateOfCloseShift;
    }

    public void setDateOfCloseShift(String dateOfCloseShift) {
        if (this.dateOfCloseShift == null) {
            this.dateOfCloseShift = new SimpleStringProperty();
        }
        this.dateOfCloseShift.set(dateOfCloseShift);
    }

    public BigDecimal getAveragePrice() {
        return averagePrice.get();
    }

    public ObjectProperty<BigDecimal> averagePriceProperty() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        if (this.averagePrice == null) {
            this.averagePrice = new SimpleObjectProperty<>();
        }
        this.averagePrice.set(averagePrice);
    }

    public BigDecimal getSumOfChecks() {
        return sumOfChecks.get();
    }

    public ObjectProperty<BigDecimal> sumOfChecksProperty() {
        return sumOfChecks;
    }

    public void setSumOfChecks(BigDecimal sumOfChecks) {
        if (this.sumOfChecks == null) {
            this.sumOfChecks = new SimpleObjectProperty<>();
        }
        this.sumOfChecks.set(sumOfChecks);
    }

    public BigDecimal getProfitability() {
        return profitability.get();
    }

    public ObjectProperty<BigDecimal> profitabilityProperty() {
        return profitability;
    }

    public void setProfitability(BigDecimal profitability) {
        if (this.profitability == null) {
            this.profitability = new SimpleObjectProperty<>();
        }
        this.profitability.set(profitability);
    }

    public BigDecimal getCostPrice() {
        return costPrice.get();
    }

    public ObjectProperty<BigDecimal> costPriceProperty() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        if (this.costPrice == null) {
            this.costPrice = new SimpleObjectProperty<>();
        }
        this.costPrice.set(costPrice);
    }
}
