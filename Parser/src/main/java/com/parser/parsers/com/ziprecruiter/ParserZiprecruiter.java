package com.parser.parsers.com.ziprecruiter;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/19/2016.
 */
public class ParserZiprecruiter implements ParserMain {


    private String startLink = "https://www.ziprecruiter.com/candidate/search?search=Drupal&page=1&location=&days=5";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserZiprecruiter() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=Drupal&page=1&location=&days=5");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=Angular&page=1&location=&days=5");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=React&page=1&location=&days=5");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=Meteor&page=1&location=&days=5");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=Node&page=1&location=&days=5");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=Frontend&page=1&location=&days=5");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=JavaScript&page=1&location=&days=5");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=iOS&page=1&location=&days=5");
        startLinksList.add("https://www.ziprecruiter.com/candidate/search?search=mobile&page=1&location=&days=5");

        int c = 0;
        for (String link : startLinksList) {
            try {
                doc = Jsoup.connect(link)
                        .validateTLSCertificates(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();

                Elements tables2 = doc.select("#job_list article");
                runParse(tables2, 0);

            } catch (IOException e) {
                e.printStackTrace();
                c++;
            }
        }
        if (c == startLinksList.size()) {
            jobsInforms = null;
        }

    }

    private void runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
//        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        for (int i = counter; i < tables2.size(); i += 1) {
//            try {

            objectGenerator(tables2.get(i).select("[itemprop='addressLocality']").first(), tables2.get(i).select("[itemprop='title']").first(),
                    tables2.get(i).select("[itemprop='name']").first(), tables2.get(i).select("[itemprop='url']").first());
//            } catch (ParseException e) {
//                System.out.println(e.getMessage());
//            }
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {

        JobsInform jobsInform = new JobsInform();
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.ownText());
        jobsInform.setPublicationLink(linkDescription.attr("href"));
        jobsInform = getDescription(linkDescription.attr("href"), jobsInform);
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }

    private static JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();
            Elements tablesDate = document.select(".posted_date");

            if (tablesDate.size() > 0) {
                String stringDate = tablesDate.first().text();
                Date datePublished = null;
                if (stringDate.contains("minut") || stringDate.contains("hour")) {
                    datePublished = new Date();
                } else if (stringDate.contains("yesterday")) {
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
                jobsInform.setPublishedDate(datePublished);
            }
            Elements tablesHead = document.select("h1");

            Elements tablesDescription = document.select(".description");
            if (tablesDescription.size() < 1) {
                tablesDescription = document.select("[itemprop='description']");
            } else if (tablesDescription.size() < 1) {
                tablesDescription = document.select(".companyDescriptionSection");
            } else if (tablesDescription.size() < 1) {
                tablesDescription = document.select(".featured-job-description .ng-scope div");
//            } else if (tablesDescription.size() < 1) {
//                tablesDescription = document.select(".custom-copy");
//            } else if (tablesDescription.size() < 1) {
//                tablesDescription = document.select(".jvdescriptionbody");
            }
            List<ListImpl> list = new ArrayList<ListImpl>();

            if (tablesHead.size() > 0) {
                list.add(addHead(tablesHead.first()));
            }
//            for (int i = 0; i<tablesDescription.size(); i++) {
//
//
//                Elements ps = tablesDescription.get(i).select("p");
//                Elements uls = tablesDescription.get(i).select("ul");
//
//
//
//                if (ps.size() > 0) {
//                    for(Element p: ps) {
//                        list.add(addParagraph(p));
//                    }
//                }
//                if (uls.size() > 0) {
//                    for(Element ul: uls) {
//                        list.add(addList(ul));
//                    }
//                }
//            }
            if (tablesDescription.size() > 1)
                list.addAll(new PrintDescription().generateListImpl(tablesDescription));

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