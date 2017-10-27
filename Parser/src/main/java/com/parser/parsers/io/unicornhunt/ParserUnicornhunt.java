package com.parser.parsers.io.unicornhunt;

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
 * Created by rolique_pc on 12/28/2016.
 */
public class ParserUnicornhunt implements ParserMain {

    private static final String START_LINK = "https://unicornhunt.io/?q=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;

    public ParserUnicornhunt() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> stringCat = new ArrayList<>();
        stringCat.add("drupal");
        stringCat.add("angular");
        stringCat.add("react");
        stringCat.add("meteor");
        stringCat.add("node");
        stringCat.add("frontend");
        stringCat.add("javascript");
        stringCat.add("ios");
        stringCat.add("mobile");

        int c = 0;
        int c1 = 0;
        for (String link : stringCat) {
            try {

                doc = Jsoup.connect(START_LINK + link)
                        .validateTLSCertificates(true)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();

                Elements tables2 = doc.select(".job-card");
                runParse(tables2);
                if (tables2.size() == 0) {
                    c1++;
                }
            } catch (IOException e) {
                e.printStackTrace();
                c++;
            }
        }
        if (c1 == stringCat.size() || c == stringCat.size()) {
            jobsInforms = null;
        }
    }

    private void runParse(Elements tables2) {
        System.out.println("text date : " + jobsInforms.size());
        Date datePublished = null;
        for (Element aTables2 : tables2) {
            String stringDate = aTables2.select(".job-overview__posted-at").text();
            datePublished = DateUtil.getDate(stringDate);
            objectGenerator(aTables2.select(".job-overview__rough-location").first(), aTables2.select(".job-overview__role-name").first(),
                    aTables2.select(".job-overview__company-name").first(), datePublished, aTables2.select(".job-overview__one-liner a").first());
        }
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
