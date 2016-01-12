package kz.hts.ce.model.entity;

import org.hibernate.annotations.Proxy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;

@Entity
@Audited
@Proxy(lazy = false)
public class Category extends BaseEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
