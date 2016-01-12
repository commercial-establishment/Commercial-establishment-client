package kz.hts.ce.model.entity;

import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;

@Entity
@Proxy(lazy = false)
public class City extends BaseEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
