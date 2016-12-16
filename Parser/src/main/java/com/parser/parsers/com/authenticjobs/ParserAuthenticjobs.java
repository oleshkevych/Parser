package com.parser.parsers.com.authenticjobs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
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

        int counter = 1;
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");

        do

        try {
//            System.out.println("text date : ParserJobspresso" + jobsInforms.size());
            String urlS = ("https://authenticjobs.com/filter?page=" + counter);
            URL url = new URL(urlS);
            BufferedReader r = new BufferedReader(new InputStreamReader(
                    ((HttpURLConnection) url.openConnection()).getInputStream()));

            JsonParser jp = new JsonParser();

            JsonElement jsonElement = jp.parse(r);

            r.close();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray jArray = (JsonArray) jsonObject.get("listings");
            JsonObject jobJson;
            for (int i = 0; i < jArray.size(); i++) {
                datePublished = null;
                jobJson = (jArray.get(i).getAsJsonObject());
                String title = jobJson.get("title").getAsString();
                String place = jobJson.get("loc").getAsString();
                String link = startLink + jobJson.get("url_relative").getAsString() + title.toLowerCase().replaceAll("  ", " ").replaceAll(" ", "-").replaceAll("[()/\"'.@]", "");
                String company = jobJson.get("company").getAsString();
                String stringDate = jobJson.get("post_date_relative").getAsString();
                if (stringDate.equals("Today")||stringDate.contains("minut") || stringDate.contains("hour")) {
                    datePublished = new Date();
                } else if (stringDate.contains("Yesterday") || stringDate.contains("1 day")) {
                    datePublished = new Date(new Date().getTime() - 1 * 24 * 3600 * 1000);
                } else {
                    datePublished = formatter.parse(stringDate + " " + 2016);
                }
//                System.out.println("text date : " + link);
//                System.out.println("text date : " + stringDate);
//                System.out.println("text date : " + datePublished);

                if (dateClass.dateChecker(datePublished) && jobsInforms.size() < 50) {

                    URL url1 = new URL(link);
                    BufferedReader r1 = new BufferedReader(new InputStreamReader(
                            ((HttpURLConnection) url1.openConnection()).getInputStream()));
                    String content = "";
                    while (r1.readLine() != null) {
                        content += r1.readLine();
                    }
                    r1.close();
                    doc = Jsoup.parse(content);
                    JobsInform jobsInform = new JobsInform();
                    jobsInform.setPublishedDate(datePublished);
                    jobsInform.setCompanyName(company);
                    jobsInform.setPlace(place);
                    jobsInform.setHeadPublication(title);
                    jobsInform.setPublicationLink(link);
                    jobsInform = getDescription(content, jobsInform);
                    if(!jobsInforms.contains(jobsInform)) {
                        jobsInforms.add(jobsInform);
                    }
                }
            }

            counter++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (dateClass.dateChecker(datePublished) && jobsInforms.size() < 50);

    }

    public static JobsInform getDescription(String description, JobsInform jobsInform) {

        try {
            Document document = Jsoup.parse(description);
//            System.out.println("text date : " + document);

            Elements tablesDescription = document.select(".description");
                    if(tablesDescription.size() > 0){
                    tablesDescription = tablesDescription.first().children();

                    }
            Elements tablesHead = document.select("h1");
            List<ListImpl> list = new ArrayList<ListImpl>();
//            System.out.println("text date : ParserJobspresso" + tablesHead.first().text());


            if(tablesHead.first()!=null) {
                list.add(addHead(tablesHead.first()));
            }
            for (int i = 0; i < tablesDescription.size(); i++) {
                if(tablesDescription.get(i).tagName().equals("div")){
                    for (Element e : tablesDescription.get(i).children()){
                        if (e.tagName().equals("p")) {
                            list.add(addParagraph(e));
                        } else if (e.tagName().equals("ul")) {
                            list.add(addList(e));
                        } else if (e.tagName().contains("h")) {
                            list.add(addHead(e));
                        }
                    }
                }else
                if (tablesDescription.get(i).tagName().equals("p")) {
                    list.add(addParagraph(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().equals("ul")) {
                    list.add(addList(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().contains("h")) {
                    list.add(addHead(tablesDescription.get(i)));
                }
            }

            list.add(null);
            jobsInform.setOrder(list);


            return jobsInform;
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage() + " " + jobsInform.getPublicationLink());
            e.printStackTrace();
            return jobsInform;
        }

    }

    private static ListImpl addHead(Element element) {
        ListImpl list = new ListImpl();
        list.setListHeader(element.text());
        return list;
    }

    private static ListImpl addParagraph(Element element) {
        if (element.select("strong").size() > 0) {
            return addHead(element.select("strong").first());
        } else {


            ListImpl list = new ListImpl();
            list.setTextFieldImpl(element.text());
            return list;
        }
    }

    private static ListImpl addList(Element element) {
        ListImpl list = new ListImpl();
        List<String> strings = new ArrayList<String>();
        for (Element e : element.getAllElements()) {
            strings.add(e.text());
        }
        list.setListItem(strings);
        return list;
    }
}
