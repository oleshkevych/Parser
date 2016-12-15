package com.parser.parsers.com.careerbuilder;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserCareerbuilder implements ParserMain {
    private String startLink = "http://www.careerbuilder.com/jobs?posted=7";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;

    public ParserCareerbuilder() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {
            doc = Jsoup.connect(startLink)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();
            Elements tables2 = doc.select(".jobs .job-row");
            runParse(tables2, 0);

//            Date datePublished = null;
//            int count = 2;
////            do {
//                try {
//
//
//                    // need http protocol
//                    doc = Jsoup.connect(startLink + "&pg=" + count)
//                            .validateTLSCertificates(false).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();
//
//                    Elements tables1 = doc.select(".listResults .-item");
////            System.out.println("text : " + tables2);
//                    datePublished = runParse(tables1, 0);
//                    count++;
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }while(dateClass.dateChecker(datePublished));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void runParse(Elements tables2, int counter) {

        Date datePublished = null;
        for (int i = counter; i < tables2.size(); i += 1) {
            String stringDate = tables2.get(i).select(".time-posted em").text();

            if (stringDate.contains("now") || stringDate.contains("minut") || stringDate.contains("hour")) {
                datePublished = new Date();
            } else if (stringDate.contains("yesterday") || stringDate.contains("1 day")) {
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

            objectGenerator(tables2.get(i).select(".job-information .columns").get(2), tables2.get(i).select(".job-title").first(),
                    tables2.get(i).select(".job-information .columns").get(1), tables2.get(i).select("a").first(), datePublished);
        }

    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription, Date date) {
        if (new DateGenerator().dateChecker(date) && jobsInforms.size() < 50) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(null);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
            jobsInform = getDescription(linkDescription.attr("abs:href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();


            Elements tablesDescription = document.select(".description");
            Elements tablesHead = document.select("h1");
            List<ListImpl> list = new ArrayList<ListImpl>();

            list.add(addHead(tablesHead.first()));


            for (Element element : tablesDescription) {
                if (element.tagName().equals("p")) {
                    list.add(addParagraph(element));
                } else if (element.tagName().equals("ul")) {
                    list.add(addList(element));
                }
            }

            if (list.size() < 2) {
                list.add(addParagraph(tablesDescription.first()));
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
