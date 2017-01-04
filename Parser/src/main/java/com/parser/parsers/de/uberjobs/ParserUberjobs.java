package com.parser.parsers.de.uberjobs;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserUberjobs implements ParserMain {
    private String startLink = "https://uberjobs.de";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private WebDriver ghostDriver;

    public ParserUberjobs() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {
            ghostDriver = PhantomJSStarter.startPhantom();
            doc = PhantomJSStarter.startGhost(startLink);
            Elements tables2 = doc.select(".clickable");
            runParse(tables2, 0);
            if(tables2.size() == 0){
                jobsInforms = null;
            }
        }finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
    }

    private void runParse(Elements tables2, int counter) {

        for (int i = counter; i < tables2.size(); i += 1) {
            objectGenerator(tables2.get(i).select(".ort").first(), tables2.get(i).select(".job-title").first(),
                    tables2.get(i).select(".anbieter").first(), tables2.get(i).select("a").first());
        }

    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
        if (jobsInforms.size() < 50) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(null);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink("https://uberjobs.de/" + linkDescription.attr("href"));
            jobsInform = getDescription("https://uberjobs.de/" + linkDescription.attr("href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        try {
            ghostDriver.get(linkToDescription);
            Document document = Jsoup.parse(ghostDriver.getPageSource());

            Elements tablesHead = document.select(".job-title");
            List<ListImpl> list = new ArrayList<ListImpl>();

            list.add(addHead(tablesHead.first()));
            Elements tablesDescription = document.select(".job-info").first().children();


            for (Element element : tablesDescription) {
                if (element.tagName().equals("p")) {
                    list.add(addParagraph(element));
                } else if (element.tagName().equals("ul")) {
                    list.add(addList(element));
                }
            }

            if (list.size() < 2) {
                list.add(addParagraph(tablesDescription.first()));
            }
            list.add(null);
            jobsInform.setOrder(list);


            return jobsInform;
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage() + " " + jobsInform.getPublicationLink());
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
        ListImpl list = new ListImpl();
        list.setTextFieldImpl(element.text());
        return list;
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
