package com.parser.parsers.dk.jobbank;


import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/9/2016.
 */
public class ParserJobbank implements ParserMain {

    private static final String START_LINK = "http://jobbank.dk/job";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private DateGenerator dateClass;

    public ParserJobbank() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("http://jobbank.dk/job/?act=find&key=Drupal&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=Angular&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=React&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=Meteor&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=Node&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=Frontend&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=JavaScript&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=iOS&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=mobile&virk=&oprettet=");

        int c = 0;
        for (String link : startLinksList) {
            try {

                Document doc = Jsoup.connect(link)
                        .validateTLSCertificates(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();

                Elements tables2 = doc.select(".job-item-container");
                runParse(tables2);

                Date datePublished = null;
                int count = 2;
                int ec = 0;
                do {
                    try {

                        datePublished = null;
                        doc = Jsoup.connect(link + "?&page=" + count)
                                .validateTLSCertificates(false).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();

                        Elements tables1 = doc.select(".job-item-container");
                        datePublished = runParse(tables1);
                        count++;
                        ec = tables2.size();

                    } catch (IOException e) {
                        e.printStackTrace();
                        ec++;
                    }

                } while (dateClass.dateChecker(datePublished) && jobsInforms.size() < 2000 && ec != 0);

            } catch (IOException e) {
                e.printStackTrace();
                c++;
            }
        }
    }

    private Date runParse(Elements tables2) {
        System.out.println("text date : " + jobsInforms.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        for (Element element : tables2) {
            String stringDate = element.select(".job-date-updated").text().replace("Dato:", "");
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(element.select(".job-teaser").first(),
                        element.select(".job-header").first(),
                        element,
                        datePublished, element.select(".job-item").first());
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
            jobsInform.setPlace(place.text().replace("Fuldtidsjob hos ", ""));
            jobsInform.setCompanyName(place.text().replace("Fuldtidsjob hos ", ""));
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setPublicationLink(START_LINK.substring(0, START_LINK.lastIndexOf("job")) + linkDescription.attr("href"));
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }
}
