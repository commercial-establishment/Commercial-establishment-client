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

    @Column(name = "price_with_margin")
    private BigDecimal priceWithMargin;

    private int amount;
    private BigDecimal price;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceWithMargin() {
        return priceWithMargin;
    }

    public void setPriceWithMargin(BigDecimal priceWithMargin) {
        this.priceWithMargin = priceWithMargin;
    }
}
