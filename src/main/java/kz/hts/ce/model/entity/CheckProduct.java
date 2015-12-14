package kz.hts.ce.model.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "check_product")
public class CheckProduct extends BaseEntity{

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "warehouseproduct_id", referencedColumnName = "id")
    private WarehouseProduct warehouseProduct;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "check_id", referencedColumnName = "id")
    private Check check;

    public WarehouseProduct getWarehouseProduct() {
        return warehouseProduct;
    }

    public void setWarehouseProduct(WarehouseProduct warehouseProduct) {
        this.warehouseProduct = warehouseProduct;
    }

    public Check getCheck() {
        return check;
    }

    public void setCheck(Check check) {
        this.check = check;
    }
}
