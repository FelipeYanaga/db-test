package org.acme.resteasyjackson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Formatter {

    public Formatter(){

    }

    public String formatIngredients(String s){
        Scanner scanner = new Scanner(s);
        String formattedString = "";
        boolean ingredients = true;
        String word;
        int i = 0;
        while(i < 2){
            word = scanner.next();
            if (word.contains("<") || word.contains(">")){
                ingredients = false;
                if (word.contains(".<")){
                    formattedString = formattedString + word.split(".<")[0];
                }
                i++;
            }
            if (ingredients){
                formattedString = formattedString + " " + word;
            }
            ingredients = true;
        }

        return formattedString;
    }

    public String formatName(String s){
            String[] splitString = s.split("<");
            return splitString[0];
    }

    public String formatPennStateId(String id){
        String[] splitString = id.split("\\*");
        return splitString[0] + "*" + splitString[1];
    }

    public String getMealName(String url){
        String[] splitString = url.split("=");
        return splitString[splitString.length - 1];
    }

    public String getNowDate(){
        LocalDate now = LocalDate.now();
        return now.getMonthValue() + "%2f" + now.getDayOfMonth() + "%2f" + now.getYear();
    }

    public String getFormattedDate(LocalDate date){
        return date.getMonthValue() + "%2f" + date.getDayOfMonth() + "%2f" + date.getYear();
    }

    public String createPennStateUrl(LocalDate date, String meal){
        String dateString = getFormattedDate(date);
        return "http://menu.hfs.psu.edu/longmenu.aspx?sName=Penn+State+Housing+and+Food+Services&locationNum=11&locationName=East+Food+District&naFlag=1&WeeksMenus=This+Week%27s+Menus&dtdate=" + dateString + "&mealName=" + meal;
    }
}
