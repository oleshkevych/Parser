package com.parser.parsers.com.virtualvocations;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PrintDescription;
import com.parser.parsers.util.DateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/6/2016.
 */
public class ParserVirtualvocations implements ParserMain {


    private String startLink = "https://www.virtualvocations.com/jobs";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserVirtualvocations() {
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
        startLinksList.add("https://www.virtualvocations.com/jobs/q-Drupal");
        startLinksList.add("https://www.virtualvocations.com/jobs/q-Angular");
        startLinksList.add("https://www.virtualvocations.com/jobs/q-React");
        startLinksList.add("https://www.virtualvocations.com/jobs/q-Meteor");
        startLinksList.add("https://www.virtualvocations.com/jobs/q-Node");
        startLinksList.add("https://www.virtualvocations.com/jobs/q-Frontend");
        startLinksList.add("https://www.virtualvocations.com/jobs/q-JavaScript");
        startLinksList.add("https://www.virtualvocations.com/jobs/q-iOS");
        startLinksList.add("https://www.virtualvocations.com/jobs/q-mobile");

        int c = 0;
        for (String link : startLinksList) {
            try {

                doc = Jsoup.connect(link)
                        .validateTLSCertificates(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();

                Elements tables2 = doc.select(".card-item");
                runParse(tables2);

                Date datePublished = null;
                int count = 1;
                try {
                    do {
                        // need http protocol
                        doc = Jsoup.connect(link + "/p-" + count)
                                .validateTLSCertificates(false)
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                                .timeout(5000)
                                .get();

                        Elements tables1 = doc.select(".col-sm-6 .list-unstyled li");

                        datePublished = runParse(tables1);
                        count += 10;
                    }
                    while (dateClass.dateChecker(datePublished) && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 200);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
                c++;
            }
        }
        if (c == startLinksList.size()) {
            jobsInforms = null;
        }
    }

    private Date runParse(Elements tables2) {
        Date datePublished = null;
        for (Element element : tables2) {
            String stringDate = element.select(".meta").text();
            datePublished = DateUtil.getDate(stringDate);
            objectGenerator(element.select("h2 a").first(),
                    element.select("h2 a").first(),
                    element.select(".recruiter-company-profile-job-organization").first(),
                    datePublished,
                    element.select("h2 a").first());
        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.attr("title"));
        jobsInform.setCompanyName("");
        jobsInform.setPlace(place.text().replaceFirst((headPost.attr("title") + " -"), ""));
        jobsInform.setPublicationLink(linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
