package com.parser.parsers.io.remoteok;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PrintDescription;
import com.parser.parsers.com.weworkremotely.ParserWeworkremotely;
import com.parser.parsers.io.wfh.ParserWFH;
import com.parser.parsers.jobs.landing.ParserLandingJobs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/6/2016.
 */
public class ParserRemoteok implements ParserMain {

    private String startLink = "https://remoteok.io";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserRemoteok() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {
            doc = Jsoup.connect(startLink)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements tables2 = doc.select("tbody .job");
            runParse(tables2);
            if (tables2.size() == 0) {
                jobsInforms = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            jobsInforms = null;
        }

    }

    private void runParse(Elements tables2) {
        for (Element element : tables2) {
            Date datePublished = null;
            String stringDate = element.select(".time").text();
            if (stringDate.contains("m") || stringDate.contains("h")) {
                datePublished = new Date();
            } else if (stringDate.contains("d")) {
                String date = stringDate.replaceAll("[^0-9]", "");
                if (date.length() != 0)
                    datePublished = new Date(new Date().getTime() - Integer.parseInt(date) * 24 * 3600 * 1000);
            } else if (stringDate.contains("mo")) {
                String date = stringDate.replaceAll("[^0-9]", "");
                if (date.length() != 0)
                    datePublished = new Date(new Date().getTime() - Integer.parseInt(date) * 30 * 24 * 3600 * 1000);
            }
            try {
                objectGenerator(element.select(".location").first(),
                        element.select("[itemprop='title']").first(),
                        element.select(".company [itemprop='name']").first(),
                        datePublished,
                        element.select("[itemprop='url']").first());
            } catch (IndexOutOfBoundsException e) {
                System.out.println("ParserRemoteok " + e.getMessage());
            }

        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
