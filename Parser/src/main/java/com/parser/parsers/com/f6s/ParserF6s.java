package com.parser.parsers.com.f6s;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by rolique_pc on 12/9/2016.
 */
public class ParserF6s {

    private String startLink = "https://www.f6s.com/jobs/?sort=newest&sort_dir=desc";
    private String baseUrl = "https://www.f6s.com";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserF6s(){
        dateClass = new DateGenerator();
        parser();
        System.out.println("FINISH ");

    }

    public List<JobsInform> getJobsInforms() {
        return jobsInforms;
    }


    private void parser() {
        try {
//            doc = Jsoup.connect(baseUrl)
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(100000)
//                    .get();
//            System.out.println("text : " + doc);
//            doc = Jsoup.connect("https://www.f6s.com/fspcwzqvvyfuraycuud.js?PID=0D7DF155-DA02-37A3-B8D7-70CB495BD5F3")
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(100000)
//                    .get();
//            System.out.println("text : " + doc);
//            // need http protocol
            doc = Jsoup.connect(startLink)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(100000)
                    .get();


            Elements tables2 = doc.select(".result-info");
            System.out.println("text : " + doc);
//            System.out.println("text : " + tables2);
            runParse(tables2, 0);
            Date datePublished = null;
            int count = 2;
            do {
                datePublished = null;
            String newUrl = "https://www.f6s.com/jobs?page=" + count + "&page_alt=1&sort=newest&sort_dir=desc";

                doc = Jsoup.connect(newUrl)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .get();
            Elements tables = doc.select(".result-info");
            System.out.println("text : " + doc);
//            System.out.println("text : " + tables2);
            datePublished = runParse(tables, 0);
//
//            int count = 2;
//
//                try {
//
//                    datePublished = null;
//                    // need http protocol
//                    doc = Jsoup.connect(startLink + "&pg=" + count)
//                            .validateTLSCertificates(false).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();
//
//                    Elements tables1 = doc.select(".listResults .-item");
////            System.out.println("text : " + tables2);
//                    datePublished = runParse(tables1, 0);
                    count++;
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
            }while(dateClass.dateChecker(datePublished));
//
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
//        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        for (int i = counter; i<tables2.size(); i+=1) {
//            System.out.println("text date : " + tables2.get(i));

//                 for (int i = counter; i<tables2.size(); i+=1) {
//            String stringDate = tables2.get(i).select(".posted").text();
//            System.out.println("text date : " + stringDate);
//            System.out.println("text date : " + stringDate.contains("hour"));
//            System.out.println("text date : " + stringDate.split(" ").contains("hour"));
//            if(stringDate.contains("minut")||stringDate.contains("hour")){
//                datePublished = new Date();
//            }else if(stringDate.contains("yesterday")){
//                datePublished = new Date(new Date().getTime() - 1*24*3600*1000);
//            }else if(stringDate.contains("2 day")){
//                datePublished = new Date(new Date().getTime() - 2*24*3600*1000);
//            }else if(stringDate.contains("3 day")){
//                datePublished = new Date(new Date().getTime() - 3*24*3600*1000);
//            }else if(stringDate.contains("4 day")){
//                datePublished = new Date(new Date().getTime() - 4*24*3600*1000);
//            }else if(stringDate.contains("5 day")){
//                datePublished = new Date(new Date().getTime() - 5*24*3600*1000);
//            }else if(stringDate.contains("6 day")){
//                datePublished = new Date(new Date().getTime() - 6*24*3600*1000);
//            }

//            Date datePublished = null;
            try {
//                datePublished = formatter.parse(tables2.get(i).select(".date").text());
//                    System.out.println("text date : " + datePublished);
            JobsInform jobsInform = objectGenerator(tables2.get(i).select(".subtitle span").first(), tables2.get(i).select(".title").first(),
                    tables2.get(i).select(".subtitle span a").first(), tables2.get(i).select(".title").first().select("a").first());
            if(dateClass.dateChecker(jobsInform.getPublishedDate())) {
                datePublished = jobsInform.getPublishedDate();
                if(!jobsInforms.contains(jobsInform)) {
                    jobsInforms.add(jobsInform);
                }
            }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        return datePublished;
    }

    private JobsInform objectGenerator(Element place, Element headPost, Element company, Element linkDescription){

            JobsInform jobsInform = new JobsInform();
                System.out.println("text place : " + place.ownText());
                System.out.println("text headPost : " + headPost.text());
                System.out.println("text company : " + company.text());
//                System.out.println("text link1 : " + linkDescription.attr("abs:href"));

            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.ownText());
            jobsInform.setPublicationLink(baseUrl + linkDescription.attr("href"));
            jobsInform = getDescription(baseUrl + linkDescription.attr("href"), jobsInform);
        return jobsInform;
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform){

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            System.out.println("text link1 : " + document.select("body"));

            Element time = document.select("#jobApplicationWrapper").first().select(".t14").first().child(2);
            String stringDate = time.text();
            Date datePublished = null;
            if(stringDate.contains("minut")||stringDate.contains("hour")){
                datePublished = new Date();
            }else if(stringDate.contains("Yesterday")){
                datePublished = new Date(new Date().getTime() - 1*24*3600*1000);
            }else if(stringDate.contains("2 day")){
                datePublished = new Date(new Date().getTime() - 2*24*3600*1000);
            }else if(stringDate.contains("3 day")){
                datePublished = new Date(new Date().getTime() - 3*24*3600*1000);
            }else if(stringDate.contains("4 day")){
                datePublished = new Date(new Date().getTime() - 4*24*3600*1000);
            }else if(stringDate.contains("5 day")){
                datePublished = new Date(new Date().getTime() - 5*24*3600*1000);
            }else if(stringDate.contains("6 day")){
                datePublished = new Date(new Date().getTime() - 6*24*3600*1000);
            }

            jobsInform.setPublishedDate(datePublished);
                Elements tablesDescription = document.select(".job-description");
            Element tablesHead = document.select(".job-info h1").first();
            List<ListImpl> list = new ArrayList<ListImpl>();
//            System.out.println("text link1 : " + tablesDescription);
            list.add(addHead(tablesHead));
            list.add(addParagraph(tablesDescription.first()));
//            for (int i = 0; i<tablesDescription.size(); i++) {
//
//
//                Elements ps = tablesDescription.get(i).select("p");
//                Elements uls = tablesDescription.get(i).select("ul");
//                list.add(addHead(tablesHead.get(i)));
//
//                if (ps.size() > 0) {
//                    for (Element p : ps) {
//                        list.add(addParagraph(p));
//                    }
//                }
//                if (uls.size() > 0) {
//                    for(Element ul: uls) {
//                        list.add(addList(ul));
//                    }
//                }
//            }

            list.add(null);
            jobsInform.setOrder(list);


            return jobsInform;
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage() + " " + jobsInform.getPublicationLink());
            e.printStackTrace();
            return jobsInform;
        }

    }
    private static ListImpl addHead(Element element){
        ListImpl list = new ListImpl();
        list.setListHeader(element.ownText());
        return list;
    }
    private static ListImpl addParagraph(Element element){
        if(element.select("strong").size()>0){
            return addHead(element.select("strong").first());
        }else {


            ListImpl list = new ListImpl();
            list.setTextFieldImpl(element.text());
            return list;
        }
    }
    private static ListImpl addList(Element element){
        ListImpl list = new ListImpl();
        List<String> strings = new ArrayList<String>();
        for(Element e : element.getAllElements()) {
            strings.add(e.text());
        }
        list.setListItem(strings);
        return list;
    }
}
