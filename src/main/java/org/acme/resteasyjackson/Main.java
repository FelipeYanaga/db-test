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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        Scraper scraper = new PennStateScraper();
        LocalDate now = LocalDate.now();
        Formatter formatter = new Formatter();
        String url = formatter.createPennStateUrl(now, "Lunch");
        List<Dish> dishes = scraper.getDishes(url);

        System.out.println("A");
    }

}
