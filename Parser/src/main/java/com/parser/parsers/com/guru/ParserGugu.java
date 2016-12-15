package com.parser.parsers.com.guru;

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
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserGugu implements ParserMain {
    private String startLink = "http://www.guru.com/d/jobs/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserGugu() {
    }

    public List<JobsInform> startParse() {
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

            Elements tables2 = doc.select(".serviceItem");
//            System.out.println("text : " + tables2);
            runParse(tables2, 0);

            Date datePublished = null;
            int count = 2;
            do {
                try {

                    datePublished = null;
                    // need http protocol
                    doc = Jsoup.connect(startLink + "pg/" + count + "/")
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(15000)
                            .get();

                    Elements tables1 = doc.select(".serviceItem");
                    datePublished = runParse(tables1, 0);
                    count++;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } while (dateClass.dateChecker(datePublished) && jobsInforms.size() < 100);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = counter; i < tables2.size(); i += 1) {
            String stringDate = tables2.get(i).select(".reltime_new").attr("data-date").substring(0, 10);
//            System.out.println("text date : " + stringDate);
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(tables2.get(i).select(".countryInfo").first(), tables2.get(i).select(".servTitle").first(),
                        tables2.get(i), datePublished, tables2.get(i).select(".servTitle a").first());
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
            jobsInform.setPlace(place.ownText().substring(0, place.ownText().indexOf(" $")));
            jobsInform.setCompanyName("");
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
//            jobsInform = getDescription(linkDescription.attr("abs:href"), jobsInform);
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(headPost));
            list.add(null);
            jobsInform.setOrder(list);
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


            Elements tablesDescription = document.select("#job-description").first().select("p");
            Element tablesHead = document.select("h1").first();
            Elements tablesPlace = document.select("#job-description").get(1).select("tr");
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(tablesHead));
            tablesPlace.stream().filter(e -> e.select("th").first().text().contains("Location")).forEach(e -> {
                jobsInform.setPlace(e.select("td").first().ownText());
            });

            for (int i = 0; i < tablesDescription.size(); i++) {


                list.add(addParagraph(tablesDescription.get(i)));
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
        list.setListHeader(element.ownText());
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
