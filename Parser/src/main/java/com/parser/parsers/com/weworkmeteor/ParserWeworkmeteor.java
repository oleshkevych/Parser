package com.parser.parsers.com.weworkmeteor;


import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/9/2016.
 */
public class ParserWeworkmeteor implements ParserMain {
    private String startLink = "https://www.weworkmeteor.com/jobs";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private DateGenerator dateClass;

    public ParserWeworkmeteor() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }


    private void parser() {

        Document doc = PhantomJSStarter.renderPage(startLink, false);
        Elements tables2 = doc.select(".jobSmall .panel-body");
        runParse(tables2);

    }

    private void runParse(Elements tables2) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/d/y");
        for (Element element : tables2) {

            String stringDate = element.select(".row").last().child(0).text();
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(element.select("div").first(),
                        element.select("a").first(),
                        element.select(".row").last().select("div").last(),
                        datePublished, element.select("a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished) && jobsInforms.size() < 100) {
            JobsInform jobsInform = new JobsInform();
            if (company.select(".fa-building").size() > 0 && company.select(".fa-map-marker").size() > 0) {
                String companyName = company.text().substring(0, company.text().indexOf("-"));
                String placeString = company.text().substring(company.text().indexOf("-") + 1);
                jobsInform.setCompanyName(companyName);
                jobsInform.setPlace(placeString);
            } else if (company.select(".fa-building").size() > 0) {
                jobsInform.setCompanyName(company.text());
            } else if (company.select(".fa-map-marker").size() > 0) {
                jobsInform.setPlace(company.text());
            }
            if (jobsInform.getCompanyName() == null)
                jobsInform.setCompanyName("");
            if (jobsInform.getPlace() == null)
                jobsInform.setPlace("");
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setPublicationLink(startLink.replaceFirst("/jobs", "") + linkDescription.attr("href"));
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }
}
