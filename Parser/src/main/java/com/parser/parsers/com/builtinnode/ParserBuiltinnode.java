package com.parser.parsers.com.builtinnode;


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
 * Created by rolique_pc on 12/9/2016.
 */
public class ParserBuiltinnode {
    private String startLink = "http://builtinnode.com/node-jobs";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserBuiltinnode(){
        dateClass = new DateGenerator();
        parser();
        System.out.println("FINISH ");

    }

    public List<JobsInform> getJobsInforms() {
        return jobsInforms;
    }

    private void parser() {
//        try {


            doc = ParserLandingJobs.renderPage(startLink);
            // need http protocol
//            doc = Jsoup.connect(startLink)
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(5000)
//                    .get();
//            System.out.println("text 0: " + doc);

            Elements tables2 = doc.select(".template-perl-job-board");
//            System.out.println("text : " + tables2);
            runParse(tables2, 0);
//
//            Date datePublished = null;
//            int count = 2;
//            do {
//                try {
//
//                    datePublished = null;
//                    // need http protocol
//                    doc = Jsoup.connect(startLink + "?page=" + count)
//                            .validateTLSCertificates(false)
//                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                            .timeout(15000)
//                            .get();
//
//                    Elements tables1 = doc.select("#joblist .list-group-item");
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        for (int i = counter; i<tables2.size(); i+=1) {
//            System.out.println("text date : " + tables2.get(i));

//                 for (int i = counter; i<tables2.size(); i+=1) {
            String stringDate = tables2.get(i).select("ul").first().select("li").last().text();
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
//                System.out.println("text date : " + datePublished);
                objectGenerator(tables2.get(i).select("ul").first().select("br").first().nextElementSibling(), tables2.get(i).select(".heading").first(),
                        tables2.get(i).select("a").last(), datePublished, tables2.get(i).select(".heading a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }

        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if(dateClass.dateChecker(datePublished)) {
        JobsInform jobsInform = new JobsInform();
//                System.out.println("text place : " + place.text());
//                System.out.println("text headPost : " + headPost.text());
//                System.out.println("text company : " + company.text());
//                System.out.println("text link1 : " + linkDescription.attr("abs:href"));
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        if (!company.text().contains("Read more")){
            jobsInform.setCompanyName(company.text());
        }else{
            jobsInform.setCompanyName("");
        }
            jobsInform.setPlace(place.text());
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


            Elements tablesDescription = document.select("p");
            Element tablesHead  = document.select(".heading").first();
//            Elements tablesPlace = document.select("#job-description").get(1).select("tr");
            List<ListImpl> list = new ArrayList<ListImpl>();
//            System.out.println("text link1 : " + tablesDescription);
            list.add(addHead(tablesHead));
//            tablesPlace.stream().filter(e -> e.select("th").first().text().contains("Location")).forEach(e -> {
//                jobsInform.setPlace(e.select("td").first().ownText());
//            });

            for (int i = 0; i<tablesDescription.size(); i++) {


//                Elements ps = tablesDescription.get(i).select("p");
//                Elements uls = tablesDescription.get(i).select("ul");

//                if (ps.size() > 0) {
//                    for (Element p : ps) {
                list.add(addParagraph(tablesDescription.get(i)));
//                    }
//                }
//                if (uls.size() > 0) {
//                    for(Element ul: uls) {
//                        list.add(addList(ul));
//                    }
//                }
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
