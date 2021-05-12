package org.acme.resteasyjackson;

import org.acme.resteasyjackson.Dish;
import org.acme.resteasyjackson.Scraper;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/csb")
public class CsbResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dish> getItems() throws IOException {
        Scraper scraper = new Scraper();
        return scraper.getCsbDishes();
    }

    public CsbResource(){}

}
