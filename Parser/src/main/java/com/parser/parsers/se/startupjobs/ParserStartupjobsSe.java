package com.parser.parsers.se.startupjobs;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PrintDescription;
import com.parser.parsers.jobs.landing.ParserLandingJobs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/8/2016.
 */
public class ParserStartupjobsSe implements ParserMain{
    private String startLink = "http://startupjobs.se/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserStartupjobsSe(){
    }

    public List<JobsInform> startParse(){
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private Document renderPage(String url) {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
        path = path.substring(0, path.lastIndexOf("/"))+"\\lib\\phantomjs\\phantomjs.exe";

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability("takesScreenshot", false);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, path);

        WebDriver ghostDriver = new PhantomJSDriver(caps);
        try {
            ghostDriver.get(url);
            return Jsoup.parse(ghostDriver.getPageSource());
        } finally {
            ghostDriver.quit();
        }
    }

    private void parser() {
            doc = renderPage(startLink);
            Elements tables2 = doc.select("#leadpageData").first().children();
            runParse(tables2, 0);

    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM d, y");
        for (int i = counter; i<tables2.size(); i+=1) {
            if(tables2.get(i).hasClass("dateRow")) {
                String stringDate = tables2.get(i).select(".dateRow").text();
            try {
                datePublished = formatter.parse(stringDate);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
            }else if(tables2.get(i).hasClass("row")){
                objectGenerator(tables2.get(i).select("p").first().child(1), tables2.get(i).select("a").first(),
                        tables2.get(i).select("p span").first(), datePublished, tables2.get(i).select("a").first());
            }
        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription){
        if(dateClass.dateChecker(datePublished)&& jobsInforms.size() < 100) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(startLink+linkDescription.attr("href"));
            jobsInform = getDescription(startLink+linkDescription.attr("href"), jobsInform);
            if(!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform){
        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();


            Elements tablesDescription = document.select(".mainContainer").get(1).children();
            List<ListImpl> list = new ArrayList<ListImpl>();
            Element tablesHead = document.select(".mainContainer").get(1).select("h2").get(1);
            list.add(addHead(tablesHead));
            list.addAll(new PrintDescription().generateListImpl(tablesDescription));
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
}
