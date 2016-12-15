package com.parser.parsers.co.jobspresso;

import com.google.gson.*;
import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
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

        Date datePublished = null;
//        do

        try {
//            System.out.println("text date : ParserJobspresso" + jobsInforms.size());
            datePublished = null;
            String urlS = ("https://jobspresso.co/jm-ajax/get_listings/");
            URL url = new URL(urlS);
            BufferedReader r = new BufferedReader(new InputStreamReader(
                    ((HttpURLConnection) url.openConnection()).getInputStream()));

            JsonParser jp = new JsonParser();

            JsonElement jsonElement = jp.parse(r);

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            doc = Jsoup.parse(jsonObject.get("html").getAsString());
            Elements tables3 = doc.select(".job_listing");
            datePublished = runParse(tables3,/* jobsInforms.size()*/0);
//            System.out.println("text date : ParserJobspresso" + jobsInforms.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
// while (dateClass.dateChecker(datePublished) && jobsInforms.size() < 100);

    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        for (int i = counter; i < tables2.size(); i += 1) {
            datePublished = null;
            String stringDate = tables2.get(i).select(".job_listing-date").text();
            if (stringDate.contains("minut") || stringDate.contains("hour")) {
                datePublished = new Date();
            } else if (stringDate.contains("yesterday") || stringDate.contains("1 day")) {
                datePublished = new Date(new Date().getTime() - 1 * 24 * 3600 * 1000);
            } else if (stringDate.contains("2 day")) {
                datePublished = new Date(new Date().getTime() - 2 * 24 * 3600 * 1000);
            } else if (stringDate.contains("3 day")) {
                datePublished = new Date(new Date().getTime() - 3 * 24 * 3600 * 1000);
            } else if (stringDate.contains("4 day")) {
                datePublished = new Date(new Date().getTime() - 4 * 24 * 3600 * 1000);
            } else if (stringDate.contains("5 day")) {
                datePublished = new Date(new Date().getTime() - 5 * 24 * 3600 * 1000);
            } else if (stringDate.contains("6 day")) {
                datePublished = new Date(new Date().getTime() - 6 * 24 * 3600 * 1000);
            }
            objectGenerator(tables2.get(i).select(".job_listing-location").first(), tables2.get(i).select(".job_listing-title").first(),
                    tables2.get(i).select(".job_listing-company strong").first(), datePublished, tables2.get(i).select("a").first());
        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished) && jobsInforms.size() < 100) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.ownText());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(linkDescription.attr("href"));
            jobsInform = getDescription(linkDescription.attr("href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(true)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements tablesDescription = document.select("[itemprop='description']").first().children();
            Elements tablesHead = document.select(".page-title");
            List<ListImpl> list = new ArrayList<ListImpl>();


            list.add(addHead(tablesHead.first()));
            for (int i = 0; i < tablesDescription.size(); i++) {
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
