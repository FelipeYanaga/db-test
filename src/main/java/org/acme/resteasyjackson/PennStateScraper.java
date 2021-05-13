package org.acme.resteasyjackson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PennStateScraper implements Scraper {

    // This automatically gets the daily meals so no need to change the date
    public List<String> getMealUrls() throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000).setConnectTimeout(1000).setSocketTimeout(1000).build();
        HttpGet get = new HttpGet("http://menu.hfs.psu.edu/shortmenu.aspx?sName=Penn+State+Housing+and+Food+Services&locationNum=11&locationName=East+Food+District&naFlag=1");
        HttpResponse response = client.execute(get);

        String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().parallel()
                .collect(Collectors.joining("\n"));

        Document doc = Jsoup.parse(content);
        Elements mealHeaders = doc.getElementsByClass("meal-header");

        List<String> mealNames = new LinkedList<>();

        Elements elements = mealHeaders.select("a");

        Formatter formatter = new Formatter();

        for (Element element : elements) {
            mealNames.add(formatter.getMealName(element.attr("href")));
        }

        return mealNames;
    }

    /*
    Must change the dates for this one, per meal and day
     */
    public List<String> getRecipeIds(String url) throws IOException{

        HttpClient client = HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000).setConnectTimeout(1000).setSocketTimeout(1000).build();
        // split string at the end of psu.edu/
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);

        String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().parallel()
                .collect(Collectors.joining("\n"));

        Document doc = Jsoup.parse(content);
        Elements mealHeaders = doc.getElementsByAttributeValue("name","recipe");

        List<String> recipeIds = new LinkedList<>();

        Formatter formatter = new Formatter();

        for (Element element : mealHeaders){
            recipeIds.add(formatter.formatPennStateId(element.attr("value")));
        }

        return recipeIds;
    }

    public Dish getDish(String id) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000).setConnectTimeout(1000).setSocketTimeout(1000).build();
        HttpGet get = new HttpGet("http://menu.hfs.psu.edu/label.aspx?locationNum=11&locationName=East+Food+District&dtdate=05%2f12%2f2021&RecNumAndPort=" + id);
        HttpResponse response = client.execute(get);

        String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().parallel()
                .collect(Collectors.joining("\n"));

        Document doc = Jsoup.parse(content);
        Elements names = doc.getElementsByClass("labelrecipe");
        Elements ingredients = doc.getElementsByClass("labelingredientsvalue");
        Elements allergens = doc.getElementsByClass("labelallergensvalue");

        return new Dish.Builder(names.text(),ingredients.text()).allergens(allergens.text()).build();
    }

    @Transactional
    public List<Dish> getDishes(String mealUrl) throws IOException {
        List<String> recipeIds = getRecipeIds(mealUrl);

        List<Dish> dishes = new LinkedList<>();

        for (String id : recipeIds) {
            Dish dish = getDish(id);
            dish.persist();
            dishes.add(dish);
        }

        return dishes;
    }





}
