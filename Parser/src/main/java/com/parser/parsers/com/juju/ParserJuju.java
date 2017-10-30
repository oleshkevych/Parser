package com.parser.parsers.com.juju;


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
 * Created by rolique_pc on 12/7/2016.
 */
public class ParserJuju implements ParserMain {

    private String startLink = "http://www.juju.com/jobs?k=&l=&r=20&c=software-it";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserJuju() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("http://www.juju.com/jobs?k=Drupal&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=Angular&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=React&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=Meteor&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=Node&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=Frontend&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=JavaScript&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=iOS&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=mobile&l&r");

        for (String link : startLinksList) {
            try {
                // need http protocol
                doc = Jsoup.connect(link)
                        .validateTLSCertificates(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();

                Elements tables2 = doc.select(".article .job");
                runParse(tables2);

                boolean isContinue = false;
                int count = 2;
                do {
                    try {
                        // need http protocol
                        doc = Jsoup.connect(link + "&page=" + count)
                                .validateTLSCertificates(false)
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                                .timeout(5000)
                                .get();

                        Elements tables1 = doc.select(".article .job");
                        isContinue = runParse(tables1);
                        count++;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                while (isContinue && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 200);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean runParse(Elements tables2) {
        System.out.println("text date1 : " + jobsInforms.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        int newJobsCount = 0;
        for (Element element : tables2) {
            String stringDate = element.select(".options .source").text();
            try {
                datePublished = formatter.parse(stringDate.substring(stringDate.indexOf("(") + 1, stringDate.indexOf(")")));

                newJobsCount += objectGenerator(element.select(".company span").first(),
                        element.select(".result").first(),
                        element.select(".company").first(),
                        datePublished,
                        element.select(".result").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
        return dateClass.dateChecker(datePublished) && newJobsCount != 0;
    }

    private int objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.ownText());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(linkDescription.attr("href"));
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
                return 1;
            }
            return 0;
        }
}
