package com.parser.parsers.com.juju;


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
 * Created by rolique_pc on 12/7/2016.
 */
public class ParserJuju implements ParserMain {

    private String startLink = "http://www.juju.com/jobs?k=&l=&r=20&c=software-it";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserJuju() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("http://www.juju.com/jobs?k=Drupal&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=Angular&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=React&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=Meteor&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=Node&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=Frontend&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=JavaScript&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=iOS&l&r");
        startLinksList.add("http://www.juju.com/jobs?k=mobile&l&r");

        for (String link : startLinksList) {
            try {


                // need http protocol
                doc = Jsoup.connect(link)
                        .validateTLSCertificates(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();

                Elements tables2 = doc.select(".article .job");
                runParse(tables2, 0);

                Date datePublished = null;
                int count = 2;
                do {
                    try {

                        datePublished = null;
                        // need http protocol
                        doc = Jsoup.connect(link + "&page=" + count)
                                .validateTLSCertificates(false).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();

                        Elements tables1 = doc.select(".article .job");
                        datePublished = runParse(tables1, 0);
                        count++;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } while (dateClass.dateChecker(datePublished) &&  jobsInforms.size() < (startLinksList.indexOf(link)+1) * 20);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date1 : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        for (int i = counter; i < tables2.size(); i += 1) {
            String stringDate = tables2.get(i).select(".options .source").text();
            try {
                datePublished = formatter.parse(stringDate.substring(stringDate.indexOf("(") + 1, stringDate.indexOf(")")));

                objectGenerator(tables2.get(i).select(".company span").first(), tables2.get(i).select(".result").first(),
                        tables2.get(i).select(".company").first(), datePublished, tables2.get(i).select(".result").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }

        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.ownText());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(linkDescription.attr("href"));
//            jobsInform = getDescription(linkDescription.attr("href"), jobsInform);
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

            List<ListImpl> list = new ArrayList<ListImpl>();

            list.add(addHead(document.select(".title").first()));
            if(document.hasAttr("data-caption")) {
//            int count = 0;
                Elements tablesDescription = document.select("[data-caption='Show Full Description']").first().children();
//            if (tablesDescription.select("p").first().hasClass("simple_line")) {
//                jobsInform.setPlace(tablesDescription.select("p").first().text());
//                count++;
//            }

                for (int i = 0; i < tablesDescription.size(); i++) {

                    if (tablesDescription.get(i).tagName().equals("span")) {
                        list.add(addParagraph(tablesDescription.get(i)));
                    } else if (tablesDescription.get(i).tagName().equals("ul")) {
                        list.add(addList(tablesDescription.get(i)));
                    } else if (tablesDescription.get(i).tagName().contains("strong")) {
                        list.add(addHead(tablesDescription.get(i)));
                    } else if (tablesDescription.get(i).tagName().equals("div")) {
                        ListImpl list1 = new ListImpl();
                        list1.setTextFieldImpl(tablesDescription.get(i).ownText());
                        list.add(list1);
                    }
                }

                list.add(null);
                jobsInform.setOrder(list);
            }

            return jobsInform;
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage() + " " + jobsInform.getPublicationLink());
            e.printStackTrace();
            return jobsInform;
        }

    }

    private static ListImpl addHead(Element element) {
        ListImpl list = new ListImpl();
        try {
            list.setListHeader(element.text());
        }catch (NullPointerException e){
            System.out.println("Error : Juju");
            e.printStackTrace();
        }
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
