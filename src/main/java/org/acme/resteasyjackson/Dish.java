package org.acme.resteasyjackson;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Dish extends PanacheEntity {
    private int id;
    private String name;
    private String attribute;

    public String getName(){
        return this.name.toUpperCase();
    }

    public void setName(String name){
        this.name = name.toLowerCase();
    }

    public String getAttribute(){
        return this.attribute.toUpperCase();
    }

    public void setAttribute(){
        this.attribute = attribute.toLowerCase();
    }

}
