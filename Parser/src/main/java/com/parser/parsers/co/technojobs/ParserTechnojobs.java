package com.parser.parsers.co.technojobs;

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
 * Created by rolique_pc on 12/16/2016.
 */
public class ParserTechnojobs implements ParserMain {

    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private DateGenerator dateClass;

    public ParserTechnojobs() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

        Date datePublished = null;
        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("https://www.technojobs.co.uk/search.phtml?page=0&row_offset=10&keywords=Drupal&salary=0&jobtype=all&postedwithin=all&radius=100");
        startLinksList.add("https://www.technojobs.co.uk/search.phtml?page=0&row_offset=10&keywords=Angular&salary=0&jobtype=all&postedwithin=all&radius=100");
        startLinksList.add("https://www.technojobs.co.uk/search.phtml?page=0&row_offset=10&keywords=React&salary=0&jobtype=all&postedwithin=all&radius=100");
        startLinksList.add("https://www.technojobs.co.uk/search.phtml?page=0&row_offset=10&keywords=Meteor&salary=0&jobtype=all&postedwithin=all&radius=100");
        startLinksList.add("https://www.technojobs.co.uk/search.phtml?page=0&row_offset=10&keywords=Node&salary=0&jobtype=all&postedwithin=all&radius=100");
        startLinksList.add("https://www.technojobs.co.uk/search.phtml?page=0&row_offset=10&keywords=Frontend&salary=0&jobtype=all&postedwithin=all&radius=100");
        startLinksList.add("https://www.technojobs.co.uk/search.phtml?page=0&row_offset=10&keywords=JavaScript&salary=0&jobtype=all&postedwithin=all&radius=100");
        startLinksList.add("https://www.technojobs.co.uk/search.phtml?page=0&row_offset=10&keywords=iOS&salary=0&jobtype=all&postedwithin=all&radius=100");
        startLinksList.add("https://www.technojobs.co.uk/search.phtml?page=0&row_offset=10&keywords=mobile&salary=0&jobtype=all&postedwithin=all&radius=100");

        for (String link : startLinksList) {
            int count = 1;
            int size = 0;
            String loadLink = link;
            do {
                try {
                    loadLink = loadLink.replaceFirst(("page=" + (count - 1)), ("page=" + (count)));

                    datePublished = null;
                    Document doc = Jsoup.connect(loadLink)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    count++;
                    Elements tables = doc.select(".jobs-list .jobbox");
                    if (!tables.isEmpty())
                        datePublished = runParse(tables);
                    size = tables.size();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            while (size > 0 && dateClass.dateChecker(datePublished) && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 200);
        }
    }

    private Date runParse(Elements tables2) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (Element element : tables2) {
            String stringDate = element.select("time").attr("datetime");
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(element.select(".job-details").first(),
                        element.select(".job-ti").first(),
                        datePublished,
                        element.select(".job-ti a").last());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName("");
            String placeString = place.text();
            placeString = placeString.substring(placeString.indexOf("Location:") + 9, placeString.indexOf("Salary"));
            jobsInform.setPlace(placeString);
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }
}
