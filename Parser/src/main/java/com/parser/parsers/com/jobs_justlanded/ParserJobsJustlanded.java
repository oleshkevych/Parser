package com.parser.parsers.com.jobs_justlanded;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import com.parser.parsers.PrintDescription;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.parser.parsers.PhantomJSStarter.startPhantom;

/**
 * Created by rolique_pc on 1/3/2017.
 */
public class ParserJobsJustlanded implements ParserMain {

    private String startLink = "https://jobs.justlanded.com/en/TTTTT/Web-development?q_cid=10&q_ji=&q_jt=&q_k=&q_l%5B%5D=en&q_l_opt=all&q_salary_max=&q_salary_min=&q_salary_period_id=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;
    private WebDriver ghostDriver;

    public ParserJobsJustlanded() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        try {
            parser();
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
        return jobsInforms;
    }

    private Document startGhost(String link) {
        if (ghostDriver == null) ghostDriver = startPhantom();
        ghostDriver.get(link);
        WebDriverWait wdw = new WebDriverWait(ghostDriver, 15);
        wdw.until(ExpectedConditions.visibilityOfElementLocated(By.className("listings")));
        return Jsoup.parse(ghostDriver.getPageSource());
    }

    private void parser() {
        List<String> countries = new ArrayList<>();
        countries.add("Austria");
        countries.add("Belgium");
        countries.add("Canada");
        countries.add("Czech-Republic");
        countries.add("Denmark");
        countries.add("Finland");
        countries.add("Germany");
        countries.add("Ireland");
        countries.add("Israel");
        countries.add("Netherlands");
        countries.add("Norway");
        countries.add("Sweden");
        countries.add("Switzerland");
        countries.add("United-Kingdom");
        countries.add("United-States");
        for (String s : countries) {
            doc = startGhost(startLink.replace("TTTTT", s));
            Elements tables2 = doc.select(".listings li .item-content-wrap");
            runParse(tables2, s);
        }
    }

    private void runParse(Elements tables2, String s) {
        System.out.println("text date : " + tables2.size());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (Element element : tables2) {
            Date datePublished = null;
            String stringDate = element.select("time").first().attr("datetime").substring(0, 10);
            try {
                datePublished = formatter.parse(stringDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            objectGenerator(s,
                    element.select(".title-wrapper").first(),
                    element.select("a").first(),
                    datePublished);
        }
    }

    private void objectGenerator(String place, Element headPost, Element linkDescription, Date date) {
        try {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPlace(place);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setPublishedDate(date);
            jobsInform.setCompanyName("");
            jobsInform.setPublicationLink(linkDescription.attr("href"));
            if (dateClass.dateChecker(jobsInform.getPublishedDate()) && !jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        } catch (NullPointerException e) {
            System.out.println(place + " " + date);
        }
    }
}
