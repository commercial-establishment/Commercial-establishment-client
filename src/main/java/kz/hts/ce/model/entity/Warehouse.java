package kz.hts.ce.model.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Warehouse extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
