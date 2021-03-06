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
    private String startLink = "http://www.webentwickler-jobs.de/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;

    public ParserWebentwicklerJobs() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {
            doc = Jsoup.connect(startLink)
                    .timeout(6000)
                    .method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .execute()
                    .parse();

            Elements tables2 = doc.select(".jobs-table .job-item");
            runParse(tables2, 0);

            int c = 0;
            int count = 2;
            do {
                try {

                    doc = Jsoup.connect(startLink + "/?page=" + count)
                            .timeout(6000)
                            .method(Connection.Method.GET)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .execute()
                            .parse();
                    Elements tables1 = doc.select(".jobs-table .job-item");
                    runParse(tables1, 0);
                    count++;

                } catch (IOException e) {
                    e.printStackTrace();
                    c++;
                }

            } while (jobsInforms.size() < 100 || c > 5);

        } catch (Exception e) {
            e.printStackTrace();
            jobsInforms = null;
        }

    }

    private void runParse(Elements tables2, int counter) {

        for (int i = counter; i < tables2.size(); i++) {

            objectGenerator(tables2.get(i).select("[property='addressLocality']").first(), tables2.get(i).select("[property='title']").first(),
                    tables2.get(i).select("[property='name']").first(), tables2.get(i).select("a").first());
        }

    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(null);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink(linkDescription.attr("href"));
        jobsInform = getDescription(linkDescription.attr("href"), jobsInform);
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .timeout(6000)
                    .method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .execute()
                    .parse();


            Elements tablesDescription = document.select(".ibox-content [property='description']");
            Elements tablesHead = document.select("[property='title']");
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
