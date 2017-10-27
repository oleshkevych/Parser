package com.parser.parsers.co.remote;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
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
 * Created by rolique_pc on 12/12/2016.
 */
public class ParserRemote implements ParserMain {

    private static final String START_LINK = "https://remote.co/remote-jobs/developer/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private DateGenerator dateClass;

    public ParserRemote() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {
            Document doc = Jsoup.connect(START_LINK)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();
            Elements tables1 = doc.select(".job_listings .job_listing");
            runParse(tables1, 0);

        } catch (IOException e) {
            e.printStackTrace();
            jobsInforms = null;
        }
    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        for (int i = counter; i < tables2.size(); i += 1) {
            String stringDate = tables2.get(i).select("date").first().text();
            datePublished = DateUtil.getDate(stringDate);
            objectGenerator(tables2.get(i).select(".position").first(),
                    tables2.get(i).select(".company").first(),
                    datePublished,
                    tables2.get(i).select("a").first());
        }
        return datePublished;
    }

    private void objectGenerator(Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace("");
            jobsInform.setPublicationLink(linkDescription.attr("href"));
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }
}
