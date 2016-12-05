package com.parser.io.wfh;

import com.parser.DateGenerator;
import com.parser.JobsInform;
import com.parser.ListImpl;
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
 * Created by rolique_pc on 12/5/2016.
 */
public class ParserWFH {

    private String startLink = "https://www.wfh.io/latest-remote-jobs";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserWFH(){
        dateClass = new DateGenerator();
        parser();
    }

    public List<JobsInform> getJobsInforms() {
        return jobsInforms;
    }

    private void parser() {
        try {


            // need http protocol
            doc = Jsoup.connect(startLink).get();


            // get page title
            String title = doc.title();
            System.out.println("title : " + title);

//            Elements tables1 = doc.select("tbody tr");
//            for (Element table : tables1) {
//
//                System.out.println("text tr : " + table.text());
//
//            }
            Elements tables2 = doc.select("tbody tr td");

            runParse(tables2, 0);


        } catch (IOException e) {
            e.printStackTrace();
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
                    runParse(tables2, i+1);
                    break;
                }
            }
            objectGenerator(tables2.get(i), tables2.get(i+1), tables2.get(i+2), datePublished);
        }
    }

    private void objectGenerator(Element table1, Element table2, Element table3, Date datePublished){
        if(dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
//            System.out.println("text date : " + table1.text());
//            System.out.println("text speciality : " + table2.text());
//            System.out.println("text place : " + table3.text());
//            System.out.println("text link : " + table2.getAllElements().get(2).attr("abs:href"));
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(table2.text()+"     PLACE: "+table3.text());
            jobsInform.setPublicationLink(table2.getAllElements().get(2).attr("abs:href"));

            getDescription(table2.getAllElements().get(2).attr("abs:href"), jobsInform);
        }
    }

    private void getDescription(String linkToDescription, JobsInform jobsInform){
        try {
            Document document = Jsoup.connect(linkToDescription).get();
            Elements tables = document.select(".col-md-9 dd");
            Elements description = tables.get(2).children();

            List<ListImpl> list = new ArrayList<ListImpl>();
//            list1.setListHeader(description.get(0).select("strong").text());
//            int count = 0;
//            if (list1.getListHeader()!=null){
//                count = 1;
//            }
            for (Element aDescription: description) {



                if (aDescription.getElementsByTag("li").size() > 0) {
                    list.add(addList(aDescription));
                } else if (aDescription.getElementsByTag("strong").size() > 0) {
                    list.add(null);
                    list.add(addHead(aDescription));
                } else {
                    list.add(addParagraph(aDescription));
                }
            }

            list.add(null);
            jobsInform.setOrder(list);
            jobsInforms.add(jobsInform);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private ListImpl addHead(Element element){
        ListImpl list = new ListImpl();
        list.setListHeader(element.text());
        return list;
    }
    private ListImpl addParagraph(Element element){
        ListImpl list = new ListImpl();
        list.setTextFieldImpl(element.text());
        return list;
    }
    private ListImpl addList(Element element){
        ListImpl list = new ListImpl();
        List<String> strings = new ArrayList<String>();
        for(Element e : element.getAllElements()) {
            strings.add(e.text());
        }
        list.setListItem(strings);
        return list;
    }

}
