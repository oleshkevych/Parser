package com.parser.parsers.com.simplyhired;

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
public class ParserSimplyhired implements ParserMain {
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserSimplyhired() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    public List<JobsInform> getJobsInforms() {
        return jobsInforms;
    }

    private void parser() {
        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("http://www.simplyhired.com/search?q=drupal&fdb=7&sb=dd");
        startLinksList.add("http://www.simplyhired.com/search?q=Angular&fdb=7&sb=dd");
        startLinksList.add("http://www.simplyhired.com/search?q=React&fdb=7&sb=dd");
        startLinksList.add("http://www.simplyhired.com/search?q=Meteor&fdb=7&sb=dd");
        startLinksList.add("http://www.simplyhired.com/search?q=iOS&fdb=7&sb=dd");
        startLinksList.add("http://www.simplyhired.com/search?q=Frontend&fdb=7&sb=dd");
        startLinksList.add("http://www.simplyhired.com/search?q=Node&fdb=7&sb=dd");
        startLinksList.add("http://www.simplyhired.com/search?q=JavaScript&fdb=7&sb=dd");
        startLinksList.add("http://www.simplyhired.com/search?q=mobile&fdb=7&sb=dd");

        int c = 0;
        for (String link : startLinksList) {
            try {

                doc = Jsoup.connect(link)
                        .validateTLSCertificates(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();

                Elements tables2 = doc.select(".jobs .card");
                runParse(tables2, 0);
                Date datePublished = null;

                int count = 2;
                do {
                    try {
                        datePublished = null;
                        doc = Jsoup.connect(link + "&pn=" + count)
                                .validateTLSCertificates(false).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();

                        Elements tables1 = doc.select(".jobs .card");
                        datePublished = runParse(tables1, 0);
                        count++;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                while (dateClass.dateChecker(datePublished) && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 50);

            } catch (IOException e) {
                e.printStackTrace();
                c++;
            }
        }
        if(c == startLinksList.size()){
            jobsInforms = null;
        }
    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        for (int i = counter; i < tables2.size(); i += 1) {
            String stringDate = tables2.get(i).select(".jobposting-timestamp").text();
            if (stringDate.contains("minut") || stringDate.contains("hour")) {
                datePublished = new Date();
            } else if (stringDate.contains("1 day")) {
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

            objectGenerator(tables2.get(i).select(".jobposting-location").first(),
                    tables2.get(i).select(".jobposting-title").first(),
                    tables2.get(i).select(".jobposting-company").first(),
                    datePublished,
                    tables2.get(i).select(".jobposting-title a").first());
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
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

}
