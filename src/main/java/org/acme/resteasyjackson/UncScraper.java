package org.acme.resteasyjackson;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;


import javax.sound.midi.SysexMessage;
import javax.swing.*;
import javax.swing.text.StyledDocument;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class UncScraper {
    private Map<String, Dish> dishes = new HashMap<String, Dish>();

    public String[] test (String item) throws IOException {

        String url = "";
        if (item.equalsIgnoreCase("biscuit")){
            url = "https://www.chick-fil-a.com/menu/chick-fil-a-chicken-biscuit";
        }
        else{
            url = "https://www.chick-fil-a.com/menu/chick-fil-a-chicken-sandwich";
        }

        HttpClient client = HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000).setConnectTimeout(1000).setSocketTimeout(1000).build();
        HttpGet get = new HttpGet(url);
        get.setConfig(requestConfig);
        HttpResponse response = client.execute(get);

        String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().parallel()
                .collect(Collectors.joining("\n"));

        Document doc = Jsoup.parse(content);
        Elements links = doc.getElementsByClass("p-nutri__content").select("p");

        String[] s = links.text().split(",");

        return s;

    }

    public List<Dish> unc() throws IOException{
        String url = "https://dining.unc.edu/locations/chase/?date=2021-04-27";
        HttpClient client = HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000).setConnectTimeout(1000).setSocketTimeout(1000).build();
        HttpGet get = new HttpGet(url);
        get.setConfig(requestConfig);
        HttpResponse response = client.execute(get);

        String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().parallel()
                .collect(Collectors.joining("\n"));

        Document doc = Jsoup.parse(content);
        Elements links = doc.getElementsByAttribute("data-searchable");

        List<Dish> list = new ArrayList<>();

        for (Element link : links){
            String ingredients = link.attr("data-searchable");
            String ingredientName = link.selectFirst("a").text();
            dishes.put(ingredientName,Dish.of(ingredientName, ingredients));
            list.add(Dish.of(ingredientName, ingredients));
        }

        return list;
    }

    public Dish getDish(int recipeId) throws IOException{
        String url = "https://dining.unc.edu/wp-content/themes/nmc_dining/ajax-content/recipe.php?recipe=" + recipeId;
        HttpClient client = HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(2000).setConnectTimeout(2000).setSocketTimeout(2000).build();
        HttpGet get = new HttpGet(url);
        get.setConfig(requestConfig);
        HttpResponse response = client.execute(get);

        String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().parallel()
                .collect(Collectors.joining("\n"));

        Document doc = Jsoup.parse(content);

        Element dishName = doc.selectFirst("h2");
        Elements links = doc.select("p").select("strong");

        Formatter formatter = new Formatter();

        return Dish.of(formatter.formatName(dishName.ownText()),formatter.formatIngredients(links.text()));
    }

    public List<Integer> getRecipeIds() throws IOException{
        String url = "https://dining.unc.edu/locations/chase/?date=2021-04-27";
        HttpClient client = HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000).setConnectTimeout(1000).setSocketTimeout(1000).build();
        HttpGet get = new HttpGet(url);
        get.setConfig(requestConfig);
        HttpResponse response = client.execute(get);

        String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().parallel()
                .collect(Collectors.joining("\n"));

        Document doc = Jsoup.parse(content);
        Elements links = doc.getElementsByAttribute("data-searchable");

        List<Integer> ids = new ArrayList<>();

        for (Element link : links){
            String id = link.getElementsByAttribute("data-recipe").attr("data-recipe");
            ids.add(Integer.parseInt(id));
        }

        return ids;
    }

    public List<Dish> getDishes() throws IOException{
        List<Integer> ids = getRecipeIds();
        List<Dish> dishes = new ArrayList<>();
        int i = 0;
        while(i < 20){
            dishes.add(getDish(ids.get(i)));
            i++;
        }
        return dishes;
    }

    public Dish getCsbDish(String recipeId) throws IOException{
        HttpClient client = HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000).setConnectTimeout(1000).setSocketTimeout(1000).build();
        HttpGet get = new HttpGet("http://csbmenu.csbsju.edu/mobile/Detail?menu=48173&" + recipeId);
        HttpResponse response = client.execute(get);

        String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().parallel()
                .collect(Collectors.joining("\n"));

        Document doc = Jsoup.parse(content);
        Elements ingredientsDiv = doc.getElementsByClass("ingredients").select("p");
        Elements name = doc.getElementsByClass("nutrition-h2");
        Element ingredients = ingredientsDiv.first();
        Element allergens = ingredientsDiv.last();
        return new Dish.Builder(name.text(), ingredients.text()).allergens(allergens.text()).build();
    }

    public List<String> getCsbIds() throws IOException{
        HttpClient client = HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000).setConnectTimeout(1000).setSocketTimeout(1000).build();
        HttpGet get = new HttpGet("http://csbmenu.csbsju.edu/mobile/Foods?menu=48173");
        HttpResponse response = client.execute(get);

        String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().parallel()
                .collect(Collectors.joining("\n"));

        Document doc = Jsoup.parse(content);
        Elements dishIds = doc.getElementsByClass("food-item-add-link").select("a");

        List<String> recipeIds = new LinkedList<>();
        for (Element id : dishIds){
            String a = id.attr("href").toString();
            String[] splitString = a.split("&");
            recipeIds.add(splitString[1]);
        }

        return recipeIds;
    }

    public List<Dish> getCsbDishes() throws IOException{
        List<Dish> dishes = new LinkedList<>();
        List<String> ids  = this.getCsbIds();
        for (String id : ids){
            dishes.add(this.getCsbDish(id));
        }

        return dishes;
    }

    public List<String> getMealUrls() throws IOException{

        HttpClient client = HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000).setConnectTimeout(1000).setSocketTimeout(1000).build();
        HttpGet get = new HttpGet("http://menu.hfs.psu.edu/shortmenu.aspx?sName=Penn+State+Housing+and+Food+Services&locationNum=11&locationName=East+Food+District&naFlag=1");
        HttpResponse response = client.execute(get);

        String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().parallel()
                .collect(Collectors.joining("\n"));

        Document doc = Jsoup.parse(content);
        Elements mealHeaders = doc.getElementsByClass("meal-header");

        List<String> mealUrls = new LinkedList<>();

        Elements elements = mealHeaders.select("a");

        for (Element element : elements){
            mealUrls.add(element.attr("href"));
        }

        return mealUrls;
    }

    public List<String> getPennStateRecipeIds(String urlEndPoint) throws IOException{

        HttpClient client = HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000).setConnectTimeout(1000).setSocketTimeout(1000).build();
        // split string at the end of psu.edu/
        HttpGet get = new HttpGet("http://menu.hfs.psu.edu/" + urlEndPoint);
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

    public Dish getPennStateDish(String id) throws IOException {
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
    public List<Dish> getPennStateDishes(String mealUrl) throws IOException {
        List<String> recipeIds = getPennStateRecipeIds(mealUrl);

        List<Dish> dishes = new LinkedList<>();

        for (String id : recipeIds) {
            Dish dish = getPennStateDish(id);
            dish.persist();
            dishes.add(dish);
        }

        return dishes;
    }

}
