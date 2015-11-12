package kz.hts.ce.model.entity;

import javax.persistence.Entity;

@Entity
public class Gender extends BaseEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Gender{" +
                "name='" + name + '\'' +
                '}';
    }
}
