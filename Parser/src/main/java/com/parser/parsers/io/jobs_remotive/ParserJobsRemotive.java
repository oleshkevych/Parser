package com.parser.parsers.io.jobs_remotive;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import com.parser.parsers.PrintDescription;
import com.parser.parsers.util.DateUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserJobsRemotive implements ParserMain {

    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();

    public ParserJobsRemotive() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {

        Document doc = PhantomJSStarter.startGhostJobsRemotive("http://jobs.remotive.io/?category=engineering");
        Elements tables2 = doc.select("ul.job_listings").first().children();
        runParse(tables2);
    }

    private void runParse(Elements tables2) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        for (Element element : tables2) {
            String stringDate = element.select(".job_listing-date").text();
            datePublished = DateUtil.getDate(stringDate);
            objectGenerator(element.select(".job_listing-location").first(),
                    element.select(".job_listing-title").first(),
                    element.select("strong").first(),
                    datePublished,
                    element.select("a").first());
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPlace(place.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setPublicationLink(linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
