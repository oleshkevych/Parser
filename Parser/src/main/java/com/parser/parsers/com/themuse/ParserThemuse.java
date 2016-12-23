package com.parser.parsers.com.themuse;

import com.parser.Main;
import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.jobs.landing.ParserLandingJobs;
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
import java.util.concurrent.TimeUnit;

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

    private void startPhantom() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
        path = path.substring(0, path.lastIndexOf("/"))+"\\lib\\phantomjs\\phantomjs.exe";

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, path);
        caps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.60 Safari/537.17");
//        caps.setCapability("phantomjs.page.settings.host", "https://www.themuse.com/jobs?keyword=drupal&filter=true&page=1&sort=primary_attributes_updated_at");
//        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{
//                "--web-security=false",
//                "--ssl-protocol=any",
//                "--ignore-ssl-errors=true"
//        });
        ghostDriver = new PhantomJSDriver(caps);

    }

    private Document startParser(String url){
        ghostDriver.get(url);
//        System.out.println("text date : " + ghostDriver.getPageSource());
        try {
            WebDriverWait wait = new WebDriverWait(ghostDriver, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("job-element")));
        }catch (Exception e){
            System.out.println("themuse");
            e.printStackTrace();
        }
        return Jsoup.parse(ghostDriver.getPageSource());
    }

    private void parser() {
        try {
            startPhantom();
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
            for (String s: stringCat) {
                doc = startParser(startLink.replace("TTTTT", s));
                Elements tables2 = doc.select(".job-element");
                runParse(tables2, 0);
            }
            ghostDriver.quit();
        }catch (Exception e){
            e.printStackTrace();
            ghostDriver.quit();
        }
        finally {
            ghostDriver.quit();
        }

    }

    private void runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        for (int i = counter; i < tables2.size(); i += 1) {
            objectGenerator(tables2.get(i).select("li").first(), tables2.get(i).select("h3").first(),
                    tables2.get(i).select("span").first(), tables2.get(i).select("a").first());
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
//        if (jobsInforms.size() < 30) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(null);
            jobsInform.setHeadPublication(headPost.ownText());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink("https://www.themuse.com" + linkDescription.attr("href"));
            jobsInform = getDescription("https://www.themuse.com" + linkDescription.attr("href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
//        }
    }

    public JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {
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
