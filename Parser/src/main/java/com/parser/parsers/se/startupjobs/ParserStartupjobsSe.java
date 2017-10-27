package com.parser.parsers.se.startupjobs;

import com.parser.entity.JobsInform;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/8/2016.
 */
public class ParserStartupjobsSe implements ParserMain {

    private final static String START_LINK = "http://startupjobs.se/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();

    public ParserStartupjobsSe() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }


    private void parser() {
        Document doc = PhantomJSStarter.startGhost(START_LINK);
        Elements tables2 = doc.select("#leadpageData").first().children();
        runParse(tables2);

    }

    private void runParse(Elements tables2) {
        System.out.println("text date : " + jobsInforms.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM d, y");
        for (Element element : tables2) {
            if (element.hasClass("dateRow")) {
                String stringDate = element.select(".dateRow").text();
                try {
                    datePublished = formatter.parse(stringDate);
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            } else if (element.hasClass("row")) {
                objectGenerator(element.select("p").first().child(1),
                        element.select("a").first(),
                        element.select("p span").first(),
                        datePublished,
                        element.select("a").first());
            }
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink(START_LINK + linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
