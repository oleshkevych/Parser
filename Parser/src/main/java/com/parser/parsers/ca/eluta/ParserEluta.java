package com.parser.parsers.ca.eluta;

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
 * Created by rolique_pc on 12/13/2016.
 */
public class ParserEluta implements ParserMain {


    private static final String startLink = "http://www.eluta.ca/search?q=TTTTTT&pg=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserEluta() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
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
            try {
                int count = 1;
                int newJobsCount = 0;
                do {

                    doc = Jsoup.connect(startLink.replace("TTTTTT", s) + count)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    Elements elements = doc.select(".organic-job");
                    newJobsCount = runParse(elements);

                    count++;

                } while (jobsInforms.size() < (stringCat.indexOf(s) + 1) * 300 && newJobsCount != 0);
            } catch (IOException e) {
                e.printStackTrace();
                c++;
            }
        }
        if (c == stringCat.size()) {
            jobsInforms = null;
        }
    }

    private int runParse(Elements elements) {
        System.out.println("text date : " + jobsInforms.size());
        int counter = 0;
        for (int i = 0; i < elements.size(); i++) {
            Date datePublished = null;

            String stringDate = elements.get(i).select(".lastseen").text();
            if (!stringDate.contains("+")) {
                String date = stringDate.replaceAll("[^0-9]", "");
                if ((date.length() != 0)) {
                    datePublished = new Date(new Date().getTime() - Integer.parseInt(date) * 24 * 3600 * 1000);
                }
            }

//            if (stringDate.contains("minut") || stringDate.contains("hour")) {
//                datePublished = new Date();
//            } else if (stringDate.contains("yesterday") || stringDate.contains("1 day")) {
//                datePublished = new Date(new Date().getTime() - 1 * 24 * 3600 * 1000);
//            } else if (stringDate.contains("2 day")) {
//                datePublished = new Date(new Date().getTime() - 2 * 24 * 3600 * 1000);
//            } else if (stringDate.contains("3 day")) {
//                datePublished = new Date(new Date().getTime() - 3 * 24 * 3600 * 1000);
//            } else if (stringDate.contains("4 day")) {
//                datePublished = new Date(new Date().getTime() - 4 * 24 * 3600 * 1000);
//            } else if (stringDate.contains("5 day")) {
//                datePublished = new Date(new Date().getTime() - 5 * 24 * 3600 * 1000);
//            } else if (stringDate.contains("6 day")) {
//                datePublished = new Date(new Date().getTime() - 6 * 24 * 3600 * 1000);
//            }

            counter += objectGenerator(elements.get(i).select(".location").first(),
                    elements.get(i).select(".lk-job-title").first(),
                    elements.get(i).select(".lk-employer").first(),
                    datePublished,
                    elements.get(i).select(".lk-job-title").first());
        }
        return counter;
    }

    private int objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        String link = linkDescription.attr("onclick");
        link = link.substring(link.indexOf("'") + 1, link.lastIndexOf("'"));
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.ownText());
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink("http://www.eluta.ca" + link);
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
            return 1;
        }
        return 0;
    }
}
