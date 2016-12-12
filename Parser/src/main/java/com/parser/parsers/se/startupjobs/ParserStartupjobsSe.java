package com.parser.parsers.se.startupjobs;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.parsers.jobs.landing.ParserLandingJobs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/8/2016.
 */
public class ParserStartupjobsSe {
    private String startLink = "http://startupjobs.se/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserStartupjobsSe(){
        dateClass = new DateGenerator();
        parser();
        System.out.println("FINISH ");

    }

    public List<JobsInform> getJobsInforms() {
        return jobsInforms;
    }

    private void parser() {
//        try {


            // need http protocol
//            doc = Jsoup.connect(startLink)
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(5000)
//                    .get();
            doc = ParserLandingJobs.renderPage(startLink);
//            System.out.println("text : " + doc);

            Elements tables2 = doc.select("#leadpageData").first().children();
//            System.out.println("text : " + tables2);
            runParse(tables2, 0);

//            Date datePublished = null;
//            int count = 2;
////            do {
//                try {
//
//
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
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM d, y");
        for (int i = counter; i<tables2.size(); i+=1) {
//            System.out.println("text date : " + tables2.get(i));

//                 for (int i = counter; i<tables2.size(); i+=1) {
            if(tables2.get(i).hasClass("dateRow")) {
                String stringDate = tables2.get(i).select(".dateRow").text();
//            System.out.println("text date : " + stringDate);
//            System.out.println("text date : " + stringDate.contains("hour"));
//            System.out.println("text date : " + stringDate.split(" ").contains("hour"));
//                if (stringDate.contains("minut") || stringDate.contains("hour")) {
//                    datePublished = new Date();
//                } else if (stringDate.contains("yesterday")) {
//                    datePublished = new Date(new Date().getTime() - 1 * 24 * 3600 * 1000);
//                } else if (stringDate.contains("2 day")) {
//                    datePublished = new Date(new Date().getTime() - 2 * 24 * 3600 * 1000);
//                } else if (stringDate.contains("3 day")) {
//                    datePublished = new Date(new Date().getTime() - 3 * 24 * 3600 * 1000);
//                } else if (stringDate.contains("4 day")) {
//                    datePublished = new Date(new Date().getTime() - 4 * 24 * 3600 * 1000);
//                } else if (stringDate.contains("5 day")) {
//                    datePublished = new Date(new Date().getTime() - 5 * 24 * 3600 * 1000);
//                } else if (stringDate.contains("6 day")) {
//                    datePublished = new Date(new Date().getTime() - 6 * 24 * 3600 * 1000);
//                }

//            Date datePublished = null;
            try {
                datePublished = formatter.parse(stringDate);
                    System.out.println("text date : " + datePublished);

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
        if(dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
//                System.out.println("text place : " + place.text());
//                System.out.println("text headPost : " + headPost.text());
//                System.out.println("text company : " + company.text());
//            System.out.println("text link1 : " + linkDescription);
//            System.out.println("text link1 : " + linkDescription.absUrl("href"));
//            System.out.println("text link1 : " + linkDescription.attr("href"));
//            System.out.println("text link1 : " + linkDescription.attr("abs:href"));
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
//        System.out.println("text link1 : " + linkToDescription);

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();


            Elements tablesDescription = document.select(".mainContainer").get(1).children();
//            Elements tablesHead = document.select(".detail-sectionTitle");
            List<ListImpl> list = new ArrayList<ListImpl>();
//            System.out.println("text link1 : " + tablesDescription);

            for (int i = 0; i<tablesDescription.size(); i++) {

//                System.out.println("text link1 : " + tablesDescription.size());
               if(tablesDescription.get(i).tagName().contains("h")){
                   if(list.size()>0) {
                       list.add(null);
                   }
                   list.add(addHead(tablesDescription.get(i)));
               } else if(tablesDescription.get(i).select("p").size() > 0 || tablesDescription.get(i).select("ul").size() > 0){
//                   System.out.println("text p/ul : " + tablesDescription.get(i));
                   for(Element element: tablesDescription.get(i).select(".col-md-12").first().children()){
                       if(element.tagName().equals("p")){
                           list.add(addParagraph(element));
//                           System.out.println("text p : " + element);
                       }else if(element.tagName().equals("ul")){
                           list.add(addList(element));
                       }
                   }
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
