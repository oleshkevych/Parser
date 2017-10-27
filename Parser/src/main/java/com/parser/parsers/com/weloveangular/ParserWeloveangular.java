package com.parser.parsers.com.weloveangular;


import com.parser.entity.JobsInform;
import com.parser.entity.ParserMain;
import com.parser.parsers.util.DateUtil;
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
public class ParserWeloveangular implements ParserMain {

    private static final String START_LINK = "http://www.weloveangular.com/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();

    public ParserWeloveangular() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {

            Document doc = Jsoup.connect(START_LINK)
                    .validateTLSCertificates(true)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements tables2 = doc.select(".stream-job");
            runParse(tables2);
        } catch (IOException e) {
            e.printStackTrace();
            jobsInforms = null;
        }

    }

    private void runParse(Elements tables2) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        for (Element element : tables2) {

            String stringDate = element.select(".date").first().text() + " " + DateUtil.getCurrentYear();
            if (stringDate.length() < 11) {
                String day = stringDate.substring(4);
                stringDate = stringDate.substring(0, 4) + day;
            }
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(element.select("span[itemprop=\"jobLocation\"]").first(), element.select(".media-heading").first(),
                        element.select("div > strong").first(), datePublished, element.select(".media-heading a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        if (company != null) {
            jobsInform.setCompanyName(company.text());
        } else {
            jobsInform.setCompanyName("");
        }
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
