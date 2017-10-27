package com.parser.parsers.com.randstad;

import com.google.gson.Gson;
import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PrintDescription;
import com.parser.parsers.jobs.landing.ParserLandingJobs;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/12/2016.
 */
public class ParserRandstad implements ParserMain {


    private String startLink = "https://www.randstad.com/jobs/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserRandstad() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("https://www.randstad.com/jobs/q-drupal/");
        startLinksList.add("https://www.randstad.com/jobs/q-angular/");
        startLinksList.add("https://www.randstad.com/jobs/q-react/");
        startLinksList.add("https://www.randstad.com/jobs/q-meteor/");
        startLinksList.add("https://www.randstad.com/jobs/q-node/");
        startLinksList.add("https://www.randstad.com/jobs/q-frontend/");
        startLinksList.add("https://www.randstad.com/jobs/q-javascript/");
        startLinksList.add("https://www.randstad.com/jobs/q-ios/");
        startLinksList.add("https://www.randstad.com/jobs/q-mobile/");

        for (String link : startLinksList) {
            try {


                // need http protocol
                doc = Jsoup.connect(link)
                        .validateTLSCertificates(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();
                Elements tables2 = doc.select("article");
                boolean isContinue = runParse(tables2);
                int count = 2;
                while (isContinue && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 200) {
                    try {


                        doc = Jsoup.connect(link + "page-" + count + "/")
                                .validateTLSCertificates(false)
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                                .timeout(5000)
                                .get();
                        Elements tables1 = doc.select("article");
                        isContinue = runParse(tables1);
                        count++;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean runParse(Elements tables2) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        int newJobs = 0;
        for (Element element : tables2) {
            String stringDate = element.select("time").attr("datetime");
            try {
                datePublished = formatter.parse(stringDate);
                newJobs += objectGenerator(element.select(".job-summary-small-location").first(),
                        element.select("header").first(),
                        datePublished,
                        element.select("a").last());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }

        }
        return dateClass.dateChecker(datePublished) && newJobs != 0;
    }

    private int objectGenerator(Element place, Element headPost, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName("Randstad Technologies");
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink("https://www.randstad.com" + linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
            return 1;
        }
        return 0;
    }
}
