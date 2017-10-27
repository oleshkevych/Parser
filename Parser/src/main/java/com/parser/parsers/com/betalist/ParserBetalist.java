package com.parser.parsers.com.betalist;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import com.parser.parsers.PrintDescription;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.parser.parsers.PhantomJSStarter.startPhantom;

/**
 * Created by rolique_pc on 12/26/2016.
 */
public class ParserBetalist implements ParserMain {

    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private WebDriver ghostDriver;

    public ParserBetalist() {
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
        List<String> stringCat = new ArrayList<>();
        stringCat.add("drupal");
        stringCat.add("angular");
        stringCat.add("react");
        stringCat.add("meteor");
        stringCat.add("node");
        stringCat.add("frontend");
        stringCat.add("javascript");
        stringCat.add("ios");
        stringCat.add("mobile");

        String startLink = "https://betalist.com/jobs?q=";
        for (String category : stringCat) {
            int count = 0;
            int jobsCount = 0;
            do
                try {
                    Document doc = startGhost(startLink + category + "&p=" + count);
                    Elements tables2 = doc.select(".content-wrapper .ais-hits .ais-hits--item");
                    jobsCount = tables2.size();
                    runParse(tables2);
                    count++;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("owerhfoif" + jobsCount);
                } while (count < 7 && jobsCount != 0);
        }
    }

    private void runParse(Elements tables2) {
        for (Element aTables2 : tables2) {
            objectGenerator(aTables2.select(".jobCard__details__location").first(),
                    aTables2.select(".jobCard__details__title").first(),
                    aTables2.select(".jobCard__details__company").first(),
                    aTables2.select(".jobCard__details__title").first());
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(null);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink("http://betalist.com" + linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
