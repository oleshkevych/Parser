package com.parser.parsers.dk.jobbank;


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
 * Created by rolique_pc on 12/9/2016.
 */
public class ParserJobbank implements ParserMain {

    private String startLink = "http://jobbank.dk/job";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserJobbank() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

    List<String> startLinksList = new ArrayList<>();
        startLinksList.add("http://jobbank.dk/job/?act=find&key=Drupal&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=Angular&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=React&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=Meteor&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=Node&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=Frontend&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=JavaScript&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=iOS&virk=&oprettet=");
        startLinksList.add("http://jobbank.dk/job/?act=find&key=mobile&virk=&oprettet=");

        for (String link : startLinksList) {
            try {

                doc = Jsoup.connect(link)
                        .validateTLSCertificates(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();

                Elements tables2 = doc.select(".job-item-container");
                runParse(tables2, 0);

                Date datePublished = null;
                int count = 2;
                do {
                    try {

                        datePublished = null;
                        // need http protocol
                        doc = Jsoup.connect(link + "?&page=" + count)
                                .validateTLSCertificates(false).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();

                        Elements tables1 = doc.select(".job-item-container");
                        datePublished = runParse(tables1, 0);
                        count++;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } while (dateClass.dateChecker(datePublished) && jobsInforms.size() < 180);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        for (int i = counter; i < tables2.size(); i += 1) {
            String stringDate = tables2.get(i).select(".job-date-updated").text().replace("Dato:", "");
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(tables2.get(i), tables2.get(i).select(".job-header").first(),
                        tables2.get(i), datePublished, tables2.get(i).select(".job-item").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }

        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setPublicationLink(startLink.substring(0, startLink.lastIndexOf("job")) + linkDescription.attr("href"));
            jobsInform = getDescription(startLink.substring(0, startLink.lastIndexOf("job")) + linkDescription.attr("href"), jobsInform);
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


            Elements tablesDescription = document.select(".jobContent").first().children();
            Elements tablesHead = document.select(".job-view-title");
            Element tablesPlace = document.select("span[itemprop=\"jobLocation\"]").first();
            Element tablesCompany = document.select("b[itemprop=\"hiringOrganization\"]").first();
            List<ListImpl> list = new ArrayList<ListImpl>();
            jobsInform.setCompanyName(tablesCompany.text());
            jobsInform.setPlace(tablesPlace.text());
            if (tablesHead != null) {
                list.add(addHead(tablesHead.first()));
            }
            for (int i = 0; i < tablesDescription.size(); i++) {


                if (tablesDescription.get(i).tagName().equals("p")) {
                    list.add(addParagraph(tablesDescription.get(i)));
                }
                if (tablesDescription.get(i).tagName().equals("ul")) {
                    list.add(addList(tablesDescription.get(i)));
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

    private static ListImpl addHead(Element element) {
        ListImpl list = new ListImpl();
        list.setListHeader(element.text());
        return list;
    }

    private static ListImpl addParagraph(Element element) {
        if (element.select("strong").size() > 0) {
            return addHead(element.select("strong").first());
        } else {


            ListImpl list = new ListImpl();
            list.setTextFieldImpl(element.text());
            return list;
        }
    }

    private static ListImpl addList(Element element) {
        ListImpl list = new ListImpl();
        List<String> strings = new ArrayList<String>();
        for (Element e : element.getAllElements()) {
            strings.add(e.text());
        }
        list.setListItem(strings);
        return list;
    }
}
