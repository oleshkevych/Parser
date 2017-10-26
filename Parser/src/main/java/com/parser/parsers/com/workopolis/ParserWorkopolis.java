package com.parser.parsers.com.workopolis;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
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
 * Created by rolique_pc on 12/13/2016.
 */
public class ParserWorkopolis implements ParserMain {

    //    private String startLink = "http://www.workopolis.com/jobsearch/find-jobs#lg=en&st=POSTDATE%20%20%20%20&lr=50&pn=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserWorkopolis() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cdrupal%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cangular%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Creact%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cmeteor%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cnode%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cfrontend%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cjavascript%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cios%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cmobile%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");

        int c = 0;
        for (String link : startLinksList) {
//            Date datePublished = null;
            int count = 1;
            try {
//                do {
//                    datePublished = null;

                    doc = Jsoup.connect(link + count)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();
                    Elements tables1 = doc.select(".search-result-item-link");
//                    datePublished = runParse(tables1, 0);
                    runParse(tables1, 0);
                    count++;
//                }
//                while (count < 2 && dateClass.dateChecker(datePublished) && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 20);
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
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date datePublished = null;
        for (int i = counter; i < tables2.size(); i += 1) {
//            datePublished = null;
//            String stringDate = tables2.get(i).select("time").attr("datetime");
//            if (stringDate.isEmpty()) continue;
//            stringDate = stringDate.substring(0, stringDate.indexOf(" "));
//            try {
//                datePublished = formatter.parse(stringDate);
                objectGenerator(tables2.get(i).select(".location").first(),
                        tables2.get(i).select(".job-title").first(),
                        tables2.get(i).select(".company").first(),
//                        datePublished,
                        tables2.get(i).select("a.search-result-item-link").first());
//            } catch (ParseException e) {
//                System.out.println(e.getMessage());
//            }

        }
//        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
//        if (dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
//            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink("http://www.workopolis.com" + linkDescription.attr("href"));
            jobsInform = getDescription("http://www.workopolis.com" + linkDescription.attr("href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
//        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements tablesDescription = document.select(".job-view-content-wrapper").first().children();
            Elements tablesHead = document.select(".job-view-header-title");
            List<ListImpl> list = new ArrayList<ListImpl>();

            list.add(addHead(tablesHead.first()));
            for (int i = 0; i < tablesDescription.size(); i++) {


                if (tablesDescription.get(i).select(".iCIMS_Expandable_Text").size() > 0) {
                    for (Element element : tablesDescription.get(i).select(".iCIMS_Expandable_Text").first().children()) {
                        if (element.tagName().equals("p")) {
                            list.add(addParagraph(element));
                        } else if (element.tagName().equals("ul")) {
                            list.add(addList(element));
                        }
                    }
                } else if (tablesDescription.get(i).tagName().equals("p")) {
                    list.add(addParagraph(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().equals("ul")) {
                    list.add(addList(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).select("div").size() > 0) {
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
