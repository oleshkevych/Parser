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
    private String startLink = "https://www.technojobs.co.uk/browse/jobs/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
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
            String loadLink = link;
            do {
                try {
                    loadLink = loadLink.replaceFirst(("page=" + (count - 1)), ("page=" + (count)));

                    datePublished = null;
                    doc = Jsoup.connect(loadLink)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    count++;
                    Elements tables = doc.select(".jobs-list .jobbox");
                    datePublished = runParse(tables, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            while (count < 4 && dateClass.dateChecker(datePublished) && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 20);
        }


    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = counter; i < tables2.size(); i += 1) {
            String stringDate = tables2.get(i).select("time").attr("datetime");
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(tables2.get(i).select("[itemprop='jobLocation']").first(), tables2.get(i).select(".job-ti").first(),
                        tables2.get(i), datePublished, tables2.get(i).select(".job-ti a").last());
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
//            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
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
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();


            Elements tablesDescription = document.select(".job-listing-body");
            Element tablesHead = document.select(".job-listing-title").first();
            Element tablesCompany = document.select(".job-listing-table td").first();
            jobsInform.setCompanyName(tablesCompany.text());
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(tablesHead));


//            for (int i = 0; i < tablesDescription.size(); i++) {
//
//                if (tablesDescription.get(i).tagName().equals("div")) {
//                    for (Element e : tablesDescription.get(i).children()) {
//                        if (e.tagName().equals("p")) {
//                            list.add(addParagraph(e));
//                        } else if (e.tagName().equals("ul")) {
//                            list.add(addList(e));
//                        } else if (e.tagName().contains("h")) {
//                            list.add(addHead(e));
//                        } else if (e.tagName().contains("di")) {
//                            for (Element e1 : e.children()) {
//                                if (e1.tagName().equals("p")) {
//                                    list.add(addParagraph(e1));
//                                } else if (e1.tagName().equals("ul")) {
//                                    list.add(addList(e1));
//                                } else if (e1.tagName().contains("h")) {
//                                    list.add(addHead(e1));
//                                } else if (e1.tagName().equals("div")) {
//                                    ListImpl list1 = new ListImpl();
//                                    list1.setTextFieldImpl(e1.text());
//                                    list.add(list1);
//                                }
//                            }
//                            ListImpl list1 = new ListImpl();
//                            list1.setTextFieldImpl(e.ownText());
//                            list.add(list1);
//                        }
//                    }
//                }
//                if (tablesDescription.get(i).tagName().equals("p")) {
//                    list.add(addParagraph(tablesDescription.get(i)));
//                } else if (tablesDescription.get(i).tagName().equals("ul")) {
//                    list.add(addList(tablesDescription.get(i)));
//                } else if (tablesDescription.get(i).tagName().contains("h")) {
//                    list.add(addHead(tablesDescription.get(i)));
//                } else if (tablesDescription.get(i).tagName().equals("div")) {
//                    ListImpl list1 = new ListImpl();
//                    list1.setTextFieldImpl(tablesDescription.get(i).ownText());
//                    list.add(list1);
//                }
//            }
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
