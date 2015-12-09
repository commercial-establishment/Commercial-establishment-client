package kz.hts.ce.model.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Invoice extends BaseEntity {

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    private int postponement;
    private boolean vat;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    private int margin;

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public int getPostponement() {
        return postponement;
    }

    public void setPostponement(int postponement) {
        this.postponement = postponement;
    }

    public boolean isVat() {
        return vat;
    }

    public void setVat(boolean vat) {
        this.vat = vat;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "date=" + date +
                ", provider=" + provider +
                ", postponement=" + postponement +
                '}';
    }
}
