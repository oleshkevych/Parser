package com.parser.parsers.co.jobspresso;

import com.google.gson.*;
import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.util.DateUtil;
import org.apache.regexp.RE;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/13/2016.
 */
public class ParserJobspresso implements ParserMain {


    private String startLink = "https://jobspresso.co/browsejobs/#s=1";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserJobspresso() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

        try {
            String urlS = ("https://jobspresso.co/jm-ajax/get_listings/");
            URL url = new URL(urlS);
            BufferedReader r = new BufferedReader(new InputStreamReader(
                    ((HttpURLConnection) url.openConnection()).getInputStream()));

            JsonParser jp = new JsonParser();

            JsonElement jsonElement = jp.parse(r);

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            doc = Jsoup.parse(jsonObject.get("html").getAsString());
            Elements tables3 = doc.select(".job_listing");
            runParse(tables3, 0);

        } catch (IOException e) {
            e.printStackTrace();
            jobsInforms = null;
        }
    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        for (int i = counter; i < tables2.size(); i += 1) {
            datePublished = null;

            String stringDate = tables2.get(i).select("date").first().text() + " " + DateUtil.getCurrentYear();
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(tables2.get(i).select(".job_listing-location").first(),
                        tables2.get(i).select(".job_listing-title").first(),
                        tables2.get(i).select(".job_listing-company strong").first(),
                        datePublished,
                        tables2.get(i).select("a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.ownText());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(linkDescription.attr("href"));
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }
}
