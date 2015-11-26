package kz.hts.ce.model.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "waybill_warehouse_product")
public class WaybillWarehouseProduct extends BaseEntity {

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "waybill_id", referencedColumnName = "id")
    private Waybill waybill;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "warehouse_product_id", referencedColumnName = "id")
    private WarehouseProduct warehouseProduct;

    public Waybill getWaybill() {
        return waybill;
    }

    public void setWaybill(Waybill waybill) {
        this.waybill = waybill;
    }

    public WarehouseProduct getWarehouseProduct() {
        return warehouseProduct;
    }

    public void setWarehouseProduct(WarehouseProduct warehouseProduct) {
        this.warehouseProduct = warehouseProduct;
    }
}
