package kz.hts.ce.model.entity;

import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;

@Entity
@Proxy(lazy = false)
public class Role extends BaseEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                '}';
    }
}
