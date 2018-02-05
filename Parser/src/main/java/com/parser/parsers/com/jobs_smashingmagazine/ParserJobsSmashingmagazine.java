package com.parser.parsers.com.jobs_smashingmagazine;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.parser.parsers.PhantomJSStarter.startPhantom;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserJobsSmashingmagazine implements ParserMain {

    private final static String START_LINK = "http://jobs.smashingmagazine.com/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private DateGenerator dateClass;
    private WebDriver ghostDriver;

    public ParserJobsSmashingmagazine() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private Document startGhost(String link) {
        if (ghostDriver == null) ghostDriver = startPhantom();
        ghostDriver.get(link);
        new WebDriverWait(ghostDriver, 15);
        return Jsoup.parse(ghostDriver.getPageSource());
    }

    private void parser() {
        Document doc = startGhost(START_LINK);
        Elements tables2 = doc.select(".jobs__list").first().children();
        runParse(tables2);
    }

    private void runParse(Elements tables2) {
        try {
            System.out.println("text date : " + tables2.size());
            for (Element aTables2 : tables2) {
                if (aTables2.hasClass("hidden")) continue;
                objectGenerator(aTables2.select(".job__location").first(),
                        aTables2.select(".job__title").first(),
                        aTables2.select(".job__title").first(),
                        aTables2.select("a").first());
            }
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {

        JobsInform jobsInform = new JobsInform();
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName("");
        jobsInform.setPlace(place.ownText());
        jobsInform.setPublicationLink(START_LINK + linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
