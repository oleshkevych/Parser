package com.parser.parsers.jobs.landing;


import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/6/2016.
 */
public class ParserLandingJobs {


    private String startLink = "https://landing.jobs/offers/?page=1&s=date&s_l=0&s_h=100&t_co=false&t_st=false";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserLandingJobs(){
        dateClass = new DateGenerator();
        parser();
        System.out.println(" FINISH" );

    }

    private static String filePath = "data/temp/";

    public static Document renderPage(String url) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:\\Users\\rolique_pc\\Desktop\\ParserApp\\Parser\\Libs\\phantomjs.exe");

        WebDriver ghostDriver = new PhantomJSDriver(caps);
        try {
            ghostDriver.get(url);
            return Jsoup.parse(ghostDriver.getPageSource());
        } finally {
            ghostDriver.quit();
        }
    }

//    public static Document renderPage(Document doc) throws IOException {
//        String tmpFileName = "$filePath${Calendar.getInstance().timeInMillis}.html";
//        File file = new File(tmpFileName);
//        FileUtils.writeStringToFile(file, doc.toString());
//        return renderPage(tmpFileName);
//    }




    public List<JobsInform> getJobsInforms() {
        return jobsInforms;
    }

    private void parser() {
//        try {


            // need http protocol
//            doc = Jsoup.connect(startLink).userAgent("Mozilla/5.0").timeout(5000).get();
            Document doc = renderPage(startLink);
            Elements tables2 = doc.select(".ld-job-offer");
//            System.out.println("text : " + doc.select("body"));
            System.out.println("text : " + tables2);
            runParse(tables2, 0);


//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private void runParse(Elements tables2, int counter) {
        System.out.println("text size : " + tables2.size());

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = counter; i<tables2.size(); i++) {
//                System.out.println("text date : " + tables2.get(i));

            Date datePublished = null;
//            try {
                int daysOld = 90 - Integer.parseInt(tables2.get(i).select(".ld-time-left").text().substring(0, 2));
                datePublished = new Date(new Date().getTime() - daysOld * 24 * 60 * 60 * 1000);
//                    System.out.println("text date : " + datePublished);
                objectGenerator(tables2.get(i).select(".ld-location").first(), tables2.get(i).select(".ld-title").first(),
                        tables2.get(i).select(".company-name").first(), datePublished, tables2.get(i).select(".ld-job-offer-link").first());
//            } catch (ParseException e) {
//                System.out.println(e.getMessage());
//            }

        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription){
        if(dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
                System.out.println("text place : " + place.text());
                System.out.println("text headPost : " + headPost.text());
                System.out.println("text company : " + company.text());
                System.out.println("text link1 : " + linkDescription.attr("abs:href"));
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
            jobsInform = getDescription(linkDescription.attr("abs:href"), jobsInform);
            if(!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform){

        try {
            Document document = Jsoup.connect(linkToDescription).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();
            System.out.println("DOCs: "+document);
//            Document document = renderPage(linkToDescription);
//  if(document.select(".navbar-brand").attr("alt").equals("Logo white")){
//                return ParserWFH.getDescription(linkToDescription, jobsInform);
//            }
//            String locate = document.select(".location").text();
//            if(locate.length()>0){
//                    System.out.println("text locate : " + locate);
//                jobsInform.setPlace(locate);

//            }
//            String company = document.select(".employer").text();
//            if(locate.length()>0){
//                    System.out.println("text employer : " + company);
//                jobsInform.setCompanyName(company);
//
//            }
            Elements tablesDescription = document.select(".ld-job-offer-section");
//            Elements tablesHeaders = document.select(".detail-sectionTitle");
            List<ListImpl> list = new ArrayList<ListImpl>();

            for (int i = 1; i<tablesDescription.size()-1; i++) {
                list.add(null);
//                list.add(addHead(tablesHeaders.get(i)));

                Elements ps = tablesDescription.get(i).select("p");
                Elements uls = tablesDescription.get(i).select("ul");
                Elements h = tablesDescription.get(i).select("h1");
                if(h!=null) {
                    list.add(addHead(h.first()));
                }

                if (ps.size() > 0) {
                    for(Element p: ps) {
                        list.add(addParagraph(p));
                    }
                }
                if (uls.size() > 0) {
                    for(Element ul: uls) {
                        list.add(addList(ul));
                    }
                }
            }

            list.add(null);
            jobsInform.setOrder(list);


            return jobsInform;
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
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
