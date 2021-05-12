package org.acme.resteasyjackson;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Dish extends PanacheEntity {
    private String name;

    @Lob
    private String ingredients;
    private String allergens;
    private String dining;

    public Dish(String name, String ingredients){
        this.name = name;
        this.ingredients = ingredients;
    }

    public Dish(String name, String ingredients, String allergens, String dining){
        this.name = name;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.dining = dining;
    }

    public Dish() {

    }

    public static Dish of(String name, String ingredients){
        return new Dish(name, ingredients);
    }

    private Dish(Builder builder){
        this.name = builder.name;
        this.ingredients = builder.ingredients;
        this.allergens = builder.allergens;
        this.dining = builder.diningHall;
    }

    public String getName(){ return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }

    public String getIngredients(){return
     this.ingredients;
    }

    public String getDining(){ return this.dining;
    }

    public void setDining(String dining){
        this.dining = dining;
    }

    public void setAllergens(String allergens){
        this.allergens = allergens;
    }

    public String getAllergens(){return
            this.allergens;
    }



    //Builder
    public static class Builder {
        //Required
        private final String name;
        private final String ingredients;

        //Optional
        private String allergens;
        private String meal;
        private String diningHall;

        public Builder(String name, String ingredients){
            this.name = name;
            this.ingredients = ingredients;
        }

        public Builder allergens(String allergens){
            this.allergens = allergens;
            return this;
        }

        public Builder meal(String meal){
            this.meal = meal;
            return this;
        }

        public Builder diningHall(String diningHall){
            this.diningHall = diningHall;
            return this;
        }

        public Dish build(){
            return new Dish(this);
        }


    }



}
