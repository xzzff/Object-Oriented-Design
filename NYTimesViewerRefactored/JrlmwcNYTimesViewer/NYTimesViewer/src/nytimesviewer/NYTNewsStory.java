/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nytimesviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.*;

/**
 *
 * @author joe
 */
public class NYTNewsStory {
    public String webUrl;
    public String headline;
    public String snippet;
    public String leadParagraph;
    public String newsDesk;
    public String sectionName;
    public String source;
    
    // Things that were in the Manager class. Using my own API key.
    private static String urlString;
    private static final String BASE_URL_STRING = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static final String API_KEY = "878b67b09dfb3a552d42e7e4580eb223:3:74605446";
    
    // Never used searchString since we moved this to the Controller.
    private static URL url;
    private static List<NYTNewsStory> newsStories;
    
    public NYTNewsStory(String webUrl, String headline, String snippet, 
            String leadParagraph, String newsDesk, String sectionName, String source ) {
        this.webUrl = webUrl;
        this.headline = headline;
        this.snippet = snippet;
        this.leadParagraph = leadParagraph;
        this.newsDesk = newsDesk;
        this.sectionName = sectionName;
    }
    
    public static void load(String searchString) throws Exception {
        newsStories = new ArrayList<>();
        
        if (searchString == null || searchString.isEmpty()) {
            throw new Exception("The search string was empty.");
        }
        
        // create the urlString
        String encodedSearchString;
        try {
            // searchString must be URL encoded to put in URL
            encodedSearchString = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw ex;
        }
        
        urlString = BASE_URL_STRING + "?q=" + encodedSearchString + "&api-key=" + API_KEY;
        
        try {
            url = new URL(urlString);
        } catch(MalformedURLException muex) {
           throw muex;
        }
        
        String jsonString = "";
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(url.openStream()))){
            String inputLine;
            while ((inputLine = br.readLine()) != null)
                jsonString += inputLine;
        } catch (IOException ioex) {
            throw ioex;
        }
        
        try {
            parseJsonNewsFeed(jsonString);
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    private static void parseJsonNewsFeed(String jsonString) throws Exception {
        // start with clean list
        getNewsStories().clear();
       
        if (jsonString == null || jsonString.isEmpty()) return;
               
        JSONObject jsonObj;
        try {
            System.out.println("About to parse JSON string");
            jsonObj = (JSONObject)JSONValue.parse(jsonString);
        } catch (Exception ex) {
            throw ex;
        }
        
        if (jsonObj == null) return;
        
        String status = "";
        try {
            status = (String)jsonObj.get("status");
        } catch (Exception ex) {
            throw ex;
        }
        
        if (status == null || !status.equals("OK")) {
            throw new Exception("Status returned from API was not OK.");
        }
        
        JSONObject response;
        try {
            response = (JSONObject)jsonObj.get("response");
        } catch (Exception ex) {
            throw ex;
        }
        
        JSONArray docs;
        try {
            docs = (JSONArray)response.get("docs");
        } catch (Exception ex) {
            throw ex;
        }
      
        for (Object doc : docs) {
            try {
                JSONObject story = (JSONObject)doc;
                String webUrl = (String)story.getOrDefault("web_url", "");
                String snippet = (String)story.getOrDefault("snippet", "");
                String leadParagraph = (String)story.getOrDefault("lead_paragraph", "");
                String source = (String)story.getOrDefault("source", "");
                String newsDesk = (String)story.getOrDefault("news_desk", "");
                String sectionName = (String)story.getOrDefault("section_name", "");
                JSONObject headlineObj = (JSONObject)story.getOrDefault("headline", null);
                String headline = "";
                if (headlineObj != null) {
                    headline = (String)headlineObj.getOrDefault("main", "");
                }
                
                System.out.println("headline: " + headline + "\n");
                System.out.println("webUrl: " + webUrl + "\n");
                System.out.println("snippet: " + snippet + "\n");
                System.out.println("leadParagraph: " + leadParagraph + "\n");
                System.out.println("newsDesk: " + newsDesk + "\n");
                System.out.println("sectionName: " + sectionName + "\n");
                System.out.println("source: " + source + "\n");
                System.out.println("------------------------------------------------------\n");
                
                NYTNewsStory newsStory = new NYTNewsStory(webUrl, headline, 
                    snippet, leadParagraph, newsDesk, sectionName, source);
                getNewsStories().add(newsStory);               
            } catch (Exception ex) {
                throw ex;
            }
            
        }
        
    }
    
    public static List<NYTNewsStory> getNewsStories() {
        return newsStories;
    }
    
    public int getNumNewsStories() {        
        return getNewsStories().size();
    }   
}
