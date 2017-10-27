package com.parser.parsers.ca.wowjobs;

import com.parser.entity.JobsInform;
import com.parser.entity.ParserMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/22/2016.
 */
public class ParserWowjobs implements ParserMain {

    private static final String START_LINK = "http://www.wowjobs.ca/BrowseResults.aspx?q=TTTTTT&t=7&s=d";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();

    public ParserWowjobs() {
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
        for (String s : stringCat) {
            String loadLink = START_LINK.replace("TTTTTT", s);

            try {

                Document doc = Jsoup.connect(loadLink)
                        .validateTLSCertificates(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();

                Elements tables2 = doc.select(".result");
                runParse(tables2);

                int counter = 1;
                int index = 10;
                while (jobsInforms.size() < (stringCat.indexOf(s) + 1) * 500 && counter != 0) {
                    doc = Jsoup.connect(loadLink + "&start=" + index)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    Elements tables1 = doc.select(".result");
                    counter = runParse(tables1);
                    index += 10;
                }
            } catch (IOException e) {
                e.printStackTrace();
                c++;
            }
        }
        if (c == stringCat.size()) {
            jobsInforms = null;
        }
    }

    private int runParse(Elements tables2) {
        int newCounter = 0;
        for (Element element : tables2) {
            Date datePublished = null;
            String stringDate = element.select(".tags").text();

            if (stringDate.contains("day")) {
                String date = stringDate.replaceAll("[^0-9]", "");
                if ((date.length() != 0)) {
                    datePublished = new Date(new Date().getTime() - Integer.parseInt(date) * 24 * 3600 * 1000);
                }
            } else if (stringDate.contains("hour") || stringDate.contains("min")) {
                datePublished = new Date();
            }
            newCounter += objectGenerator(element.select(".location").first(),
                    element.select("a").first(),
                    element.select(".employer").first(),
                    element.select("a").first(),
                    datePublished);
        }
        return newCounter;
    }

    private int objectGenerator(Element place, Element headPost, Element company, Element linkDescription, Date datePublished) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost != null ? headPost.text() : "");
        jobsInform.setCompanyName(company != null ? company.text() : "");
        jobsInform.setPlace((place != null ? place.text() : ""));
        jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
        if (jobsInforms.contains(jobsInform)) return 0;
        jobsInforms.add(jobsInform);
        return 1;
    }
}
