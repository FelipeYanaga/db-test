package org.acme.resteasyjackson;

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

}
