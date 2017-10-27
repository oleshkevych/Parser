package com.parser.parsers.com.guru;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PrintDescription;
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
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserGugu implements ParserMain {
    private String startLink = "http://www.guru.com/d/freelancers/q/drupal/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserGugu() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("http://www.guru.com/d/freelancers/q/drupal/pg/1/");
        startLinksList.add("http://www.guru.com/d/freelancers/q/angular/pg/1/");
        startLinksList.add("http://www.guru.com/d/freelancers/q/react/pg/1/");
        startLinksList.add("http://www.guru.com/d/freelancers/q/meteor/pg/1/");
        startLinksList.add("http://www.guru.com/d/freelancers/q/node/pg/1/");
        startLinksList.add("http://www.guru.com/d/freelancers/q/frontend/pg/1/");
        startLinksList.add("http://www.guru.com/d/freelancers/q/javascript/pg/1/");
        startLinksList.add("http://www.guru.com/d/freelancers/q/ios/pg/1/");
        startLinksList.add("http://www.guru.com/d/freelancers/q/mobile/pg/1/");
        for (String link : startLinksList) {
            int count = 1;
            do {
                try {

                    doc = Jsoup.connect(link + "pg/" + count + "/")
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(15000)
                            .get();

                    Elements tables1 = doc.select(".serviceItem");
                    runParse(tables1, 0);
                    count++;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            while (jobsInforms.size() < startLinksList.indexOf(link) * 200);
        }
    }

    private void runParse(Elements tables2, int counter) {
        System.out.println("text date : " + jobsInforms.size());
        for (Element element : tables2) {
            objectGenerator(element.select(".countryInfo").first(),
                    element.select(".servTitle").first(),
                    element.select("[id='aSName']").first(),
                    element.select(".servTitle a").first());
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setPlace(place.ownText().substring(0, place.ownText().indexOf(" $")));
            jobsInform.setCompanyName(company.text());
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
    }
}
