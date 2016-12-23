package com.parser.parsers.com.randstad;

import com.google.gson.Gson;
import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.jobs.landing.ParserLandingJobs;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/12/2016.
 */
public class ParserRandstad implements ParserMain {


    private String startLink = "https://www.randstad.com/jobs/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserRandstad() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("https://www.randstad.com/jobs/q-drupal/");
        startLinksList.add("https://www.randstad.com/jobs/q-angular/");
        startLinksList.add("https://www.randstad.com/jobs/q-react/");
        startLinksList.add("https://www.randstad.com/jobs/q-meteor/");
        startLinksList.add("https://www.randstad.com/jobs/q-node/");
        startLinksList.add("https://www.randstad.com/jobs/q-frontend/");
        startLinksList.add("https://www.randstad.com/jobs/q-javascript/");
        startLinksList.add("https://www.randstad.com/jobs/q-ios/");
        startLinksList.add("https://www.randstad.com/jobs/q-mobile/");

        for (String link : startLinksList) {
            try {


                // need http protocol
                doc = Jsoup.connect(link)
                        .validateTLSCertificates(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();
                Elements tables2 = doc.select("article");
                Date datePublished = runParse(tables2, 0);
                int count = 2;
                while (dateClass.dateChecker(datePublished) && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 20) {
                    try {

                        datePublished = null;

                        doc = Jsoup.connect(link + "page-" + count + "/")
                                .validateTLSCertificates(false)
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                                .timeout(5000)
                                .get();
                        Elements tables1 = doc.select("article");
                        datePublished = runParse(tables1, 0);
                        count += 20;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = counter; i < tables2.size(); i += 1) {
            String stringDate = tables2.get(i).select("time").attr("datetime");
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(tables2.get(i).select("[itemprop='jobLocation']").first(), tables2.get(i).select("[itemprop='title']").first(),
                        tables2.get(i).select("[itemprop='employmentType']").first(), datePublished, tables2.get(i).select("a").last());
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
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink("https://www.randstad.com" + linkDescription.attr("href"));
            jobsInform = getDescription("https://www.randstad.com" + linkDescription.attr("href"), jobsInform);
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
            Elements tablesDescription = document.select(".job-desc-section span").first().children();
            Elements tablesHead = document.select("article h1");
            List<ListImpl> list = new ArrayList<ListImpl>();

            list.add(addHead(tablesHead.first()));
            for (int i = 0; i < tablesDescription.size(); i++) {
                if (tablesDescription.get(i).tagName().equals("p")) {
                    list.add(addParagraph(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().equals("ul")) {
                    list.add(addList(tablesDescription.get(i)));
                }
            }


            ListImpl list1 = new ListImpl();
            list1.setTextFieldImpl(tablesDescription.first().ownText());
            list.add(list1);
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
