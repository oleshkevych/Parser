package com.parser.parsers.io.webbjobb;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/13/2016.
 */
public class ParserWebbjobb implements ParserMain{



    private String startLink = "https://webbjobb.io/lediga-jobb/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserWebbjobb(){
    }

    public List<JobsInform> startParse(){
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {


//         need http protocol
            doc = Jsoup.connect(startLink)
                    .validateTLSCertificates(true)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();
//
            Elements tables2 = doc.select(".job");
////
            runParse(tables2, 0);
////
        Date datePublished = null;
        int count = 2;
//            for(Element element: tables2) {
//            }
            do try {

                datePublished = null;
                // need http protocol

                doc = Jsoup.connect(startLink+count)
                        .validateTLSCertificates(true)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();

                Elements tables3 = doc.select(".job");
//            System.out.println("text : " + doc);
////
//            System.out.println("text : " + tables2);
                datePublished = runParse(tables3,/* jobsInforms.size()*/0);
//                        Elements tables4 = doc.select(".sponsored_job");
//                        runParse(tables4, 0);
//                        System.out.println(" text RENDER: " + tables1);
//                        datePublished = runParse(tables1, 0);
                count++;

            } catch (IOException e) {
                e.printStackTrace();
            } while (dateClass.dateChecker(datePublished) && jobsInforms.size()<100);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date datePublished = null;
        for (int i = counter; i<tables2.size(); i+=1) {
//            System.out.println("text date : " + tables2.get(i));
            datePublished = null;

//                 for (int i = counter; i<tables2.size(); i+=1) {
            String stringDate = tables2.get(i).select(".job-published").text().substring(1);
//            stringDate = stringDate.substring(0, stringDate.indexOf(" "));
//            System.out.println("text date : " + stringDate);
//            System.out.println("text date : " + stringDate.contains("hour"));
//            System.out.println("text date : " + stringDate.split(" ").contains("hour"));
//            if(stringDate.contains("minut")||stringDate.contains("hour")){
//                datePublished = new Date();
//            }else if(stringDate.contains("yesterday")||stringDate.contains("1 day")){
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
//                System.out.println("text date : " + datePublished);
            objectGenerator(tables2.get(i).select(".job-company-city").first(), tables2.get(i).select(".job-title").first(),
                    tables2.get(i).select(".job-company-city").first(), datePublished, tables2.get(i).select(".job-title a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }

        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription){
        if(dateClass.dateChecker(datePublished)) {
            String companyName = company.text();
            String city = companyName.substring(companyName.indexOf(",")+1);
            companyName = companyName.substring(0, companyName.indexOf(","));
            JobsInform jobsInform = new JobsInform();
//            System.out.println("text place : " + city);
//            System.out.println("text headPost : " + headPost.text());
//            System.out.println("text company : " + companyName);
//            System.out.println("text link1 : " + linkDescription.attr("href"));
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(companyName);
            jobsInform.setPlace(city);
            jobsInform.setPublicationLink(linkDescription.attr("href"));
            jobsInform = getDescription(linkDescription.attr("href"), jobsInform);
            if(!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform){

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(true)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();


//        Document document = ParserLandingJobs.renderPage(linkToDescription);

//        System.out.println("text link1 : " + document);
            Elements tablesDescription = document.select(".job-info-section").first().children();
            Elements tablesHead = document.select(".section-title");
            List<ListImpl> list = new ArrayList<ListImpl>();


            list.add(addHead(tablesHead.first()));
            for (int i = 0; i<tablesDescription.size(); i++) {



                /*if (tablesDescription.get(i).select(".iCIMS_Expandable_Text").size()>0) {
                    for (Element element : tablesDescription.get(i).select(".iCIMS_Expandable_Text").first().children()) {
                        if (element.tagName().equals("p")) {
                            list.add(addParagraph(element));
                        } else if (element.tagName().equals("ul")) {
                            list.add(addList(element));
                        }
                    }
                }else*/ if (tablesDescription.get(i).tagName().equals("p")) {
                    list.add(addParagraph(tablesDescription.get(i)));
                }else if (tablesDescription.get(i).tagName().equals("ul")){
                    list.add(addList(tablesDescription.get(i)));
                }else if(tablesDescription.get(i).tagName().contains("h")){
                    list.add(addHead(tablesDescription.get(i)));
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