package com.parser.parsers.ch.jobs;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
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
 * Created by rolique_pc on 12/8/2016.
 */
public class ParserJobs implements ParserMain {

    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserJobs() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("http://www.jobs.ch/en/vacancies/?page=1&sort-by=date&term=Drupal+&web-results=1");
        startLinksList.add("http://www.jobs.ch/en/vacancies/?page=1&sort-by=date&term=Angular+&web-results=1");
        startLinksList.add("http://www.jobs.ch/en/vacancies/?page=1&sort-by=date&term=React+&web-results=1");
        startLinksList.add("http://www.jobs.ch/en/vacancies/?page=1&sort-by=date&term=Meteor+&web-results=1");
        startLinksList.add("http://www.jobs.ch/en/vacancies/?page=1&sort-by=date&term=Node+&web-results=1");
        startLinksList.add("http://www.jobs.ch/en/vacancies/?page=1&sort-by=date&term=Frontend+&web-results=1");
        startLinksList.add("http://www.jobs.ch/en/vacancies/?page=1&sort-by=date&term=iOS+&web-results=1");
        startLinksList.add("http://www.jobs.ch/en/vacancies/?page=1&sort-by=date&term=JavaScript+&web-results=1");
        startLinksList.add("http://www.jobs.ch/en/vacancies/?page=1&sort-by=date&term=mobile+&web-results=1");

        for (String link : startLinksList) {

            boolean isContinue = false;
            int count = 1;
            String nextLink = link;
            do {
                try {
                    if (count > 1)
                        nextLink = nextLink.replace("page=" + (count - 1), "page=" + count);

                    doc = Jsoup.connect(nextLink)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    Elements elements = doc.select(".serp-item-job-info");
                    isContinue = runParse(elements);
                    count++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            while (isContinue && jobsInforms.size() < ((startLinksList.indexOf(link) + 1) * 200));
        }
    }

    private boolean runParse(Elements elements) {
        System.out.println("text date : " + jobsInforms.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd.MM.yyyy");
        int newJobs = 0;
        for (Element element : elements) {
            datePublished = null;
            String stringDate = element.select(".serp-item-meta .badge-pool .outline").first().text();

            try {
                datePublished = formatter.parse(stringDate);

            } catch (ParseException e) {
                try {
                    datePublished = formatter1.parse(stringDate);

                } catch (ParseException e1) {
                    System.out.println(e1.getMessage());
                }
            }
            newJobs += objectGenerator(element.select(".serp-item-head-2").first().children().last(),
                    element.select(".serp-item-head-1").first(),
                    element.select(".x--company-link").first(),
                    datePublished,
                    element.select(".x--job-link").first());
        }
        return dateClass.dateChecker(datePublished) && newJobs != 0;
    }

    private int objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(headPost.text());
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
            return 1;
        }
        return 0;
    }
}
