package com.parser.parsers.cc.startus;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/6/2016.
 */
public class ParserStartus implements ParserMain {

    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserStartus() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("https://www.startus.cc/jobs/design-ux");
        startLinksList.add("https://www.startus.cc/jobs/software");
        startLinksList.add("https://www.startus.cc/jobs/it-back-end");
        startLinksList.add("https://www.startus.cc/jobs/frontend");
        startLinksList.add("https://www.startus.cc/jobs/web");
        startLinksList.add("https://www.startus.cc/jobs/mobile");

        int c = 0;
        for (String link : startLinksList) {
            try {


                // need http protocol
                doc = Jsoup
                        .connect(link)
                        .timeout(6000)
                        .method(Connection.Method.GET)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .execute()
                        .parse();
                Elements tables2 = doc.select(".views-row");
                runParse(tables2, 0);

                Date datePublished = null;
                int count = 1;
                do {
                    try {

                        datePublished = null;
                        // need http protocol
                        doc = Jsoup.connect(link + "?search=&radius[0]=50&job_geo_location=&lat=&lon=&country=&administrative_area_level_1=&page=" + count)
                                .timeout(6000)
                                .method(Connection.Method.GET)
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                                .execute()
                                .parse();

                        Elements tables1 = doc.select(".views-row");
                        datePublished = runParse(tables1, 0);
                        count++;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                while (dateClass.dateChecker(datePublished) && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 200);

            } catch (IOException e) {
                e.printStackTrace();
                c++;
            }
        }
        if (c == startLinksList.size()) {
            jobsInforms = null;
        }
    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        for (int i = counter; i < tables2.size(); i += 1) {
            try {
                datePublished = formatter.parse(tables2.get(i).select(".date").text());
                objectGenerator(tables2.get(i).select(".description span").last(),
                        tables2.get(i).select(".node__title").first(),
                        tables2.get(i).select(".recruiter-company-profile-job-organization").first(),
                        datePublished,
                        tables2.get(i).select(".recruiter-job-link").first());
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
            jobsInform.setPlace(place.ownText());
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }
}
