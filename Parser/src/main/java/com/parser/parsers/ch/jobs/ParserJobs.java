package com.parser.parsers.ch.jobs;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
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
import java.util.stream.Collectors;

/**
 * Created by rolique_pc on 12/8/2016.
 */
public class ParserJobs implements ParserMain{

    private static String startLink = "http://www.jobs.ch/en/vacancies/?page=1&sort-by=date&term=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserJobs(){

    }

    public List<JobsInform> startParse(){
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {


            // need http protocol
            doc = Jsoup.connect(startLink)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements tables2 = doc.select(".serp-item-job-info");
//            System.out.println("text : " + tables2);
            runParse(tables2, 0);

            Date datePublished = null;
            int count = 2;
            do {
                try {
                    datePublished = null;

                    // need http protocol
                    doc = Jsoup.connect(startLink .replace((count-1)+"", count+""))
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    Elements tables1 = doc.select(".serp-item-job-info");
//            System.out.println("text : " + tables2);
//                    datePublished = runParse(tables1, 0);
                    count++;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }while(dateClass.dateChecker(datePublished));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = counter; i<tables2.size(); i+=1) {
            if(i == 100)
                break;
//            System.out.println("text date : " + tables2.get(i));

//                 for (int i = counter; i<tables2.size(); i+=1) {
            String stringDate = tables2.get(i).select(".serp-item-meta .badge-pool .outline").first().text();
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

            try {
                datePublished = formatter.parse(stringDate);
//                System.out.println("text date : " + datePublished);
                objectGenerator(tables2.get(i).select(".serp-item-head-2 span").get(1), tables2.get(i).select(".serp-item-head-1").first(),
                        tables2.get(i).select(".x--company-link").first(), datePublished, tables2.get(i).select(".x--job-link").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }

        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription){
        if(dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
//                System.out.println("text place : " + place.text());
//            System.out.println("text headPost : " + headPost.text());
//            System.out.println("text company : " + company.text());
//            System.out.println("text link1 : " + linkDescription.attr("abs:href"));
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(headPost.text());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
            jobsInform = getDescription(linkDescription.attr("abs:href"), jobsInform);
            if(!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform){
        Document document = null;
        try {
            document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();
            Elements iframe = document.select("iframe");

                String iframeSrc = iframe.attr("src");
//            System.out.println(linkToDescription.substring(0,linkToDescription.indexOf("?")) + iframeSrc);
//            System.out.println(" : " + linkToDescription);
//
//            System.out.println(" : " + iframeSrc);

            if(iframeSrc != null) {
                document = Jsoup.connect(linkToDescription.substring(0,linkToDescription.indexOf("?")) + iframeSrc)
                        .validateTLSCertificates(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();
            }
//            document = ParserLandingJobs.renderPage(linkToDescription.substring(0,linkToDescription.indexOf("?")) + iframeSrc);
        }catch (IOException e ) {
            System.out.println("Error : " + e.getMessage() + " " + jobsInform.getPublicationLink());
            e.printStackTrace();
            return jobsInform;
        }
        try{
//            System.out.println("                text : " + document);

            Elements tablesDescriptionJob = document.select("p");
//            System.out.println("text link 0: " + (tablesDescriptionJob.size() == 0));
//            if(tablesDescriptionJob.size() == 0){
//                tablesDescriptionJob = document.select(".text");
//            }
//            if(tablesDescriptionJob.size() == 0){
//            }
//            if(tablesDescriptionJob.size() == 0){
//                tablesDescriptionJob = document.select(".textwrap");
//            }
//            System.out.println("text link 0: " + document.select("."));
//            System.out.println("text link 1: " + tablesDescriptionJob);
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(document.select(".vacancy-ad-infobox-text h1").first()));
//            tablesDescriptionJob = document.select("ul");
            list.addAll(tablesDescriptionJob.stream().map(ParserJobs::checker).collect(Collectors.toList()));

            list.add(null);
            jobsInform.setOrder(list);


            return jobsInform;
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage() + " " + jobsInform.getPublicationLink());
            e.printStackTrace();
            return jobsInform;
        }

    }

    private static ListImpl checker(Element element){
        System.out.println("text link1 : " + element);

       if (element.tagName().equals("p")) {
            return (addParagraph(element));

        }else if (element.select("ul").size() > 0) {
            return (addList(element.select("ul").first()));

        }
        return new ListImpl();
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
