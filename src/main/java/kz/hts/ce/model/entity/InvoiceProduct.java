package kz.hts.ce.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "invoice_product")
public class InvoiceProduct extends BaseEntity {

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "invoice_id", referencedColumnName = "id")
    private Invoice invoice;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "initial_price")
    private BigDecimal initialPrice;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    private int amount;
    private int version;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(BigDecimal price) {
        this.initialPrice = price;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
