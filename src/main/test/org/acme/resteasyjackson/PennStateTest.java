package org.acme.resteasyjackson;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PennStateTest {

        private Scraper scraper;
        private Formatter formatter;

        @BeforeEach
        public void beforeEach(){
            scraper = new PennStateScraper();
            formatter = new Formatter();
        }

        @AfterEach
        public void afterEach(){
            scraper = null;
            formatter = null;
        }

        @Test
        public void testGetMealNames() throws IOException {
            List<String> names = new ArrayList<>();
            names.add("Breakfast");
            names.add("Lunch");
            names.add("Dinner");
            List<String> result = scraper.getMealUrls();
            System.out.println(Arrays.toString(result.toArray()));
            assert(Arrays.equals(names.toArray(),result.toArray()));
        }


}
