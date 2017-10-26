package com.parser.parsers.com.jobs_smashingmagazine;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
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
public class ParserJobsSmashingmagazine implements ParserMain {

    private final static String START_LINK = "http://jobs.smashingmagazine.com/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private DateGenerator dateClass;
    private WebDriver ghostDriver;

    public ParserJobsSmashingmagazine() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private Document startGhost(String link) {

        if (ghostDriver == null) ghostDriver = startPhantom();
        ghostDriver.get(link);
        new WebDriverWait(ghostDriver, 15);
        return Jsoup.parse(ghostDriver.getPageSource());
    }

    private void parser() {
//        try {


//         need http protocol
        Document doc = startGhost(START_LINK);
//                    Jsoup
//                    .connect(START_LINK)
//                    .timeout(6000)
//                    .method(Connection.Method.GET)
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .execute()
//                    .parse();
        Elements tables2 = doc.select(".entry-list").first().children();
        runParse(tables2, 0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private void runParse(Elements tables2, int counter) {
        try {
            System.out.println("text date : " + tables2.size());
            for (int i = counter; i < tables2.size(); i += 1) {
                objectGenerator(tables2.get(i).select(".entry-company").first(), tables2.get(i).select(".entry").first().child(0),
                        tables2.get(i).select(".entry-company strong").first(), tables2.get(i).select("a").first());
            }
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {

        JobsInform jobsInform = new JobsInform();
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.ownText());
        jobsInform.setPublicationLink(linkDescription.attr("href"));
        jobsInform = getDescription(linkDescription.attr("href"), jobsInform);
        if (!jobsInforms.contains(jobsInform) && dateClass.dateChecker(jobsInform.getPublishedDate())) {
            jobsInforms.add(jobsInform);
        }
    }

    private JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {
//        System.out.println("text link1 : " + linkToDescription);

        try {
            Document document = startGhost(linkToDescription);


            Elements tablesDescription = document.select("article").first().children();
            Elements tablesHead = document.select("article h2");
            Elements tablesDate = document.select(".date");

            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(tablesHead.first()));
            SimpleDateFormat formatter = new SimpleDateFormat("MMM d, y");
            String stringDate = tablesDate.text();
            try {
                Date date = formatter.parse(stringDate);
                jobsInform.setPublishedDate(date);
            } catch (ParseException e) {
                System.out.println("ParserJobsSmashingmagazine" + jobsInform.getPublicationLink());
                e.printStackTrace();
            }

            for (int i = 0; i < tablesDescription.size(); i++) {
                if (tablesDescription.get(i).tagName().equals("p")) {
                    list.add(addParagraph(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().equals("ul") && !tablesDescription.get(i).hasClass("postmetadata")) {
                    list.add(addList(tablesDescription.get(i)));
                }

            }

            list.add(null);
            jobsInform.setOrder(list);


            return jobsInform;
        } catch (Exception e) {
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
