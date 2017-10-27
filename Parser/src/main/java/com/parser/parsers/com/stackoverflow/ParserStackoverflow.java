package com.parser.parsers.com.stackoverflow;


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
 * Created by rolique_pc on 12/7/2016.
 */
public class ParserStackoverflow implements ParserMain {

    //    private String startLink = "https://stackoverflow.com/jobs?sort=p";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserStackoverflow() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("https://stackoverflow.com/jobs?sort=p&q=Drupal");
        startLinksList.add("https://stackoverflow.com/jobs?sort=p&q=Angular");
        startLinksList.add("https://stackoverflow.com/jobs?sort=p&q=React");
        startLinksList.add("https://stackoverflow.com/jobs?sort=p&q=Meteor");
        startLinksList.add("https://stackoverflow.com/jobs?sort=p&q=Node");
        startLinksList.add("https://stackoverflow.com/jobs?sort=p&q=Frontend");
        startLinksList.add("https://stackoverflow.com/jobs?sort=p&q=JavaScript");
        startLinksList.add("https://stackoverflow.com/jobs?sort=p&q=iOS");
        startLinksList.add("https://stackoverflow.com/jobs?sort=p&q=mobile");

        int c = 0;
        for (String link : startLinksList) {
            int counter = 1;
            Date date = null;
            int countNew = 0;
            do
                try {

                    doc = Jsoup.connect(link + "&pg=" + counter)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    Elements tables2 = doc.select(".listResults .-item");
                    countNew = tables2.size();
                    date = runParse(tables2);
                    counter++;
                } catch (IOException e) {
                    e.printStackTrace();
                    c++;
                } while (countNew != 0 && dateClass.dateChecker(date));
        }
        if (c == startLinksList.size()) {
            jobsInforms = null;
        }
    }

    private Date runParse(Elements tables2) {
        Date datePublished = null;
        for (Element element : tables2) {
            String stringDate = element.select(".-posted-date").text();
            datePublished = DateUtil.getDateWithOneLetter(stringDate);
            objectGenerator(element.select(".-company .-location").first(),
                    element.select(".job-link").first(),
                    element.select(".-company .-name").first(),
                    datePublished,
                    element.select(".job-link").first());
        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
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
