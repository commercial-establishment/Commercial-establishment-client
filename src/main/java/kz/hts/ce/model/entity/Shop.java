package kz.hts.ce.model.entity;

import org.hibernate.annotations.Proxy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Audited
@Proxy(lazy = false)
public class Shop extends BaseEntity {

    private int iin;

    @NotEmpty
    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String name;

    @NotEmpty
    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String address;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Type type;

    @OneToOne
    @JoinColumn(name = "area_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Area area;

    @Column(name = "lock_date")/*TODO nullable = false*/
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date lockDate;

    @Column(name = "is_blocked", nullable = false)
    private boolean blocked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public int getIin() {
        return iin;
    }

    public void setIin(int iin) {
        this.iin = iin;
    }

    public Date getLockDate() {
        return lockDate;
    }

    public void setLockDate(Date lockDate) {
        this.lockDate = lockDate;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "blocked=" + blocked +
                ", area=" + area +
                ", type=" + type +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", iin=" + iin +
                '}';
    }
}
