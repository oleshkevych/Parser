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


    private String startLink = "https://webbjobb.io/sok/dela?freetext=TTTTT&exclude_headhunters=0";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;
    private WebDriver ghostDriver;

    public ParserWebbjobb() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        ghostDriver = PhantomJSStarter.startPhantom();
        try {
            parser(0);
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
        return jobsInforms;
    }

    private Document startGhost(String link, WebDriver ghostDriver){
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
                System.out.println(startLink.replace("TTTTT", stringCat.get(i)));
                doc = startGhost(startLink.replace("TTTTT", stringCat.get(i)), ghostDriver);
                Elements tables3 = doc.select(".job");
                runParse(tables3, 0);
                if (tables3.size() == 0) {
                    c++;
                }
            }
        } catch (Exception e) {
            if (e.getMessage().startsWith("Timed out after 15") && index < stringCat.size() - 1) {
                parser (index + 1);
            } else {
                throw e;
            }
        }
        if (c == stringCat.size()) {
            jobsInforms = null;
        }
    }

    private Date runParse(Elements tables2, int counter) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date datePublished = null;
        for (int i = counter; i < tables2.size(); i += 1) {
            datePublished = null;

            String stringDate = tables2.get(i).select(".job-published").text().substring(1);
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(tables2.get(i).select(".job-company-city").first(), tables2.get(i).select(".job-title").first(),
                        tables2.get(i).select(".job-company-city").first(), datePublished, tables2.get(i).select(".job-title a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }

        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished)) {
            String companyName = company.text();
            String city = companyName.substring(companyName.indexOf(",") + 1);
            companyName = companyName.substring(0, companyName.indexOf(","));
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(companyName);
            jobsInform.setPlace(city);
            jobsInform.setPublicationLink(linkDescription.attr("href"));
            jobsInform = getDescription(linkDescription.attr("href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(true)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();
            Elements tablesDescription = document.select(".job-info-section").first().children();
            Elements tablesHead = document.select(".section-title");
            List<ListImpl> list = new ArrayList<ListImpl>();


            list.add(addHead(tablesHead.first()));
            for (int i = 0; i < tablesDescription.size(); i++) {
                if (tablesDescription.get(i).tagName().equals("p")) {
                    list.add(addParagraph(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().equals("ul")) {
                    list.add(addList(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().contains("h")) {
                    list.add(addHead(tablesDescription.get(i)));
                }
            }

            list.add(null);
            jobsInform.setOrder(list);


            return jobsInform;
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage() + " " + jobsInform.getPublicationLink());
            e.printStackTrace();
            return jobsInform;
        }

    }

    private static ListImpl addHead(Element element) {
        ListImpl list = new ListImpl();
        list.setListHeader(element.text());
        return list;
    }

    private static ListImpl addParagraph(Element element) {
        if (element.select("strong").size() > 0) {
            return addHead(element.select("strong").first());
        } else {


            ListImpl list = new ListImpl();
            list.setTextFieldImpl(element.text());
            return list;
        }
    }

    private static ListImpl addList(Element element) {
        ListImpl list = new ListImpl();
        List<String> strings = new ArrayList<String>();
        for (Element e : element.getAllElements()) {
            strings.add(e.text());
        }
        list.setListItem(strings);
        return list;
    }
}
