package com.parser.parsers.ca.eluta;

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

/**
 * Created by rolique_pc on 12/13/2016.
 */
public class ParserEluta implements ParserMain {


    private String startLink = "http://www.eluta.ca/search?q=TTTTTT&pg=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserEluta() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> stringCat = new ArrayList<>();
        stringCat.add("drupal");
        stringCat.add("angular");
        stringCat.add("react");
        stringCat.add("meteor");
        stringCat.add("node");
        stringCat.add("frontend");
        stringCat.add("javascript");
        stringCat.add("ios");
        stringCat.add("mobile");
        int c = 0;
        for(String s: stringCat) {
            try {

                Date datePublished = null;
                int count = 1;
                do {

                        doc = Jsoup.connect(startLink.replace("TTTTTT", s) + count)
                                .validateTLSCertificates(false)
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                                .timeout(5000)
                                .get();

                        Elements tables3 = doc.select(".organic-job");
                        datePublished = runParse(tables3, 0);

                        count++;

                } while (dateClass.dateChecker(datePublished) && jobsInforms.size() < 100);

            } catch (IOException e) {
                e.printStackTrace();
                c++;
            }
        }
        if (c == stringCat.size()){
            jobsInforms = null;
        }
    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        for (int i = counter; i < tables2.size(); i += 1) {
            datePublished = null;

            String stringDate = tables2.get(i).select(".lastseen").text();
            if (stringDate.contains("minut") || stringDate.contains("hour")) {
                datePublished = new Date();
            } else if (stringDate.contains("yesterday") || stringDate.contains("1 day")) {
                datePublished = new Date(new Date().getTime() - 1 * 24 * 3600 * 1000);
            } else if (stringDate.contains("2 day")) {
                datePublished = new Date(new Date().getTime() - 2 * 24 * 3600 * 1000);
            } else if (stringDate.contains("3 day")) {
                datePublished = new Date(new Date().getTime() - 3 * 24 * 3600 * 1000);
            } else if (stringDate.contains("4 day")) {
                datePublished = new Date(new Date().getTime() - 4 * 24 * 3600 * 1000);
            } else if (stringDate.contains("5 day")) {
                datePublished = new Date(new Date().getTime() - 5 * 24 * 3600 * 1000);
            } else if (stringDate.contains("6 day")) {
                datePublished = new Date(new Date().getTime() - 6 * 24 * 3600 * 1000);
            }

            objectGenerator(tables2.get(i).select(".location").first(), tables2.get(i).select(".lk-job-title").first(),
                    tables2.get(i).select(".lk-employer").first(), datePublished, tables2.get(i).select(".lk-job-title").first());
        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished)) {
            String link = linkDescription.attr("onclick");
            link = link.substring(link.indexOf("'") + 1, link.lastIndexOf("'"));
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.ownText());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink("http://www.eluta.ca" + link);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

//    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform){
//
//        try {
//            Document document = Jsoup.connect(linkToDescription)
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(5000)
//                    .get();
//
//            Elements tablesDescription = document.select(".job-view-content-wrapper").first().children();
//            Elements tablesHead = document.select(".job-view-header-title");
//            List<ListImpl> list = new ArrayList<ListImpl>();
//
//            list.add(addHead(tablesHead.first()));
//            for (int i = 0; i<tablesDescription.size(); i++) {
//
//
//
//                if (tablesDescription.get(i).select(".iCIMS_Expandable_Text").size()>0) {
//                    for (Element element : tablesDescription.get(i).select(".iCIMS_Expandable_Text").first().children()) {
//                        if (element.tagName().equals("p")) {
//                            list.add(addParagraph(element));
//                        } else if (element.tagName().equals("ul")) {
//                            list.add(addList(element));
//                        }
//                    }
//                }else if (tablesDescription.get(i).tagName().equals("p")) {
//                    list.add(addParagraph(tablesDescription.get(i)));
//                }else if (tablesDescription.get(i).tagName().equals("ul")){
//                    list.add(addList(tablesDescription.get(i)));
//                }else if(tablesDescription.get(i).select("div").size()>0){
//                    list.add(addParagraph(tablesDescription.get(i)));
//                }
//            }
//
//            list.add(null);
//            jobsInform.setOrder(list);
//
//
//            return jobsInform;
//        } catch (Exception e) {
//            System.out.println("Error : " + e.getMessage() + " " + jobsInform.getPublicationLink());
//            e.printStackTrace();
//            return jobsInform;
//        }
//
//    }
//    private static ListImpl addHead(Element element){
//        ListImpl list = new ListImpl();
//        list.setListHeader(element.text());
//        return list;
//    }
//    private static ListImpl addParagraph(Element element){
//        if(element.select("strong").size()>0){
//            return addHead(element.select("strong").first());
//        }else {
//
//
//            ListImpl list = new ListImpl();
//            list.setTextFieldImpl(element.text());
//            return list;
//        }
//    }
//    private static ListImpl addList(Element element){
//        ListImpl list = new ListImpl();
//        List<String> strings = new ArrayList<String>();
//        for(Element e : element.getAllElements()) {
//            strings.add(e.text());
//        }
//        list.setListItem(strings);
//        return list;
//    }
}
