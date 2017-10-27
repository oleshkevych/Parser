package com.parser.parsers.com.authenticjobs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PrintDescription;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserAuthenticjobs implements ParserMain {


    private String startLink = "https://authenticjobs.com";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserAuthenticjobs() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("https://authenticjobs.com/filter?page=1&query=Drupal");
        startLinksList.add("https://authenticjobs.com/filter?page=1&query=Angular");
        startLinksList.add("https://authenticjobs.com/filter?page=1&query=React");
        startLinksList.add("https://authenticjobs.com/filter?page=1&query=Meteor");
        startLinksList.add("https://authenticjobs.com/filter?page=1&query=Node");
        startLinksList.add("https://authenticjobs.com/filter?page=1&query=Frontend");
        startLinksList.add("https://authenticjobs.com/filter?page=1&query=JavaScript");
        startLinksList.add("https://authenticjobs.com/filter?page=1&query=iOS");
        startLinksList.add("https://authenticjobs.com/filter?page=1&query=mobile");

        int c = 0;
        for (String linkS : startLinksList) {

            int counter = 1;
            Date datePublished = null;
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");

            do
                try {

                    String urlS = (linkS /*+ counter*/);
                    URL url = new URL(urlS);
                    BufferedReader r = new BufferedReader(new InputStreamReader(
                            ((HttpURLConnection) url.openConnection()).getInputStream()));

                    JsonParser jp = new JsonParser();

                    JsonElement jsonElement = jp.parse(r);

                    r.close();
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    JsonArray jArray = (JsonArray) jsonObject.get("listings");
                    JsonObject jobJson;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    if (jArray.size() == 0) {
                        c++;
                    }
                    for (int i = 0; i < jArray.size(); i++) {
                        datePublished = null;
                        jobJson = (jArray.get(i).getAsJsonObject());
                        String title = jobJson.get("title").getAsString();
                        String place = jobJson.get("loc").getAsString();
                        String link = startLink + jobJson.get("url_relative").getAsString() + title.toLowerCase().replaceAll("  ", " ").replaceAll(" ", "-").replaceAll("[()/\"'.@,+-/*]", "");
                        String company = jobJson.get("company").getAsString();
                        String stringDate = jobJson.get("post_date_relative").getAsString();
                        if (stringDate.equals("Today") || stringDate.contains("minut") || stringDate.contains("hour")) {
                            datePublished = new Date();
                        } else if (stringDate.contains("Yesterday") || stringDate.contains("1 day")) {
                            datePublished = new Date(new Date().getTime() - 1 * 24 * 3600 * 1000);
                        } else {
                            datePublished = formatter.parse(stringDate + " " + calendar.get(Calendar.YEAR));
                            Calendar calendarPublished = Calendar.getInstance();
                            calendarPublished.setTime(datePublished);
                            if (calendar.get(Calendar.MONTH) < calendarPublished.get(Calendar.MONTH)) {
                                stringDate = stringDate + " " + (calendar.get(Calendar.YEAR) - 1);
                                datePublished = formatter.parse(stringDate);
                            }
                        }
                        counter++;

                        if (dateClass.dateChecker(datePublished) && jobsInforms.size() < startLinksList.indexOf(linkS) * 200) {
                            String content = "";
                            try {
                                URL url1 = new URL(link);
                                BufferedReader r1 = new BufferedReader(new InputStreamReader(
                                        ((HttpURLConnection) url1.openConnection()).getInputStream()));

                                while (r1.readLine() != null) {
                                    content += r1.readLine();
                                }

                                r1.close();
                            } catch (Exception e) {
                                datePublished = null;
                            }
                            doc = Jsoup.parse(content);
                            JobsInform jobsInform = new JobsInform();
                            jobsInform.setPublishedDate(datePublished);
                            jobsInform.setCompanyName(company);
                            jobsInform.setPlace(place);
                            jobsInform.setHeadPublication(title);
                            jobsInform.setPublicationLink(link);
                            if (!jobsInforms.contains(jobsInform)) {
                                jobsInforms.add(jobsInform);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    c++;
                }
            while (dateClass.dateChecker(datePublished) && jobsInforms.size() < startLinksList.indexOf(linkS) * 200 && counter < 5);
        }
        if (c == startLinksList.size()) {
            jobsInforms = null;
        }
    }
}
