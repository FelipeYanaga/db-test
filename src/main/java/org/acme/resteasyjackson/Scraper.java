package org.acme.resteasyjackson;

import java.io.IOException;
import java.util.List;

public interface Scraper {

    public Dish getDish(String id) throws IOException;

    public List<Dish> getDishes (String mealUrl) throws IOException;

    public List<String> getRecipeIds(String urlEndPoint) throws IOException;

    public List<String> getMealUrls() throws IOException;

}
