package com.parser.parsers.io.wfh;

import com.parser.entity.JobsInform;
import com.parser.entity.ParserMain;
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
 * Created by rolique_pc on 12/5/2016.
 */
public class ParserWFH implements ParserMain {

    private static final String START_LINK = "https://www.wfh.io/latest-remote-jobs";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();

    public ParserWFH() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {

            Document doc = Jsoup.connect(START_LINK)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();
            Elements tables2 = doc.select("tbody tr td");
            runParse(tables2, 0);
        } catch (IOException e) {
            e.printStackTrace();
            jobsInforms = null;
        }

    }

    private void runParse(Elements tables2, int counter) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = counter; i < tables2.size(); i += 3) {

            Date datePublished = null;
            try {
                datePublished = formatter.parse(tables2.get(i).text());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
                if (tables2.get(i).text().equals("Click for more Remote Software Development jobs")) {
                    Document doc1 = null;
                    try {
                        doc1 = Jsoup.connect(tables2.get(i).select("a").attr("abs:href")).get();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    runParse(tables2, i + 1);
                    if (doc1 != null) {
                        runParse(doc1.select("tbody tr td"), 0);
                    }
                    break;
                }
            }
            objectGenerator(tables2.select("strong").first(), tables2.get(i + 1), tables2.get(i + 2), datePublished);
        }
    }

    private void objectGenerator(Element table1, Element table2, Element table3, Date datePublished) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(table2.text());
        jobsInform.setPlace(table3.text());
        jobsInform.setCompanyName(table1.text());
        jobsInform.setPublicationLink(table2.getAllElements().get(2).attr("abs:href"));

        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
