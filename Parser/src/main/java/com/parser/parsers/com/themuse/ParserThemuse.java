package com.parser.parsers.com.themuse;

import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserThemuse implements ParserMain {
    private String startLink = "https://www.themuse.com/jobs?keyword=TTTTT&filter=true&page=I&sort=primary_attributes_updated_at";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;

    private WebDriver ghostDriver;

    public ParserThemuse() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }


    private Document startParser(String url) {
        if (ghostDriver == null) ghostDriver = PhantomJSStarter.startPhantom();
        ghostDriver.get(url);
        try {
            WebDriverWait wait = new WebDriverWait(ghostDriver, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("job-list-individual__header-section")));
        } catch (Exception e) {
            System.out.println("themuse");
            e.printStackTrace();
        }
        return Jsoup.parse(ghostDriver.getPageSource());
    }

    private void parser() {
//        int c = 0;
        List<String> stringCat = new ArrayList<>();
        try {
            stringCat.add("drupal");
            stringCat.add("angular");
            stringCat.add("react");
            stringCat.add("meteor");
            stringCat.add("node");
            stringCat.add("frontend");
            stringCat.add("javascript");
            stringCat.add("ios");
            stringCat.add("mobile");
            for (String s : stringCat) {
                for (int count = 1; count < 10; count++) {
                    String link = startLink.replace("page=I", "page=" + count);
                    doc = startParser(link.replace("TTTTT", s));
                    Elements tables2 = doc.select(".job-list-individual");
//                if (tables2.size() == 0) {
//                    c++;
//                }
                    if (tables2.size() == 0) break;
                    runParse(tables2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
//            c = stringCat.size();
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
//        if (c == stringCat.size()) {
//            jobsInforms = null;
//        }
    }

    private void runParse(Elements tables2) {
        for (Element element : tables2) {
            objectGenerator(element.select(".location-value").first(),
                    element.select("h2").first(),
                    element.select(".header-text").first(),
                    element.select("a").first());
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {

        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(null);
        jobsInform.setHeadPublication(headPost.ownText());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place == null ? "" : place.text());
        jobsInform.setPublicationLink("https://www.themuse.com" + linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
