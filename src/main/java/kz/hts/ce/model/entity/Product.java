package kz.hts.ce.model.entity;

import org.hibernate.annotations.Proxy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Audited
@Proxy(lazy = false)
public class Product extends BaseEntity {

    private String name;
    private String barcode;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Unit unit;

    @Column(name = "is_blocked")
    private boolean blocked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", barcode='" + barcode + '\'' +
                ", category=" + category +
                ", unit=" + unit +
                ", blocked=" + blocked +
                '}';
    }
}
