package com.parser.parsers.com.dutchstartupjobs;

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
public class ParserDutchstartupjobs implements ParserMain {

    private static final String startLink = "https://dutchstartupjobs.com/?post_type=noo_job&s=&category%5B%5D=software-development-3&type=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private DateGenerator dateClass;

    public ParserDutchstartupjobs() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {

            Document doc = Jsoup.connect(startLink)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements tables2 = doc.select("article");
            runParse(tables2);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void runParse(Elements tables2) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (Element element : tables2) {
            String stringDate = element.select("time").first().attr("datetime").substring(0, 10);
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(element.select(".job-location").first(),
                        element.select(".loop-item-title").first(),
                        element.select(".job-company").first(),
                        datePublished,
                        element.select(".loop-item-title a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
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
