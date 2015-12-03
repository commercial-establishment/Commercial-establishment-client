package kz.hts.ce.model.dto;

import javafx.beans.property.*;

import java.math.BigDecimal;

public class InvoiceDto {

    private LongProperty id;
    private StringProperty providerCompanyName;
    private StringProperty date;
    private IntegerProperty postponement;
    private BooleanProperty vat;
    private ObjectProperty<BigDecimal> totalPrice;

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

    public String getProviderCompanyName() {
        return providerCompanyName.get();
    }

    public StringProperty providerCompanyNameProperty() {
        return providerCompanyName;
    }

    public void setProviderCompanyName(String providerCompanyName) {
        if (this.providerCompanyName == null) {
            this.providerCompanyName = new SimpleStringProperty();
        }
        this.providerCompanyName.set(providerCompanyName);
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

    public int getPostponement() {
        return postponement.get();
    }

    public IntegerProperty postponementProperty() {
        return postponement;
    }

    public void setPostponement(int postponement) {
        if (this.postponement == null) {
            this.postponement = new SimpleIntegerProperty();
        }
        this.postponement.set(postponement);
    }

    public boolean getVat() {
        return vat.get();
    }

    public BooleanProperty vatProperty() {
        return vat;
    }

    public void setVat(boolean vat) {
        if (this.vat == null) {
            this.vat = new SimpleBooleanProperty();
        }
        this.vat.set(vat);
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
