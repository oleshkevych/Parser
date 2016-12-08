package com.parser.com.weworkremotely;

import com.parser.DateGenerator;
import com.parser.JobsInform;
import com.parser.ListImpl;
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
public class ParserWeworkremotely {

    private String startLink1 = "https://weworkremotely.com/categories/6-devops-sysadmin/jobs#intro";
    private String startLink2 = "https://weworkremotely.com/categories/2-programming/jobs#intro";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserWeworkremotely(){
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
            doc = Jsoup.connect(startLink1)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements tables2 = doc.select(".jobs li");
//            System.out.println("text : " + tables2);
            runParse(tables2, 0);
            doc = Jsoup.connect(startLink2)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements tables = doc.select(".jobs li");
//            System.out.println("text : " + tables2);
            runParse(tables, 0);
            Date datePublished = null;
            int count = 2;
//            do {
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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        for (int i = counter; i<tables2.size(); i+=1) {
//            System.out.println("text date : " + tables2.get(i));

//                 for (int i = counter; i<tables2.size(); i+=1) {
            String stringDate = "";
            try {
                stringDate = tables2.get(i).select(".date").first().text() + " 2016";
            }catch (NullPointerException n){
                System.out.println("text date : " + tables2.get(i));

            }
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
            objectGenerator(tables2.get(i).select(".location").first(), tables2.get(i).select(".title").first(),
                    tables2.get(i).select(".company").first(), datePublished, tables2.get(i).select("a").first());
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
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
//            jobsInform.setPlace(place.text());
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


            Elements tablesDescription = document.select(".listing-container").first().children();
            Element compPlace = document.select(".location").first();
            Element compName = document.select(".company").first();
            jobsInform.setCompanyName(compName.text());
            jobsInform.setPlace(compPlace.text().replaceFirst("Headquarters: ", ""));
            List<ListImpl> list = new ArrayList<ListImpl>();
//            System.out.println("text link1 : " + tablesDescription);
            list.add(addHead(document.select(".listing-header-container h1").first()));
            for (int i = 0; i<tablesDescription.size(); i++) {


//            System.out.println("text link1 : " + tablesDescription.get(i));

                if (tablesDescription.get(i).select("strong").size() > 0) {
//                    System.out.println("text STRONG : " + tablesDescription.get(i));
                    if(tablesDescription.get(i).select("strong").text().length() > 0
                            && tablesDescription.get(i).select("strong").text().length() == tablesDescription.get(i).text().length()) {
                        list.add(null);
                        list.add(addHead(tablesDescription.get(i).select("strong").first()));
                    }else if(tablesDescription.get(i).text().length()>0){
                        list.add(addParagraph(tablesDescription.get(i)));
                    }
                }else
                if (tablesDescription.get(i).select("div div").size() > 0) {
                    for(Element ul: tablesDescription.get(i).select("div div")) {
                        list.add(addList(ul));
                    }
                }else if ( tablesDescription.get(i).select("ul").size() > 0){
//                    System.out.println("text UL : " + tablesDescription.get(i));

                    for(Element ul: tablesDescription.get(i).select("ul")) {
                        list.add(addList(ul));
                    }
                }else if (tablesDescription.get(i).text().length() > 0){
                    list.add(addParagraph(tablesDescription.get(i)));

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
//        System.out.println("text link1 : " + element);

        list.setListHeader(element.text());
        return list;
    }
    private static ListImpl addParagraph(Element element){
//        if(element.select("strong").size()>0){
//            return addHead(element.select("strong").first());
//        }else {


            ListImpl list = new ListImpl();
            list.setTextFieldImpl(element.text());
            return list;
//        }
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
