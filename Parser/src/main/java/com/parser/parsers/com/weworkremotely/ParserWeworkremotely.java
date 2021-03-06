package com.parser.parsers.com.weworkremotely;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
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

    private String startLink1 = "https://weworkremotely.com/categories/6-devops-sysadmin/jobs#intro";
    private String startLink2 = "https://weworkremotely.com/categories/2-programming/jobs#intro";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserWeworkremotely() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {

            doc = Jsoup.connect(startLink1)
                    .timeout(6000)
                    .method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .execute()
                    .parse();

            Elements tables2 = doc.select(".jobs li");
            runParse(tables2, 0);
            doc = Jsoup.connect(startLink2)
                    .timeout(6000)
                    .method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .execute()
                    .parse();

            Elements tables = doc.select(".jobs li");
            runParse(tables, 0);
        } catch (IOException e) {
            e.printStackTrace();
            jobsInforms = null;
        }

    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        for (int i = counter; i < tables2.size(); i += 1) {
            String stringDate = "";
            try {
                stringDate = tables2.get(i).select(".date").first().text() + " " + calendar.get(Calendar.YEAR);
            } catch (NullPointerException n) {
                System.out.println("text date : " + tables2.get(i));
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
                objectGenerator(tables2.get(i).select(".location").first(), tables2.get(i).select(".title").first(),
                        tables2.get(i).select(".company").first(), datePublished, tables2.get(i).select("a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }

        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished) && jobsInforms.size() < 100) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
            jobsInform = getDescription(linkDescription.attr("abs:href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .timeout(6000)
                    .method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .execute()
                    .parse();


            Elements tablesDescription = document.select(".listing-container").first().children();
            Element compPlace = document.select(".location").first();
            Element compName = document.select(".company").first();
            jobsInform.setCompanyName(compName.text());
            jobsInform.setPlace(compPlace.text().replaceFirst("Headquarters: ", ""));
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(document.select(".listing-header-container h1").first()));
            for (int i = 0; i < tablesDescription.size(); i++) {

                if (tablesDescription.get(i).select("strong").size() > 0) {
                    if (tablesDescription.get(i).select("strong").text().length() > 0
                            && tablesDescription.get(i).select("strong").text().length() == tablesDescription.get(i).text().length()) {
                        list.add(null);
                        list.add(addHead(tablesDescription.get(i).select("strong").first()));
                    } else if (tablesDescription.get(i).text().length() > 0) {
                        list.add(addParagraph(tablesDescription.get(i)));
                    }
                } else if (tablesDescription.get(i).select("div div").size() > 0) {
                    for (Element ul : tablesDescription.get(i).select("div div")) {
                        list.add(addList(ul));
                    }
                } else if (tablesDescription.get(i).select("ul").size() > 0) {

                    for (Element ul : tablesDescription.get(i).select("ul")) {
                        list.add(addList(ul));
                    }
                } else if (tablesDescription.get(i).text().length() > 0) {
                    list.add(addParagraph(tablesDescription.get(i)));

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

        ListImpl list = new ListImpl();
        list.setTextFieldImpl(element.text());
        return list;
//        }
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
