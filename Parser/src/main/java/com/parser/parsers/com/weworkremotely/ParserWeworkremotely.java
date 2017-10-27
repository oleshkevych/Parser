package com.parser.parsers.com.weworkremotely;

import com.parser.entity.JobsInform;
import com.parser.entity.ParserMain;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/8/2016.
 */
public class ParserWeworkremotely implements ParserMain {

    private static final String START_LINK_1 = "https://weworkremotely.com/categories/6-devops-sysadmin/jobs#intro";
    private static final String START_LINK_2 = "https://weworkremotely.com/categories/2-programming/jobs#intro";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();

    public ParserWeworkremotely() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {

            Document doc = Jsoup.connect(START_LINK_1)
                    .timeout(6000)
                    .method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .execute()
                    .parse();

            Elements tables2 = doc.select(".jobs li");
            runParse(tables2);
            doc = Jsoup.connect(START_LINK_2)
                    .timeout(6000)
                    .method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .execute()
                    .parse();

            Elements tables = doc.select(".jobs li");
            runParse(tables);
        } catch (IOException e) {
            e.printStackTrace();
            jobsInforms = null;
        }

    }

    private void runParse(Elements tables2) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        for (Element element : tables2) {
            if (element.text().contains("Back to all jobs")) continue;
            String stringDate = "";
            try {
                stringDate = element.select(".date").first().text() + " " + calendar.get(Calendar.YEAR);
            } catch (NullPointerException n) {
                System.out.println("text date : " + element);
                stringDate = "Dec 1 1999";
            }
            try {
                datePublished = formatter.parse(stringDate);
                Calendar calendarPublished = Calendar.getInstance();
                calendarPublished.setTime(datePublished);
                if (calendar.get(Calendar.MONTH) < calendarPublished.get(Calendar.MONTH)) {
                    stringDate = stringDate.replace(calendar.get(Calendar.YEAR) + "", (calendar.get(Calendar.YEAR) - 1) + "");
                    datePublished = formatter.parse(stringDate);
                }
                objectGenerator(element.select(".location").first(),
                        element.select(".title").first(),
                        element.select(".company").first(),
                        datePublished,
                        element.select("a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace("");
        jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
