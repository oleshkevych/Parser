package com.parser.parsers.com.virtualvocations;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
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
 * Created by rolique_pc on 12/6/2016.
 */
public class ParserVirtualvocations implements ParserMain {


    private String startLink = "https://www.virtualvocations.com/jobs";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserVirtualvocations() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    public List<JobsInform> getJobsInforms() {
        return jobsInforms;
    }

    private void parser() {
        try {


            // need http protocol
            doc = Jsoup.connect(startLink)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements tables2 = doc.select(".col-sm-6 .list-unstyled li");
            runParse(tables2, 0);

            Date datePublished = null;
            int count = 1;
            do {
                try {

                    datePublished = null;
                    // need http protocol
                    doc = Jsoup.connect(startLink + "/p-" + count)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    Elements tables1 = doc.select(".col-sm-6 .list-unstyled li");

                    datePublished = runParse(tables1, 0);
                    count += 10;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } while (dateClass.dateChecker(datePublished) && jobsInforms.size() < 100);
//
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text size : " + tables2.size());
        Date datePublished = null;
        for (int i = counter; i < tables2.size(); i += 1) {
            String stringDate = tables2.get(i).select(".default-orange").text();
            if (stringDate.contains("minut") || stringDate.contains("hour")) {
                datePublished = new Date();
            } else if (stringDate.contains("1 day")) {
                datePublished = new Date(new Date().getTime() - 1 * 24 * 3600 * 1000);
            } else if (stringDate.contains("2 day")) {
                datePublished = new Date(new Date().getTime() - 2 * 24 * 3600 * 1000);
            } else if (stringDate.contains("3 day")) {
                datePublished = new Date(new Date().getTime() - 3 * 24 * 3600 * 1000);
            } else if (stringDate.contains("4 day")) {
                datePublished = new Date(new Date().getTime() - 4 * 24 * 3600 * 1000);
            } else if (stringDate.contains("5 day")) {
                datePublished = new Date(new Date().getTime() - 5 * 24 * 3600 * 1000);
            } else if (stringDate.contains("6 day")) {
                datePublished = new Date(new Date().getTime() - 6 * 24 * 3600 * 1000);
            }
            objectGenerator(tables2.get(i).select("h2 a").first(), tables2.get(i).select("h2 a").first(),
                    tables2.get(i).select(".recruiter-company-profile-job-organization").first(), datePublished, tables2.get(i).select("h2 a").first());
        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished) && jobsInforms.size() < 100) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.attr("title"));
            jobsInform.setCompanyName("");
            jobsInform.setPlace(place.text().replaceFirst(headPost.attr("title"), ""));
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
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();


            Element tablesDescription = document.select("#job_details").first();
            String place = tablesDescription.select(".row").first().text();
            List<ListImpl> list = new ArrayList<ListImpl>();
            if (place.length() > 0) {
                jobsInform.setPlace(place.substring(place.indexOf("Location:"), place.indexOf("Compensation:"))
                        .replaceFirst("Location:", ""));
            }


            list.add(addHead(tablesDescription.select("h1").first()));

            Elements ps = tablesDescription.select("p");
            Elements uls = tablesDescription.select("ul");


            if (ps.size() > 0) {
                for (Element p : ps) {
                    list.add(addParagraph(p));
                }
            }
            if (uls.size() > 0) {
                for (Element ul : uls) {
                    list.add(addList(ul));
                }
            }
//            }

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
