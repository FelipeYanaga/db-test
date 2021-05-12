package org.acme.resteasyjackson;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

public class Main {

    @Inject
    EntityManager em;

    @Transactional
    public static void main(String[] args) throws IOException {
        Dish dish = new Dish("tomato","tomato");
        dish.persist();
    }

}
