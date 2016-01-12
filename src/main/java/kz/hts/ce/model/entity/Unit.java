package kz.hts.ce.model.entity;

import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;

@Entity
@Proxy(lazy = false)
public class Unit extends BaseEntity {

    private String name;
    private String symbol;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
