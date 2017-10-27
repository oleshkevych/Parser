package com.parser.parsers.com.builtinaustin;

import com.parser.entity.JobsInform;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import com.parser.parsers.util.DateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.parser.parsers.PhantomJSStarter.startPhantom;

/**
 * Created by rolique_pc on 12/26/2016.
 */
public class ParserBuiltinaustin implements ParserMain {

    private static final String START_LINK = "https://www.builtinaustin.com/jobs?page=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private WebDriver ghostDriver;

    public ParserBuiltinaustin() {
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

    private Document startGhost(String link) {
        if (ghostDriver == null) ghostDriver = startPhantom();
        ghostDriver.get(link);
        new WebDriverWait(ghostDriver, 15);
        return Jsoup.parse(ghostDriver.getPageSource());
    }

    private void parser() {
        int counter = 0;
        int jobsCounter = 0;
        do
            try {
                Document doc = startGhost(START_LINK + counter);
                Elements tables2 = doc.select(".view-jobs .view-content .views-row");
                jobsCounter = tables2.size();
                runParse(tables2);
                counter++;
            } catch (Exception e) {
                e.printStackTrace();
            } while (jobsCounter != 0);
    }

    private void runParse(Elements tables2) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        for (Element element : tables2) {
            String stringDate = element.select(".job-date").text();
            datePublished = DateUtil.getDate(stringDate);

            objectGenerator(element.select(".job-location").first(),
                    element.select(".title").first(),
                    element.select(".company-title").first(),
                    datePublished,
                    element.select("a").first());
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company == null ? "" : company.text());
        jobsInform.setPlace(place == null ? "" : place.text());
        jobsInform.setPublicationLink("http://www.builtinaustin.com" + linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
