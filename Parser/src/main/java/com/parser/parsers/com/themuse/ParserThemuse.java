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
    private String startLink = "https://www.themuse.com/jobs?keyword=TTTTT&filter=true&page=1&sort=primary_attributes_updated_at";
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
        int c = 0;
        List<String> stringCat = new ArrayList<>();
        try {
            ghostDriver = PhantomJSStarter.startPhantom();
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
                doc = startParser(startLink.replace("TTTTT", s));
                Elements tables2 = doc.select(".job-list-individual");
                if (tables2.size() == 0) {
                    c++;
                }
                runParse(tables2, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            c = stringCat.size();
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
        if (c == stringCat.size()) {
            jobsInforms = null;
        }
    }

    private void runParse(Elements tables2, int counter) {
        for (int i = counter; i < tables2.size(); i += 1) {
            objectGenerator(tables2.get(i).select(".location-value").first(),
                    tables2.get(i).select("h2").first(),
                    tables2.get(i).select(".header-text").first(),
                    tables2.get(i).select("a").first());
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

    private JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {
        try {
            ghostDriver.get(linkToDescription);
            Document document = Jsoup.parse(ghostDriver.getPageSource());
            Elements tablesDescription = document.select(".job-post-description").first().children();
            Elements tablesHead = document.select("h1");
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(tablesHead.first()));
            for (Element element : tablesDescription) {
                if (element.tagName().equals("p")) {
                    list.add(addParagraph(element));
                } else if (element.tagName().equals("ul")) {
                    list.add(addList(element));
                }
            }

            list.add(null);
            jobsInform.setOrder(list);


            return jobsInform;
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage() + " " + jobsInform.getPublicationLink());
            if (e.getMessage().contains("RemoteWebDriver")) {
                ghostDriver.close();
                ghostDriver = PhantomJSStarter.startPhantom();
            }
            e.printStackTrace();
            return jobsInform;
        }

    }

    private static ListImpl addHead(Element element) {
        ListImpl list = new ListImpl();
        list.setListHeader(element.text());
        return list;
    }

    private static ListImpl addParagraph(Element element) {
        if (element.select("strong").size() > 0) {
            return addHead(element.select("strong").first());
        } else {


            ListImpl list = new ListImpl();
            list.setTextFieldImpl(element.text());
            return list;
        }
    }

    private static ListImpl addList(Element element) {
        ListImpl list = new ListImpl();
        List<String> strings = new ArrayList<String>();
        for (Element e : element.getAllElements()) {
            strings.add(e.text());
        }
        list.setListItem(strings);
        return list;
    }
}
