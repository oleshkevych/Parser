package com.parser.parsers.de.webentwicklerJobs;

import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PrintDescription;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserWebentwicklerJobs implements ParserMain {

    private static final String START_LINK = "http://www.webentwickler-jobs.de/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();

    public ParserWebentwicklerJobs() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {
            Document doc = Jsoup.connect(START_LINK)
                    .timeout(6000)
                    .method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .execute()
                    .parse();

            Elements tables2 = doc.select(".jobs-table .job-item");
            runParse(tables2);

            int c = 0;
            int count = 2;
            do {
                try {
                    doc = Jsoup.connect(START_LINK + "/?page=" + count)
                            .timeout(6000)
                            .method(Connection.Method.GET)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .execute()
                            .parse();
                    Elements tables1 = doc.select(".jobs-table .job-item");
                    runParse(tables1);
                    count++;
                    c = tables2.size();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (jobsInforms.size() < 1000 && count < 15 && c != 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runParse(Elements tables2) {
        for (Element element : tables2)
            objectGenerator(element.select("[property='addressLocality']").first(),
                    element.select("[property='title']").first(),
                    element.select("[property='name']").first(),
                    element.select("a").first());
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(null);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink(linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
