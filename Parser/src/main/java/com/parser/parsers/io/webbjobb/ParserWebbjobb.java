package com.parser.parsers.io.webbjobb;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/13/2016.
 */
public class ParserWebbjobb implements ParserMain {

    private static final String START_LINK = "https://webbjobb.io/sok/dela?freetext=TTTTT&exclude_headhunters=0";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;
    private WebDriver ghostDriver;

    public ParserWebbjobb() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        try {
            parser(0);
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
        return jobsInforms;
    }

    private Document startGhost(String link) {
        if (ghostDriver == null) ghostDriver = PhantomJSStarter.startPhantom();
        ghostDriver.get(link);
        WebDriverWait wdw = new WebDriverWait(ghostDriver, 15);
        wdw.until(ExpectedConditions.visibilityOfElementLocated(By.className("job")));
        return Jsoup.parse(ghostDriver.getPageSource());
    }

    private void parser(int index) {
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
        try {
            for (int i = index; i < stringCat.size(); i++) {
                index = i;
                doc = startGhost(START_LINK.replace("TTTTT", stringCat.get(i)));
                Elements elements = doc.select(".job");
                runParse(elements);
                if (elements.size() == 0) {
                    c++;
                }
            }
        } catch (Exception e) {
            if (e.getMessage().startsWith("Timed out after 15") && index < stringCat.size() - 1) {
                parser(index + 1);
            } else {
                throw e;
            }
        }
        if (c == stringCat.size()) {
            jobsInforms = null;
        }
    }

    private void runParse(Elements tables2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date datePublished = null;
        for (Element element : tables2) {

            String stringDate = element.select(".job-published").text().substring(1);
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(element.select(".job-company-city").first(), element.select(".job-title").first(),
                        element.select(".job-company-city").first(), datePublished, element.select(".job-title a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        String companyName = company.text();
        String city = companyName.substring(companyName.indexOf(",") + 1);
        companyName = companyName.substring(0, companyName.indexOf(","));
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(companyName);
        jobsInform.setPlace(city);
        jobsInform.setPublicationLink(linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
