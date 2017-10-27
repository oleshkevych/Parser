package com.parser.parsers.jobs.landing;


import com.parser.entity.JobsInform;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/6/2016.
 */
public class ParserLandingJobs implements ParserMain {

    private static final String START_LINK = "https://landing.jobs/offers/?page=1&s=date&s_l=0&s_h=100&t_co=false&t_st=false";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private WebDriver ghostDriver;

    public ParserLandingJobs() {
    }

    public List<JobsInform> startParse() {
        try {
            parser();
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
        return jobsInforms;
    }

    public Document startGhost(String link) {
        if (ghostDriver == null) ghostDriver = PhantomJSStarter.startPhantom();
        ghostDriver.get(link);
        new WebDriverWait(ghostDriver, 15);
        return Jsoup.parse(ghostDriver.getPageSource());
    }

    public List<JobsInform> getJobsInforms() {
        return jobsInforms;
    }

    private void parser() {

            Document doc = startGhost(START_LINK);
            Elements tables2 = doc.select(".ld-job-offer");
            runParse(tables2);
    }

    private void runParse(Elements tables2) {
        for (Element element : tables2) {
            try {
                objectGenerator(element.select("div.ld-job-detail").first(),
                        element.select(".ld-job-title").first(),
                        element.select(".ld-job-company").first(),
                        new Date(1),
                        element.select(".ld-job-offer-link").first());
            } catch (Exception e) {
                System.out.println("error : ParserLandingJobs");
                e.printStackTrace();
            }
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.ownText());
        jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
