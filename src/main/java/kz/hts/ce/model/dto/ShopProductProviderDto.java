package kz.hts.ce.model.dto;

import java.util.UUID;

public class ShopProductProviderDto {

    private UUID providerId;
    private UUID productProviderId;
    private UUID productId;
    private UUID shopId;
    private int residue;

    public UUID getProviderId() {
        return providerId;
    }

    public void setProviderId(UUID providerId) {
        this.providerId = providerId;
    }

    public UUID getProductProviderId() {
        return productProviderId;
    }

    public void setProductProviderId(UUID productProviderId) {
        this.productProviderId = productProviderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getResidue() {
        return residue;
    }

    public void setResidue(int residue) {
        this.residue = residue;
    }

    public UUID getShopId() {
        return shopId;
    }

    public void setShopId(UUID shopId) {
        this.shopId = shopId;
    }
}
