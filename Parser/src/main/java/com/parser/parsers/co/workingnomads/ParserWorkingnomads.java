package com.parser.parsers.co.workingnomads;

import com.google.gson.*;
import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.jobs.landing.ParserLandingJobs;
import jdk.nashorn.internal.runtime.ParserException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/12/2016.
 */
public class ParserWorkingnomads implements ParserMain {

    private String startLink = "https://www.workingnomads.co/jobs";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserWorkingnomads() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date datePublished = null;
        int count = 0;
        do {
            try {
                datePublished = null;
                String urlS = "https://www.workingnomads.co/jobsapi/job/_search?sort=premium:desc,pub_date:desc&_source=company,category_name,description,instructions,id,external_id,slug,title,pub_date,tags,source,apply_url,premium&size=20&from=" + count;
                URL url = new URL(urlS);
                BufferedReader r = new BufferedReader(new InputStreamReader(
                        ((HttpURLConnection) url.openConnection()).getInputStream()));
                JsonParser jp = new JsonParser();
                JsonElement jsonElement = jp.parse(r);
                r.close();
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonObject jsonObject1 = jsonObject.getAsJsonObject("hits");
                JsonArray jArray = (JsonArray) jsonObject1.get("hits");
                JsonObject jobJson;
                for (int i = 0; i < jArray.size(); i++) {
                    try {
                        jobJson = (jArray.get(i).getAsJsonObject()).getAsJsonObject("_source");
                        String title = jobJson.get("title").getAsString();
                        String link = jobJson.get("apply_url").getAsString();
                        String company = jobJson.get("company").getAsString();
                        String stringDate = jobJson.get("pub_date").getAsString().substring(0, 10);

                        datePublished = formatter.parse(stringDate);
                        if (dateClass.dateChecker(datePublished) && jobsInforms.size() < 100) {
                            String description = jobJson.get("description").getAsString();
                            doc = Jsoup.parse(description);
                            Elements tablesDescription = doc.getAllElements();
                            JobsInform jobsInform = new JobsInform();
                            jobsInform.setPublishedDate(datePublished);
                            jobsInform.setHeadPublication(title);
                            jobsInform.setCompanyName(company);
                            jobsInform.setPlace("");
                            jobsInform.setPublicationLink(link);
                            List<ListImpl> list = new ArrayList<>();
                            ListImpl list1 = new ListImpl();
                            list1.setListHeader(title);
                            list.add(list1);
                            for (Element element : tablesDescription) {
                                if (element.tagName().equals("p")) {
                                    list.add(addParagraph(element));
                                } else if (element.tagName().equals("ul")) {
                                    list.add(addList(element));
                                } else if (element.tagName().contains("h")) {
                                    list.add(addHead(element));
                                }
                            }
                            if (list.size() < 3) {
                                ListImpl list2 = new ListImpl();
                                list2.setTextFieldImpl(tablesDescription.text());
                                list.add(list2);
                            }
                            list.add(null);

                            jobsInform.setOrder(list);
                            jobsInforms.add(jobsInform);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                count += 20;

            } catch (IOException e) {
                e.printStackTrace();
            }

        } while (dateClass.dateChecker(datePublished) && jobsInforms.size() < 100);

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
