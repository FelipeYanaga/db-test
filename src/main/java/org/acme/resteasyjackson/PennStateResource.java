package org.acme.resteasyjackson;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.io.IOException;
import java.util.List;

@Path("/psu")
public class PennStateResource {

    private List<Dish> dishes;

    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dish> getPennStateDishes() throws IOException {
        if (dishes == null){
            Scraper scraper = new Scraper();
            List<String> urls = scraper.getMealUrls();
            List<String> ids = scraper.getPennStateRecipeIds(urls.get(0));
            dishes = scraper.getPennStateDishes(urls.get(0));
        }
        return dishes;
    }



}
