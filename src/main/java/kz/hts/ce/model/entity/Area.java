package kz.hts.ce.model.entity;

import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Proxy(lazy = false)
public class Area extends BaseEntity {

    private String name;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
