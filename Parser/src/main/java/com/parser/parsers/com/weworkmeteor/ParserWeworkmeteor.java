package com.parser.parsers.com.weworkmeteor;


import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/9/2016.
 */
public class ParserWeworkmeteor implements ParserMain {
    private String startLink = "https://www.weworkmeteor.com/jobs";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserWeworkmeteor() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private Document renderPage(String url, boolean description) {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
        path = path.substring(0, path.lastIndexOf("/"))+"\\lib\\phantomjs\\phantomjs.exe";

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, path);

        WebDriver ghostDriver = new PhantomJSDriver(caps);
        try {
            ghostDriver.get(url);
            WebDriverWait wait = new WebDriverWait(ghostDriver, 15);
            if (description) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("job")));
            } else {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("jobSmall")));
            }
            return Jsoup.parse(ghostDriver.getPageSource());
        } finally {
            ghostDriver.quit();
        }
    }

    private void parser() {

        doc = renderPage(startLink, false);
        Elements tables2 = doc.select(".jobSmall .panel-body");
        runParse(tables2, 0);

    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/d/y");
        for (int i = counter; i < tables2.size(); i += 1) {

            String stringDate = tables2.get(i).select(".row").last().child(0).text();
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(tables2.get(i).select("div").first(), tables2.get(i).select("a").first(),
                        tables2.get(i).select(".row").last().select("div").last(), datePublished, tables2.get(i).select("a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }

        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished) && jobsInforms.size() < 100) {
            JobsInform jobsInform = new JobsInform();
            if (company.select(".fa-building").size() > 0 && company.select(".fa-map-marker").size() > 0) {
                String companyName = company.text().substring(0, company.text().indexOf("-"));
                String placeString = company.text().substring(company.text().indexOf("-") + 1);
                jobsInform.setCompanyName(companyName);
                jobsInform.setPlace(placeString);
            } else if (company.select(".fa-building").size() > 0) {
                jobsInform.setCompanyName(company.text());
            } else if (company.select(".fa-map-marker").size() > 0) {
                jobsInform.setPlace(company.text());
            }
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());


            jobsInform.setPublicationLink(startLink.replaceFirst("/jobs", "") + linkDescription.attr("href"));
            jobsInform = getDescription(startLink.replaceFirst("/jobs", "") + linkDescription.attr("href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        Document document = renderPage(linkToDescription, true);
        Elements tablesDescription = document.select(".col-sm-9").first().children();
        Element tablesHead = document.select("h2").first();
        List<ListImpl> list = new ArrayList<ListImpl>();
        list.add(addHead(tablesHead));
        for (int i = 0; i < tablesDescription.size(); i++) {


            if (tablesDescription.get(i).tagName().equals("p")) {
                list.add(addParagraph(tablesDescription.get(i)));
            }
            if (tablesDescription.get(i).tagName().equals("ul")) {
                list.add(addList(tablesDescription.get(i)));
            }
        }

        list.add(null);
        jobsInform.setOrder(list);


        return jobsInform;
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
