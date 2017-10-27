package com.parser.parsers.com.eurojobs;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PrintDescription;
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
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserEurojobs implements ParserMain {

    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserEurojobs() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("https://www.eurojobs.com/search-results-jobs/?action=search&listing_type%5Bequal%5D=Job&keywords%5Ball_words%5D=Drupal+&Location%5Blocation%5D%5Bvalue%5D=&Location%5Blocation%5D%5Bradius%5D=10");
        startLinksList.add("https://www.eurojobs.com/search-results-jobs/?action=search&listing_type%5Bequal%5D=Job&keywords%5Ball_words%5D=Angular+&Location%5Blocation%5D%5Bvalue%5D=&Location%5Blocation%5D%5Bradius%5D=10");
        startLinksList.add("https://www.eurojobs.com/search-results-jobs/?action=search&listing_type%5Bequal%5D=Job&keywords%5Ball_words%5D=React+&Location%5Blocation%5D%5Bvalue%5D=&Location%5Blocation%5D%5Bradius%5D=10");
        startLinksList.add("https://www.eurojobs.com/search-results-jobs/?action=search&listing_type%5Bequal%5D=Job&keywords%5Ball_words%5D=Meteor+&Location%5Blocation%5D%5Bvalue%5D=&Location%5Blocation%5D%5Bradius%5D=10");
        startLinksList.add("https://www.eurojobs.com/search-results-jobs/?action=search&listing_type%5Bequal%5D=Job&keywords%5Ball_words%5D=Node+&Location%5Blocation%5D%5Bvalue%5D=&Location%5Blocation%5D%5Bradius%5D=10");
        startLinksList.add("https://www.eurojobs.com/search-results-jobs/?action=search&listing_type%5Bequal%5D=Job&keywords%5Ball_words%5D=Frontend+&Location%5Blocation%5D%5Bvalue%5D=&Location%5Blocation%5D%5Bradius%5D=10");
        startLinksList.add("https://www.eurojobs.com/search-results-jobs/?action=search&listing_type%5Bequal%5D=Job&keywords%5Ball_words%5D=JavaScript+&Location%5Blocation%5D%5Bvalue%5D=&Location%5Blocation%5D%5Bradius%5D=10");
        startLinksList.add("https://www.eurojobs.com/search-results-jobs/?action=search&listing_type%5Bequal%5D=Job&keywords%5Ball_words%5D=iOS+&Location%5Blocation%5D%5Bvalue%5D=&Location%5Blocation%5D%5Bradius%5D=10");
        startLinksList.add("https://www.eurojobs.com/search-results-jobs/?action=search&listing_type%5Bequal%5D=Job&keywords%5Ball_words%5D=mobile+&Location%5Blocation%5D%5Bvalue%5D=&Location%5Blocation%5D%5Bradius%5D=10");

        for (String link : startLinksList) {
            boolean isNext = true;
            int counter = 1;
            do
                try {
                    doc = Jsoup.connect(link + "&page=" + counter)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    Elements tables2 = doc.select(".searchResultsJobs tr");
                    isNext = runParse(tables2);
                    counter++;
                } catch (Exception e) {
                    e.printStackTrace();
                } while (isNext);
        }
    }

    private boolean runParse(Elements elements) {
        System.out.println("text date : " + jobsInforms.size());
        Date datePublished = null;
        int newCount = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        for (Element element : elements) {
            if (element.select("span").size() > 5) {
                String stringDate = element.select(".listing-info div").first().select("span").last().text();
                try {
                    datePublished = formatter.parse(stringDate);
                    newCount += objectGenerator(element.select(".listing-info div").first().select(".captions-field").first(),
                            element.select(".listing-title").first(),
                            element.select(".listing-info div").first().select(".captions-field").get(1),
                            datePublished,
                            element.select(".listing-title a").last());
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return dateClass.dateChecker(datePublished) && newCount != 0;
    }

    private int objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink(linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
            return 1;
        }
        return 0;
    }
}
