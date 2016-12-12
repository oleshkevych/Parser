package com.parser.parsers.com.weworkmeteor;


import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
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
public class ParserWeworkmeteor {
    private String startLink = "https://www.weworkmeteor.com/jobs";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserWeworkmeteor(){
        dateClass = new DateGenerator();
        parser();
        System.out.println("FINISH ");

    }

    public List<JobsInform> getJobsInforms() {
        return jobsInforms;
    }

    private static Document renderPage(String url, boolean description) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:\\Users\\rolique_pc\\Desktop\\ParserApp\\Parser\\Libs\\phantomjs.exe");

        WebDriver ghostDriver = new PhantomJSDriver(caps);
        try {
            ghostDriver.get(url);
            WebDriverWait wait = new WebDriverWait(ghostDriver, 15);
            if(description){
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("job")));
            }else {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("jobSmall")));
            }
            return Jsoup.parse(ghostDriver.getPageSource());
        } finally {
            ghostDriver.quit();
        }
    }

    private void parser() {
//        try {


            // need http protocol
//            doc = Jsoup.connect(startLink)
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(5000)
//                    .get();

        doc = renderPage(startLink, false);
            Elements tables2 = doc.select(".jobSmall .panel-body");
            System.out.println("text : " + doc);
            runParse(tables2, 0);

//            Date datePublished = null;
//            int count = 2;
//            do {
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
//                    count++;
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }while(dateClass.dateChecker(datePublished));

//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/d/y");
        for (int i = counter; i<tables2.size(); i+=1) {
//            System.out.println("text date : " + tables2.get(i));

//                 for (int i = counter; i<tables2.size(); i+=1) {
            String stringDate = tables2.get(i).select(".row").last().child(0).text();
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
                datePublished = formatter.parse(stringDate);
                    System.out.println("text date : " + datePublished);
            objectGenerator(tables2.get(i).select("div").first(), tables2.get(i).select("a").first(),
                    tables2.get(i).select(".row").last().select("div").last(), datePublished, tables2.get(i).select("a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }

        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription){
        if(dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
                System.out.println("text headPost : " + headPost.text());
            if(company.select(".fa-building").size()>0 && company.select(".fa-map-marker").size()>0) {
                String companyName = company.text().substring(0, company.text().indexOf("-"));
                String placeString = company.text().substring(company.text().indexOf("-")+1);
                System.out.println("text company : " + companyName);
                System.out.println("text place : " + placeString);
                jobsInform.setCompanyName(companyName);
                jobsInform.setPlace(placeString);
            }else if(company.select(".fa-building").size()>0){
                System.out.println("text company : " + company.text());
                jobsInform.setCompanyName(company.text());
            }else if(company.select(".fa-map-marker").size()>0){
                System.out.println("text place : " + company.text());
                jobsInform.setPlace(company.text());
            }
                System.out.println("text link1 : " + startLink.replaceFirst("/jobs", "") + linkDescription.attr("href"));
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());


            jobsInform.setPublicationLink(startLink.replaceFirst("/jobs", "") + linkDescription.attr("href"));
            jobsInform = getDescription(startLink.replaceFirst("/jobs", "") + linkDescription.attr("href"), jobsInform);
            if(!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform){

//        try {
//            Document document = Jsoup.connect(linkToDescription)
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(5000)
//                    .get();


        Document document = renderPage(linkToDescription, true);
            Elements tablesDescription = document.select(".col-sm-9").first().children();
            Element tablesHead = document.select("h2").first();
            List<ListImpl> list = new ArrayList<ListImpl>();
            System.out.println("text link1 : " + tablesDescription);
            list.add(addHead(tablesHead));
            for (int i = 0; i<tablesDescription.size(); i++) {




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
//        } catch (Exception e) {
//            System.out.println("Error : " + e.getMessage() + " " + jobsInform.getPublicationLink());
//            e.printStackTrace();
//            return jobsInform;
//        }

    }
    private static ListImpl addHead(Element element){
        ListImpl list = new ListImpl();
        list.setListHeader(element.text());
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
