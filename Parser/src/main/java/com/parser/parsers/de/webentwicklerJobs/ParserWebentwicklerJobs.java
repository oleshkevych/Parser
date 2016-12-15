package com.parser.parsers.de.webentwicklerJobs;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserWebentwicklerJobs implements ParserMain {
    private String startLink = "http://www.webentwickler-jobs.de/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;

    //    private WebDriver ghostDriver;
    public ParserWebentwicklerJobs() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {
            doc = Jsoup.connect(startLink)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements tables2 = doc.select(".jobs-table .job-item");
            runParse(tables2, 0);

            int count = 2;
            do {
                try {

                    doc = Jsoup.connect(startLink + "/?page=" + count)
                            .validateTLSCertificates(false).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();
                    Elements tables1 = doc.select(".jobs-table .job-item");
                    runParse(tables1, 0);
                    count++;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } while (jobsInforms.size() < 50);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void runParse(Elements tables2, int counter) {

        for (int i = counter; i < tables2.size(); i += 1) {

            objectGenerator(tables2.get(i).select("[property='addressLocality']").first(), tables2.get(i).select("[property='title']").first(),
                    tables2.get(i).select("[property='name']").first(), tables2.get(i).select("a").first());
        }

    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
        if (jobsInforms.size() < 50) {
            JobsInform jobsInform = new JobsInform();
//            System.out.println("text place : " + place.text());
//            System.out.println("text headPost : " + headPost.text());
//            System.out.println("text company : " + company.text());
//            System.out.println("text link : " + linkDescription.attr("href"));
            jobsInform.setPublishedDate(null);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(linkDescription.attr("href"));
//            jobsInform = getDescription(linkDescription.attr("abs:href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

//    public JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {
////        System.out.println("text link1 : " + linkToDescription);
//
//        try {
////            ghostDriver.get(linkToDescription);
////            Document document = Jsoup.parse(ghostDriver.getPageSource());
//            Document document = Jsoup.connect(linkToDescription)
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(5000)
//                    .get();
//
//
//            Elements tablesDescription = document.select(".description");
//            Elements tablesHead = document.select("h1");
//            List<ListImpl> list = new ArrayList<ListImpl>();
//
//            list.add(addHead(tablesHead.first()));
//
//
//            for (Element element : tablesDescription) {
//                if (element.tagName().equals("p")) {
//                    list.add(addParagraph(element));
//                } else if (element.tagName().equals("ul")) {
//                    list.add(addList(element));
//                }
//            }
//
//            if (list.size() < 2) {
//                list.add(addParagraph(tablesDescription.first()));
//            }
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
//
//    private static ListImpl addHead(Element element) {
//        ListImpl list = new ListImpl();
//        list.setListHeader(element.text());
//        return list;
//    }
//
//    private static ListImpl addParagraph(Element element) {
//        ListImpl list = new ListImpl();
//        list.setTextFieldImpl(element.text());
//        return list;
//    }
//
//    private static ListImpl addList(Element element) {
//        ListImpl list = new ListImpl();
//        List<String> strings = new ArrayList<String>();
//        for (Element e : element.getAllElements()) {
//            strings.add(e.text());
//        }
//        list.setListItem(strings);
//        return list;
//    }
}
