package com.parser.parsers.com.canadajobs;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.com.indeed.ParserIndeed;
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
public class ParserCanadajobs implements ParserMain {
    private String startLink = "http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=&category=&city=&start=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserCanadajobs() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=Drupal&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=angular&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=react&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=meteor&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=node&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=frontend&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=javascript&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=ios&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=mobile&category=&city=&start=1&page=1");

        for (String link : startLinksList) {
            Date datePublished = null;
            int count = 1;
            int count1 = 1;
            String loadLink = link;
            do {
                try {

                    datePublished = null;
                    doc = Jsoup.connect(loadLink + count1)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    Elements tables2 = doc.select(".results tr");
                    datePublished = runParse(tables2, 0);
                    count += 10;
                    count1++;
                    loadLink = link.replace("start=" + (count - 10), "start=" + (count));
                    System.out.println("text date : " + loadLink);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            while (dateClass.dateChecker(datePublished) && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 20);

        }
    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        for (int i = counter; i < tables2.size(); i += 1) {
//            System.out.println("text date O: " + tables2.get(i));
            datePublished = objectGenerator(tables2.get(i).select("p").last(), tables2.get(i).select("a").first(),
                    tables2.get(i).select("p").first(), tables2.get(i).select("a").first());


        }
        return datePublished;
    }

    private Date objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
//        System.out.println("text date P: " + place);
        JobsInform jobsInform = new JobsInform();
//            jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.ownText());
        jobsInform.setPlace(place.text());
        //        System.out.println("text date P: " + place);

        if(linkDescription.attr("href").contains("http://ca.indeed.com")) {
            jobsInform.setPublicationLink(linkDescription.attr("href"));
            jobsInform = ParserIndeed.getDescription(linkDescription.attr("href"), jobsInform);
        }else{
            jobsInform.setPublicationLink("http://www.canadajobs.com/canadajobs/jobs/" + linkDescription.attr("href"));
            jobsInform = getDescription("http://www.canadajobs.com/canadajobs/jobs/" + linkDescription.attr("href"), jobsInform);
        }
        if (dateClass.dateChecker(jobsInform.getPublishedDate()) ) {
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
        return jobsInform.getPublishedDate();
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();


            Elements tablesDescription = document.select(".maincontent").first().children().select("div").get(2).children();
            Element tablesHead = document.select(".maincontent").first().children().select("div").get(1).children().last();
            Element tablesDate = document.select(".maincontent").first().children().select("div").get(2).select("p").last();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            System.out.println("Error : " + tablesDate.text());
            jobsInform.setPublishedDate(formatter.parse(tablesDate.text().substring(7)));
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(tablesHead));


            for (int i = 0; i < tablesDescription.size(); i++) {

                if (tablesDescription.get(i).tagName().equals("div")) {
                    for (Element e : tablesDescription.get(i).children()) {
                        if (e.tagName().equals("p")) {
                            list.add(addParagraph(e));
                        } else if (e.tagName().equals("ul")) {
                            list.add(addList(e));
                        } else if (e.tagName().contains("h")) {
                            list.add(addHead(e));
                        } else if (e.tagName().contains("di")) {
                            for (Element e1 : e.children()) {
                                if (e1.tagName().equals("p")) {
                                    list.add(addParagraph(e1));
                                } else if (e1.tagName().equals("ul")) {
                                    list.add(addList(e1));
                                } else if (e1.tagName().contains("h")) {
                                    list.add(addHead(e1));
                                } else if (e1.tagName().equals("div")) {
                                    ListImpl list1 = new ListImpl();
                                    list1.setTextFieldImpl(e1.text());
                                    list.add(list1);
                                }
                            }
                            ListImpl list1 = new ListImpl();
                            list1.setTextFieldImpl(e.ownText());
                            list.add(list1);
                        }
                    }
                }
                if (tablesDescription.get(i).tagName().equals("p")) {
                    list.add(addParagraph(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().equals("ul")) {
                    list.add(addList(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().contains("h")) {
                    list.add(addHead(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().equals("div")) {
                    ListImpl list1 = new ListImpl();
                    list1.setTextFieldImpl(tablesDescription.get(i).ownText());
                    list.add(list1);
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
        list.setListHeader(element.ownText());
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
