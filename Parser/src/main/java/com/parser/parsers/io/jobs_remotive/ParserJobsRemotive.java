package com.parser.parsers.io.jobs_remotive;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import com.parser.parsers.PrintDescription;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserJobsRemotive implements ParserMain {
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserJobsRemotive() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

        doc = PhantomJSStarter.startGhost("http://jobs.remotive.io/?category=engineering");

        String json = doc.toString().substring(doc.toString().indexOf("entries") - 1);
        json = "{" + json.substring(0, json.indexOf("</script>"));

        JsonParser jp = new JsonParser();
        JsonElement jsonElement = jp.parse(json);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jArray = (JsonArray) jsonObject.get("entries");


        JsonObject jobJson;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        for (int i = 0; i < jArray.size(); i++) {
            jobJson = (jArray.get(i).getAsJsonObject());
            String title = jobJson.get("role").getAsString();
            String place = jobJson.get("region").getAsString();
            String link =  jobJson.get("url").getAsString();
            String company = jobJson.get("company").getAsString();
            JobsInform jobsInform = new JobsInform();
            jobsInform.setCompanyName(company);
            jobsInform.setPlace(place);
            jobsInform.setHeadPublication(title);
            jobsInform.setPublicationLink(link);
            List<ListImpl> list = new ArrayList<ListImpl>();
            ListImpl list1 = new ListImpl();
            list1.setListHeader(title);
            list.add(list1);
            jobsInform.setOrder(list);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }





//    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {
////        System.out.println("text link1 : " + linkToDescription);
//
//        try {
//            Document document = Jsoup.connect(linkToDescription)
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(5000)
//                    .get();
//
//
//            Elements tablesDescription = document.select("[itemprop='description']").first().children();
//            Element tablesHead = document.select("[itemprop='title']").first();
//            Element tablesDate = document.select("time").first();
//            Element tablesCompany = document.select(".postinfo span").first();
//            Element tablesPlace = document.select("[itemprop='jobLocation']").first();
//            jobsInform.setCompanyName(tablesCompany.text());
//            jobsInform.setPlace(tablesPlace.text());
//            jobsInform.setHeadPublication(tablesHead.text());
//            System.out.println("text place : " + tablesPlace.text());
//            System.out.println("text headPost : " + jobsInform.getHeadPublication());
//            System.out.println("text company : " + tablesCompany.text());
//            List<ListImpl> list = new ArrayList<ListImpl>();
//            list.add(addHead(tablesHead));
//            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//            jobsInform.setPublishedDate(formatter.parse(tablesDate.text()));
////            System.out.println("text link1 : " + tablesDescription);
//
////            for (int i = 0; i < tablesDescription.size(); i++) {
//
//////                System.out.println("text link1 : " + tablesDescription.size());
////                if (tablesDescription.get(i).tagName().contains("h")) {
////                    if (list.size() > 0) {
////                        list.add(null);
////                    }
////                    list.add(addHead(tablesDescription.get(i)));
////                } else if (tablesDescription.get(i).select("p").size() > 0 || tablesDescription.get(i).select("ul").size() > 0) {
//////                   System.out.println("text p/ul : " + tablesDescription.get(i));
////                    for (Element element : tablesDescription.get(i).select(".col-md-12").first().children()) {
////                        if (element.tagName().equals("p")) {
////                            list.add(addParagraph(element));
//////                           System.out.println("text p : " + element);
////                        } else if (element.tagName().equals("ul")) {
////                            list.add(addList(element));
////                        }
////                    }
////                }
//
////            }
//
//            list.addAll(new PrintDescription().generateListImpl(tablesDescription));
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
}
