package org.acme.resteasyjackson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class StringFormatterTest {

    private Formatter formatter;

    @BeforeEach
    public void beforeEach(){
        formatter = new Formatter();
    }

    @Test
    public void testGetMealName(){
        String url = "http://menu.hfs.psu.edu/longmenu.aspx?sName=Penn+State+Housing+and+Food+Services&locationNum=11&locationName=East+Food+District&naFlag=1&WeeksMenus=This+Week%27s+Menus&dtdate=5%2f15%2f2021&mealName=Lunch";
        String result = formatter.getMealName(url);
        assert(result.equalsIgnoreCase("Lunch"));
    }

    @Test
    public void testGetNowDate(){
        assert(formatter.getNowDate().equalsIgnoreCase("5%2f13%2f2021"));
    }

    @Test
    public void testUrlFormatter(){
        String url = "http://menu.hfs.psu.edu/longmenu.aspx?sName=Penn+State+Housing+and+Food+Services&locationNum=11&locationName=East+Food+District&naFlag=1&WeeksMenus=This+Week%27s+Menus&dtdate=5%2f13%2f2021&mealName=Lunch";
        String compare = formatter.createPennStateUrl(LocalDate.now(),"Lunch");
        assert(url.equalsIgnoreCase(compare));
    }


}
