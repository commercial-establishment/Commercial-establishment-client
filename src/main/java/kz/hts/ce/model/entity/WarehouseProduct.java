package kz.hts.ce.model.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "warehouse_product")
public class WarehouseProduct extends BaseEntity {

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "warehouse_id", referencedColumnName = "id")
    private Warehouse warehouse;
//
//    /*TODO nullable = false*/
//    @Column(name = "import_date", nullable = true)
//    @DateTimeFormat(pattern = "dd-MM-yyyy")
//    private Date importDate;

    @Column(name = "initial_price")
    private BigDecimal initialPrice;


    private int version;
    private int arrival;
    private int residue;
    private BigDecimal price;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(BigDecimal initialPrice) {
        this.initialPrice = initialPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getArrival() {
        return arrival;
    }

    public void setArrival(int arrival) {
        this.arrival = arrival;
    }

    public int getResidue() {
        return residue;
    }

    public void setResidue(int residue) {
        this.residue = residue;
    }
}
