package com.parser.parsers.cc.startus;

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

/**
 * Created by rolique_pc on 12/6/2016.
 */
public class ParserStartus {


    private String startLink = "https://www.startus.cc/jobs?search=&radius[0]=50&job_geo_location=&lat=&lon=&country=&administrative_area_level_1=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserStartus(){
        dateClass = new DateGenerator();
        parser();
        System.out.println("FINISH ");

    }

    public List<JobsInform> getJobsInforms() {
        return jobsInforms;
    }

    private void parser() {
        try {


            // need http protocol
            doc = Jsoup.connect(startLink).validateTLSCertificates(false).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();

            Elements tables2 = doc.select(".views-row");
//            System.out.println("text : " + tables2);
            runParse(tables2, 0);

            Date datePublished = null;
            int count = 1;
            do {
                try {

                    datePublished = null;
                    // need http protocol
                    doc = Jsoup.connect(startLink + "&page=" + count)
                            .validateTLSCertificates(false).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();

                    Elements tables1 = doc.select(".views-row");
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        for (int i = counter; i<tables2.size(); i+=1) {
//                System.out.println("text date : " + tables2.get(i));

//            Date datePublished = null;
            try {
                datePublished = formatter.parse(tables2.get(i).select(".date").text());
//                    System.out.println("text date : " + datePublished);
                objectGenerator(tables2.get(i).select(".description").first(), tables2.get(i).select(".node__title").first(),
                        tables2.get(i).select(".recruiter-company-profile-job-organization").first(), datePublished, tables2.get(i).select(".recruiter-job-link").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }

        }
       return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription){
        if(dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
//                System.out.println("text place : " + place.ownText());
//                System.out.println("text headPost : " + headPost.text());
//                System.out.println("text company : " + company.text());
//                System.out.println("text link1 : " + linkDescription.attr("abs:href"));
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.ownText());
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
//            jobsInform = getDescription(linkDescription.attr("abs:href"), jobsInform);
            if(!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform){

        try {
            Document document = Jsoup.connect(linkToDescription).validateTLSCertificates(false).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();


            Elements tablesDescription = document.select(".generic-details-text");
            Elements tablesDescription1 = document.select(".field__item ");
            List<ListImpl> list = new ArrayList<ListImpl>();
            System.out.println("text link1 : " + tablesDescription);

            for (int i = 0; i<tablesDescription.size(); i++) {


                Elements ps = tablesDescription.get(i).select("p");
                Elements uls = tablesDescription.get(i).select("ul");



                if (ps.size() > 0) {
                    for(Element p: ps) {
                        list.add(addParagraph(p));
                    }
                }
                if (uls.size() > 0) {
                    for(Element ul: uls) {
                        list.add(addList(ul));
                    }
                }
            }

            list.add(null);
            jobsInform.setOrder(list);


            return jobsInform;
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage()+" "+jobsInform.getPublicationLink());
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
