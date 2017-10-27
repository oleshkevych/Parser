package com.parser.parsers.de.drupalcenter;

import com.parser.entity.JobsInform;
import com.parser.entity.ParserMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/20/2016.
 */
public class ParserDrupalcenter implements ParserMain {

    private static final String START_LINK = "http://www.drupalcenter.de/drupal-jobs";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    public ParserDrupalcenter() {
    }

    public List<JobsInform> startParse() {
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

            Elements tables2 = doc.select("#forum tbody tr");
            runParse(tables2);
        } catch (IOException e) {
            e.printStackTrace();
            jobsInforms = null;
        }
    }

    private void runParse(Elements tables2) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        for (Element element : tables2) {
            datePublished = null;
            String stringDate = element.select(".created").text();
            if (!stringDate.contains("Wochen")) {
                if ((!stringDate.contains("Tag")) && (stringDate.contains("Min") || stringDate.contains("Stund"))) {
                    datePublished = new Date();
                } else if (stringDate.contains("1 Tag")) {
                    datePublished = new Date(new Date().getTime() - 1 * 24 * 3600 * 1000);
                } else if (stringDate.contains("2 Tag")) {
                    datePublished = new Date(new Date().getTime() - 2 * 24 * 3600 * 1000);
                } else if (stringDate.contains("3 Tag")) {
                    datePublished = new Date(new Date().getTime() - 3 * 24 * 3600 * 1000);
                } else if (stringDate.contains("4 Tag")) {
                    datePublished = new Date(new Date().getTime() - 4 * 24 * 3600 * 1000);
                } else if (stringDate.contains("5 Tag")) {
                    datePublished = new Date(new Date().getTime() - 5 * 24 * 3600 * 1000);
                } else if (stringDate.contains("6 Tag")) {
                    datePublished = new Date(new Date().getTime() - 6 * 24 * 3600 * 1000);
                }
            }

            objectGenerator(element.select(".title").first(),
                    datePublished,
                    element.select(".title a").first());
        }
    }

    private void objectGenerator(Element headPost, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName("");
        jobsInform.setPlace("");
        jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
