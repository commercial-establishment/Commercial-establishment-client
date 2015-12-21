package kz.hts.ce.model.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "warehouse_product_history")
public class WarehouseProductHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "warehouse_product_id", nullable = false)
    private WarehouseProduct warehouseProduct;
//
//    @ManyToOne
//    @JoinColumn(name = "provider_id", nullable = true)
//    private Provider provider;

    @Column(name = "date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date date;

    @Column(name = "initial_price")
    private BigDecimal initialPrice;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    private int arrival;
    private int version;
    private int residue;

    public WarehouseProduct getWarehouseProduct() {
        return warehouseProduct;
    }

    public void setWarehouseProduct(WarehouseProduct warehouseProduct) {
        this.warehouseProduct = warehouseProduct;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date saleDate) {
        this.date = saleDate;
    }

    public BigDecimal getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(BigDecimal initialPrice) {
        this.initialPrice = initialPrice;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getArrival() {
        return arrival;
    }

    public void setArrival(int amount) {
        this.arrival = amount;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getResidue() {
        return residue;
    }

    public void setResidue(int residue) {
        this.residue = residue;
    }
//
//    public Provider getProvider() {
//        return provider;
//    }
//
//    public void setProvider(Provider provider) {
//        this.provider = provider;
//    }
}
