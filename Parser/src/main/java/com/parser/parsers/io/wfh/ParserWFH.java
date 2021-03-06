package com.parser.parsers.io.wfh;

import com.parser.dbmanager.DbHelper;
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
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/5/2016.
 */
public class ParserWFH implements ParserMain{

    private String startLink = "https://www.wfh.io/latest-remote-jobs";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;


    public ParserWFH(){

    }

    public List<JobsInform> startParse(){
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {

            doc = Jsoup.connect(startLink)
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
        for (int i = counter; i<tables2.size(); i+=3) {

            Date datePublished = null;
            try {
                datePublished = formatter.parse(tables2.get(i).text());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
                if(tables2.get(i).text().equals("Click for more Remote Software Development jobs")){
                    Document doc1 = null;
                    try {
                        doc1 = Jsoup.connect(tables2.get(i).select("a").attr("abs:href")).get();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    runParse(tables2, i+1);
                    if(doc1 != null) {
                        runParse(doc1.select("tbody tr td"), 0);
                    }
                    break;
                }
            }
            objectGenerator(tables2.get(i), tables2.get(i+1), tables2.get(i+2), datePublished);
        }
    }

    private void objectGenerator(Element table1, Element table2, Element table3, Date datePublished){
        if(dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(table2.text());
            jobsInform.setPlace(table3.text());
            jobsInform.setPublicationLink(table2.getAllElements().get(2).attr("abs:href"));

            jobsInform = getDescription(table2.getAllElements().get(2).attr("abs:href"), jobsInform);
            if(!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform){
        try {
            Document document = Jsoup.connect(linkToDescription)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();
            Elements tables = document.select(".col-md-9 dd");
            Elements description = tables.get(2).children();

            Element tableHead = document.select("h1").first();
            String company = document.select(".panel-body dd").get(1).text();
            if(company.length()>0){
                jobsInform.setCompanyName(company);
            }

            jobsInform.setCompanyName(document.select(".page-header small").text());
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(tableHead));
//            for (Element aDescription: description) {
//
//                if (aDescription.getElementsByTag("li").size() > 0) {
//                    list.add(addList(aDescription));
//                } else if (aDescription.getElementsByTag("strong").size() > 0) {
//                    list.add(null);
//                } else {
//                    list.add(addParagraph(aDescription));
//                }
//            }

            list.addAll(new PrintDescription().generateListImpl(description));
            list.add(null);
            jobsInform.setOrder(list);
            return jobsInform;

        } catch (IOException e) {
            e.printStackTrace();
            return jobsInform;
        }
    }
    private static ListImpl addHead(Element element){
        ListImpl list = new ListImpl();
        list.setListHeader(element.text());
        return list;
    }
    private static ListImpl addParagraph(Element element){
        ListImpl list = new ListImpl();
        list.setTextFieldImpl(element.text());
        return list;
    }
    private static ListImpl addList(Element element){
        ListImpl list = new ListImpl();
        List<String> strings = new ArrayList<String>();
        for(Element e : element.getAllElements()) {
            strings.add(e.text());
        }
        list.setListItem(strings);
        return list;
    }

}
