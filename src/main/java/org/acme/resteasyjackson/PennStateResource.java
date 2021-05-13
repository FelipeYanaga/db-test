package org.acme.resteasyjackson;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
            UncScraper uncScraper = new UncScraper();
            List<String> urls = uncScraper.getMealUrls();
            List<String> ids = uncScraper.getPennStateRecipeIds(urls.get(0));
            dishes = uncScraper.getPennStateDishes(urls.get(0));
        }
        return Dish.listAll();
    }



}
