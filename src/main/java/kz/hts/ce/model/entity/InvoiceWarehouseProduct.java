package kz.hts.ce.model.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "invoice_warehouse_product")
public class InvoiceWarehouseProduct extends BaseEntity {

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "invoice_id", referencedColumnName = "id")
    private Invoice invoice;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "warehouse_product_id", referencedColumnName = "id")
    private WarehouseProduct warehouseProduct;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public WarehouseProduct getWarehouseProduct() {
        return warehouseProduct;
    }

    public void setWarehouseProduct(WarehouseProduct warehouseProduct) {
        this.warehouseProduct = warehouseProduct;
    }
}
