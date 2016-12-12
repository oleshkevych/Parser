package com.parser.parsers.com.berlinstartupjobs;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
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
public class ParserBerlinstartupjobs {

    private String startLink1 = "http://berlinstartupjobs.com/engineering/";
    private String startLink2 = "http://berlinstartupjobs.com/design-ux/";
    private String startLink3 = "http://berlinstartupjobs.com/contracting-positions/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserBerlinstartupjobs(){
        dateClass = new DateGenerator();
        parser(startLink1);
        parser(startLink2);
        parser(startLink3);
        System.out.println("FINISH ");

    }

    public List<JobsInform> getJobsInforms() {
        return jobsInforms;
    }

    private void parser(String startLink) {
        try {


            // need http protocol
            doc = Jsoup.connect(startLink)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements tables2 = doc.select(".product-listing-link-block");
//            System.out.println("text : " + tables2);
            runParse(tables2, 0);

            Date datePublished = null;
            int count = 2;
            do {
                try {
                    datePublished = null;

                    // need http protocol
                    doc = Jsoup.connect(startLink + "page/" + count + "/")
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    Elements tables1 = doc.select(".product-listing-link-block");
//            System.out.println("text : " + tables2);
                    datePublished = runParse(tables1, 0);
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
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, y");
        for (int i = counter; i<tables2.size(); i+=1) {
//            System.out.println("text date : " + tables2.get(i));

//                 for (int i = counter; i<tables2.size(); i+=1) {
            String stringDate = tables2.get(i).select(".product-listing-date").text();
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
                    System.out.println("text date : " + datePublished);
            objectGenerator(tables2.get(i).select(".location").first(), tables2.get(i).select(".product-listing-h2").first(),
                    tables2.get(i).select(".category-tag a").first(), datePublished, tables2.get(i).select(".product-listing-h2 a").first());
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
                System.out.println("text headPost : " + headPost.text());
                System.out.println("text company : " + company.text());
                System.out.println("text link1 : " + linkDescription.attr("abs:href"));
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text().substring(0, headPost.text().indexOf("//")));
            jobsInform.setCompanyName(headPost.text().substring(headPost.text().indexOf("//"), headPost.text().length()));
            jobsInform.setPlace("");
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
            jobsInform = getDescription(linkDescription.attr("abs:href"), jobsInform);
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


            Elements tablesDescription = document.select("#primary");
            Elements tablesCompAbout = tablesDescription.select(".paragraph").first().children();
            Elements tablesDescriptionJob = tablesDescription.select(".job-details").first().children();
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(tablesDescription.select("h1").first()));
            list.addAll(tablesCompAbout.stream().map(ParserBerlinstartupjobs::checker).collect(Collectors.toList()));
            list.addAll(tablesDescriptionJob.stream().map(ParserBerlinstartupjobs::checker).collect(Collectors.toList()));

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
        if (element.tagName().equals("p")) {
            return (addParagraph(element));

        }else if (element.tagName().equals("ul")) {
            return (addList(element));

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