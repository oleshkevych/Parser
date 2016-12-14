package com.parser.parsers.de.monster;

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
public class ParserMonsterDe implements ParserMain {

    private String startLink = "http://www.monster.de/jobs/suche/?q=Festanstellung+Freie-Mitarbeit-Dienstvertrag+Vollzeit+Teilzeit+Befristet_88888?q=IT&intcid=HP_Nav";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserMonsterDe() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
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

            Elements tables2 = doc.select(".js_result_row");
            runParse(tables2, 0);

            Date datePublished = null;
            int count = 2;
            do {
                try {

                    doc = Jsoup.connect(startLink + "&page=" + count)
                            .validateTLSCertificates(false).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();

                    Elements tables1 = doc.select(".js_result_row");
                    datePublished = runParse(tables1, 0);
                    count++;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } while (dateClass.dateChecker(datePublished)&& jobsInforms.size() < 100);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = counter; i < tables2.size(); i += 1) {
            String stringDate = tables2.get(i).select("time").first().attr("datetime").substring(0, 10);
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(tables2.get(i).select(".location").first(), tables2.get(i).select(".jobTitle").first(),
                        tables2.get(i).select(".company").first(), datePublished, tables2.get(i).select(".jobTitle a").first());
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
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
//            jobsInform = getDescription(linkDescription.attr("abs:href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

//    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {
//
//        try {
//            Document document = Jsoup.connect(linkToDescription)
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(5000)
//                    .get();
//
//
//            Elements tablesDescription = document.select("#job").first().children();
////            Element tablesHead = tablesDescription.first();
//            List<ListImpl> list = new ArrayList<ListImpl>();
////            System.out.println("text link1 : " + tablesDescription);
//
//            for (int i = 0; i < tablesDescription.size(); i++) {
//                System.out.println("text place all: " + tablesDescription.get(i));
//                System.out.println("text place tag: " + tablesDescription.get(i).tagName());
//
//
//                if (tablesDescription.get(i).tagName().contains("h") || (tablesDescription.get(i).tagName().equals("strong") && tablesDescription.get(i + 2).tagName().equals("ul"))) {
////                    list.add(null);
//                    list.add(addHead(tablesDescription.get(i)));
//                } else if (tablesDescription.get(i).tagName().equals("p")) {
//                    list.add(addParagraph(tablesDescription.get(i)));
//                } else if (tablesDescription.get(i).tagName().equals("ul")) {
//                    list.add(addList(tablesDescription.get(i)));
//                }
//
////                Elements ps = tablesDescription.select("p");
////                Elements uls = tablesDescription.select("ul");
//
//
////                if (ps.size() > 0) {
////                    for (Element p : ps) {
////                        list.add(addParagraph(p));
////                    }
////                }
////                if (uls.size() > 0) {
////                    for(Element ul: uls) {
////                        list.add(addList(ul));
////                    }
////                }
//            }
//            ListImpl list1 = new ListImpl();
//            list1.setTextFieldImpl(document.select("#job").first().ownText());
//            list.add(list1);
//            list.add(null);
//            jobsInform.setOrder(list);
//
//
//            return jobsInform;
//        } catch (Exception e) {
//            System.out.println("Error : " + e.getMessage() + " " + jobsInform.getPublicationLink());
//            e.printStackTrace();
//            return jobsInform;
//        }
//
//    }
//
//    private static ListImpl addHead(Element element) {
//        ListImpl list = new ListImpl();
//        list.setListHeader(element.text());
//        return list;
//    }
//
//    private static ListImpl addParagraph(Element element) {
//        ListImpl list = new ListImpl();
//        list.setTextFieldImpl(element.text());
//        return list;
//    }
//
//    private static ListImpl addList(Element element) {
//        ListImpl list = new ListImpl();
//        List<String> strings = new ArrayList<String>();
//        for (Element e : element.getAllElements()) {
//            strings.add(e.text());
//        }
//        list.setListItem(strings);
//        return list;
//    }
}
