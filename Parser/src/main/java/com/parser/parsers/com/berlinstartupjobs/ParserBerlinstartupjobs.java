package com.parser.parsers.com.berlinstartupjobs;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PrintDescription;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.print.PrinterGraphicsDevice;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rolique_pc on 12/8/2016.
 */
public class ParserBerlinstartupjobs implements ParserMain {

    private String startLink1 = "http://berlinstartupjobs.com/engineering/";
    private String startLink2 = "http://berlinstartupjobs.com/design-ux/";
    private String startLink3 = "http://berlinstartupjobs.com/contracting-positions/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserBerlinstartupjobs() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser(startLink1);
        parser(startLink2);
        parser(startLink3);
        return jobsInforms;
    }

    private void parser(String startLink) {
        try {
            // need http protocol
            doc = Jsoup.connect(startLink)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements tables2 = doc.select(".product-listing-link-block");
            runParse(tables2);

            Date datePublished = null;
            int count = 2;
            do {
                try {
                    datePublished = null;

                    // need http protocol
                    doc = Jsoup.connect(startLink + "page/" + count + "/")
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    Elements tables1 = doc.select(".product-listing-link-block");
                    datePublished = runParse(tables1);
                    count++;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } while (dateClass.dateChecker(datePublished));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Date runParse(Elements tables2) {
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, y");
        for (Element element : tables2) {
            String stringDate = element.select(".product-listing-date").text();
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(element.select(".product-listing-h2").first(),
                        element.select(".category-tag").first(),
                        datePublished,
                        element.select(".product-listing-h2 a").first());
            } catch (ParseException e) {
                System.out.println("ParserBerlinstartupjobs " + e.getMessage());
                System.out.println(stringDate);
            }
        }
        return datePublished;
    }

    private void objectGenerator(Element headPost, Element company, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text().substring(0, company.text().indexOf("//")));
        jobsInform.setPlace("");
        jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
