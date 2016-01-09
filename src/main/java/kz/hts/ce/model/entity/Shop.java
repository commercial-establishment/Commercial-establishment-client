package kz.hts.ce.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Shop extends BaseEntity {

    private String name;
    private String address;
    private int iin;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shop")
    private List<Employee> employees;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shop")
    private List<Check> checks;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @OneToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;/*TODO add area entity*/

    @Column(name = "is_blocked", nullable = false)
    private boolean blocked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getIin() {
        return iin;
    }

    public void setIin(int iin) {
        this.iin = iin;
    }

    public List<Check> getChecks() {
        return checks;
    }

    public void setChecks(List<Check> checks) {
        this.checks = checks;
    }
}
