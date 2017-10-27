package com.parser.parsers.com.ziprecruiter;

import com.parser.entity.DateGenerator;
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
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/19/2016.
 */
public class ParserZiprecruiter implements ParserMain {


    private String startLink = "https://www.ziprecruiter.com/candidate/search?search=Drupal&page=1&location=&days=5";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserZiprecruiter() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=Drupal&page=I&location=&");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=Angular&page=I&location=&");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=React&page=I&location=&");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=Meteor&page=I&location=&");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=Node&page=I&location=&");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=Frontend&page=I&location=&");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=JavaScript&page=I&location=&");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=iOS&page=I&location=&");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=mobile&page=I&location=&");

        int c = 0;
        for (String link : startLinksList) {
            int newCount = 0;
            int counter = 1;
            do
                try {
                    doc = Jsoup.connect(link.replace("page=I", "page=" + counter))
                            .timeout(6000)
                            .method(Connection.Method.GET)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .execute()
                            .parse();

                    Elements tables2 = doc.select("article.job_result");
                    newCount = tables2.size();
                    runParse(tables2);
                    counter++;

                } catch (IOException e) {
                    e.printStackTrace();
                    c++;
                } while (newCount != 0 && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 200);
        }
        if (c == startLinksList.size()) {
            jobsInforms = null;
        }

    }

    private void runParse(Elements tables2) {
        System.out.println("text date : " + tables2.size());
        for (Element element : tables2) {
            objectGenerator(element.select(".location").first(),
                    element.select(".just_job_title").first(),
                    element.select(".job_org").first(),
                    element.select("a.job_link").first());
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.ownText());
        jobsInform.setPublicationLink(linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}