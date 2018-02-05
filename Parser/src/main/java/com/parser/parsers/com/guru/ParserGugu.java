package com.parser.parsers.com.guru;

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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.parser.parsers.PhantomJSStarter.startPhantom;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserGugu implements ParserMain {
    private String startLink = "https://www.guru.com/d/freelancers/c/web-software-it/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;
    private WebDriver ghostDriver;

    public ParserGugu() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private Document startGhost(String link) {
        if (ghostDriver == null) ghostDriver = startPhantom();
        ghostDriver.get(link);
        WebDriverWait wait = new WebDriverWait(ghostDriver, 15);
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("serviceItem")));
        return Jsoup.parse(ghostDriver.getPageSource());
    }

    private void parser() {

        try {
            List<String> startLinksList = new ArrayList<>();
            startLinksList.add("https://www.guru.com/d/freelancers/c/web-software-it/q/drupal/");
            startLinksList.add("https://www.guru.com/d/freelancers/c/web-software-it/q/angular/");
            startLinksList.add("https://www.guru.com/d/freelancers/c/web-software-it/q/react/");
            startLinksList.add("https://www.guru.com/d/freelancers/c/web-software-it/q/meteor/");
            startLinksList.add("https://www.guru.com/d/freelancers/c/web-software-it/q/node/");
            startLinksList.add("https://www.guru.com/d/freelancers/c/web-software-it/q/frontend/");
            startLinksList.add("https://www.guru.com/d/freelancers/c/web-software-it/q/javascript/");
            startLinksList.add("https://www.guru.com/d/freelancers/c/web-software-it/q/ios/");
            startLinksList.add("https://www.guru.com/d/freelancers/c/web-software-it/q/mobile/");
            for (String link : startLinksList) {
                int count = 1;
                do {
                    try {
                        doc = startGhost(link + "pg/" + count + "/");
//                        doc = Jsoup.connect(link + "pg/" + count + "/")
//                                .timeout(10000)
//                                .method(Connection.Method.GET)
//                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                                .execute()
//                                .parse();

                        Elements tables1 = doc.select(".serviceItem");
                        runParse(tables1, count);
                        count++;

                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
                while (jobsInforms.size() < startLinksList.indexOf(link) * 200 && count < 20);
            }
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
    }

    private void runParse(Elements tables2, int counter) {
        System.out.println(counter + " text date : " + jobsInforms.size());
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
