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

    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Dish getPennStateDishes() throws IOException {
        Scraper scraper = new Scraper();
        List<String> urls = scraper.getMealUrls();
        List<String> ids = scraper.getPennStateRecipeIds(urls.get(0));
        Dish dish = scraper.getPennStateDish(ids.get(0));
        scraper.getPennStateDishes(urls.get(0));
        return dish;
    }



}
