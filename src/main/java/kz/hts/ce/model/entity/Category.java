package kz.hts.ce.model.entity;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;

@Entity
@Audited
public class Category extends BaseEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
