package org.acme.resteasyjackson;

public class Dish {
    private final String name;
    private final String ingredients;
    private String allergens;
    private String dining;


    public Dish(String name, String ingredients){
        this.name = name;
        this.ingredients = ingredients;
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

    public String getIngredients(){return
     this.ingredients;
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
