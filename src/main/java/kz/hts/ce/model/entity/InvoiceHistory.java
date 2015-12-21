package kz.hts.ce.model.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "invoice_history")
public class InvoiceHistory extends BaseEntity{

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date date;

    private int postponement;
    private boolean vat;
    private int margin;
    private int version;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
