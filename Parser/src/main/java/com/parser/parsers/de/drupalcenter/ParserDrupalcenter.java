package com.parser.parsers.de.drupalcenter;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PrintDescription;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/20/2016.
 */
public class ParserDrupalcenter implements ParserMain {

    private String startLink = "http://www.drupalcenter.de/drupal-jobs";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserDrupalcenter() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
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

            Elements tables2 = doc.select("#forum tbody tr");
            runParse(tables2, 0);
        } catch (IOException e) {
            e.printStackTrace();
            jobsInforms = null;
        }
    }


    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        for (int i = counter; i < tables2.size(); i += 1) {
            datePublished = null;
            String stringDate = tables2.get(i).select(".created").text();
            if (!stringDate.contains("Wochen")) {
                if ((!stringDate.contains("Tag")) && (stringDate.contains("Min") || stringDate.contains("Stund"))) {
                    datePublished = new Date();
                } else if (stringDate.contains("1 Tag")) {
                    datePublished = new Date(new Date().getTime() - 1 * 24 * 3600 * 1000);
                } else if (stringDate.contains("2 Tag")) {
                    datePublished = new Date(new Date().getTime() - 2 * 24 * 3600 * 1000);
                } else if (stringDate.contains("3 Tag")) {
                    datePublished = new Date(new Date().getTime() - 3 * 24 * 3600 * 1000);
                } else if (stringDate.contains("4 Tag")) {
                    datePublished = new Date(new Date().getTime() - 4 * 24 * 3600 * 1000);
                } else if (stringDate.contains("5 Tag")) {
                    datePublished = new Date(new Date().getTime() - 5 * 24 * 3600 * 1000);
                } else if (stringDate.contains("6 Tag")) {
                    datePublished = new Date(new Date().getTime() - 6 * 24 * 3600 * 1000);
                }
            }

            objectGenerator(tables2.get(i), tables2.get(i).select(".title").first(),
                    tables2.get(i), datePublished, tables2.get(i).select(".title a").first());
        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName("");
            jobsInform.setPlace("");
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
            jobsInform = getDescription(linkDescription.attr("abs:href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();


            Elements tablesDescription = document.select(".node .content").first().children();
            Elements tablesHead = document.select(".title");
            List<ListImpl> list = new ArrayList<ListImpl>();

            list.add(addHead(tablesHead.first()));
            list.addAll(new PrintDescription().generateListImpl(tablesDescription));

            list.add(null);
            jobsInform.setOrder(list);


            return jobsInform;
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage() + " " + jobsInform.getPublicationLink());
            e.printStackTrace();
            return jobsInform;
        }

    }

    private static ListImpl addHead(Element element) {
        ListImpl list = new ListImpl();
        list.setListHeader(element.text());
        return list;
    }

}
