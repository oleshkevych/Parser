package com.parser.parsers.com.careerbuilder;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
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
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserCareerbuilder implements ParserMain {
    private String startLink = "http://www.careerbuilder.com/jobs?posted=7";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;

    public ParserCareerbuilder() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {

        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("http://www.careerbuilder.com/jobs-frontend?posted=7&sort=date_desc");
        startLinksList.add("http://www.careerbuilder.com/jobs-Angular?posted=7&sort=date_desc");
        startLinksList.add("http://www.careerbuilder.com/jobs-React?posted=7&sort=date_desc");
        startLinksList.add("http://www.careerbuilder.com/jobs-Meteor?posted=7&sort=date_desc");
        startLinksList.add("http://www.careerbuilder.com/jobs-Node?posted=7&sort=date_desc");
        startLinksList.add("http://www.careerbuilder.com/jobs-Frontend?posted=7&sort=date_desc");
        startLinksList.add("http://www.careerbuilder.com/jobs-iOS?posted=7&sort=date_desc");
        startLinksList.add("http://www.careerbuilder.com/jobs-JavaScript?posted=7&sort=date_desc");
        startLinksList.add("http://www.careerbuilder.com/jobs-mobile?posted=7&sort=date_desc");


        for (String link : startLinksList) {

            try {
                doc = Jsoup.connect(link)
                        .validateTLSCertificates(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();
                Elements tables2 = doc.select(".jobs .job-row");
                runParse(tables2);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void runParse(Elements tables2) {

        Date datePublished = null;
        for (int i = 0; i < tables2.size(); i++) {
            String stringDate = tables2.get(i).select(".time-posted em").text();

            datePublished = DateUtil.getDate(stringDate);

            objectGenerator(tables2.get(i).select(".job-information .columns").get(2), tables2.get(i).select(".job-title").first(),
                    tables2.get(i).select(".job-information .columns").get(1), tables2.get(i).select("a").first(), datePublished);
        }

    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription, Date date) {
        if (new DateGenerator().dateChecker(date)) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(date);
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
