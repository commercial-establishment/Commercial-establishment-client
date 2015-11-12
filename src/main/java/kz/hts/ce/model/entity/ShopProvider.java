package kz.hts.ce.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "shop_provider")
public class ShopProvider extends BaseEntity {

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "provider_id", referencedColumnName = "id")
    private Provider provider;

    @Column(name = "is_blocked")
    private boolean blocked;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}

