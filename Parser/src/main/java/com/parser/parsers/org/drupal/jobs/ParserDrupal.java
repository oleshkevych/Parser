package com.parser.parsers.org.drupal.jobs;

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
 * Created by rolique_pc on 12/7/2016.
 */
public class ParserDrupal implements ParserMain {

    private static final String START_LINK = "https://jobs.drupal.org/home?search_api_views_fulltext=&search_api_views_fulltext_1=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private DateGenerator dateClass;

    public ParserDrupal() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {

            Document doc = Jsoup.connect(START_LINK)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements tables2 = doc.select(".view-content .views-row");
            runParse(tables2);
            if(tables2.size() == 0){
                jobsInforms = null;
            }

            Date datePublished = null;
            int count = 1;
            int c = 0;
            do {
                try {

                    datePublished = null;
                    doc = Jsoup.connect(START_LINK + "&page=" + count)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    Elements tables1 = doc.select(".view-content .views-row");
                    datePublished = runParse(tables1);
                    count++;

                } catch (IOException e) {
                    e.printStackTrace();
                    c++;
                }

            } while (dateClass.dateChecker(datePublished) && jobsInforms.size() < 1000 && c < 5);

        } catch (IOException e) {
            e.printStackTrace();
            jobsInforms = null;
        }

    }

    private Date runParse(Elements tables2) {
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MM/dd/yyyy - HH:mm");
        for (Element element : tables2) {
            String stringDate = element.select(".timeago").text();
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(element.select(".location").first(),
                        element.select(".field-name-title").first(),
                        element.select(".field-name-field-company").first(),
                        datePublished,
                        element.select(".field-name-title a").first());
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
            jobsInform.setHeadPublication(headPost != null ? headPost.text() : "");
            jobsInform.setCompanyName(company != null ? company.ownText() : "");
            jobsInform.setPlace(place != null ? place.text() : "");
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }
}
