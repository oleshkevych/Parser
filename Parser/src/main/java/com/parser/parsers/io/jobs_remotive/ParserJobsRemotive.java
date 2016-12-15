package com.parser.parsers.io.jobs_remotive;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.jobs.landing.ParserLandingJobs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserJobsRemotive implements ParserMain {
    private String startLink = "http://jobs.remotive.io/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;
    private WebDriver ghostDriver;

    public ParserJobsRemotive(){
    }

    public List<JobsInform> startParse(){
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private static Document renderPage(String url) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:\\Users\\rolique_pc\\Desktop\\ParserApp\\Parser\\Libs\\phantomjs.exe");
        caps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.60 Safari/537.17");
        caps.setCapability("phantomjs.page.settings.host", "www.sportslogos.net");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] {
                "--web-security=false",
                "--ssl-protocol=any",
                "--ignore-ssl-errors=true"
        });
        WebDriver ghostDriver = new PhantomJSDriver(caps);
         try {
//             ghostDriver.manage().timeouts().setScriptTimeout(2600l, TimeUnit.SECONDS);
             ghostDriver.manage().timeouts().pageLoadTimeout(10600l, TimeUnit.SECONDS);
//             ghostDriver.manage().timeouts().implicitlyWait(2600l, TimeUnit.SECONDS);
            ghostDriver.get(url);
            WebDriverWait wait = new WebDriverWait(ghostDriver, 25);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("filters")));

            return Jsoup.parse(ghostDriver.getPageSource());
         } finally {
            ghostDriver.quit();
        }
    }

    private void parser() {
//        try {
//            String urlS = ("http://api-public.addthis.com/url/shares.json?url=http%3A%2F%2Fjobs.remotive.io%2F&callback=_ate.cbs.rcb_5utu0");
//            URL url = new URL(urlS);
//            BufferedReader r = new BufferedReader(new InputStreamReader(
//                    ((HttpURLConnection) url.openConnection()).getInputStream()));
//            JsonParser jp = new JsonParser();

//            JsonElement jsonElement = jp.parse(r);
//
//            JsonObject jsonObject = jsonElement.getAsJsonObject();


//            System.out.println("text date : ParserJobspresso" + r.readLine());
////             need http protocol
//            r.close();
//            doc = Jsoup.connect("http://api.mixpanel.com/track/?data=eyJldmVudCI6ICJtcF9wYWdlX3ZpZXciLCJwcm9wZXJ0aWVzIjogeyIkb3MiOiAiV2luZG93cyIsIiRicm93c2VyIjogIkNocm9tZSIsIiRjdXJyZW50X3VybCI6ICJodHRwOi8vam9icy5yZW1vdGl2ZS5pby8iLCIkYnJvd3Nlcl92ZXJzaW9uIjogNTUsIiRzY3JlZW5faGVpZ2h0IjogMTA4MCwiJHNjcmVlbl93aWR0aCI6IDE5MjAsIm1wX2xpYiI6ICJ3ZWIiLCIkbGliX3ZlcnNpb24iOiAiMi45LjE3IiwiZGlzdGluY3RfaWQiOiAiMTU4ZDQ1NjJhYTExNWItMDE3MzIwOGE5NDA5MzQtNWM0ZjIzMWMtMWZhNDAwLTE1OGQ0NTYyYWEzMTVmIiwiJGluaXRpYWxfcmVmZXJyZXIiOiAiJGRpcmVjdCIsIiRpbml0aWFsX3JlZmVycmluZ19kb21haW4iOiAiJGRpcmVjdCIsIm1wX3BhZ2UiOiAiaHR0cDovL2pvYnMucmVtb3RpdmUuaW8vIiwibXBfYnJvd3NlciI6ICJDaHJvbWUiLCJtcF9wbGF0Zm9ybSI6ICJXaW5kb3dzIiwidG9rZW4iOiAiNmVjZWQyODM1YTBkYTUxMTkwZThjMmZkZDAzZTMxOGUifX0%3D&ip=1&_=1481798770674")
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(5000)
//                    .get();
//            System.out.println("text place1 : " + doc);

        doc = renderPage(startLink);
        System.out.println("text place2 : " + doc);

//        Elements tables2 = doc.select(".positions li");
//        runParse(tables2, 0);


//        }catch (IOException e){
//
//            e.printStackTrace();
//        }
    }

    private void runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        for (int i = counter; i<tables2.size(); i+=1) {
                objectGenerator(tables2.get(i).select("[data-reactid='.0.2.1:$category_marketing.1.$job_1.1.3']").first(), tables2.get(i),
                        tables2.get(i), tables2.get(i));
            }

    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription){
            if(jobsInforms.size() < 40) {
            JobsInform jobsInform = new JobsInform();
                System.out.println("text place : " + place.text());
                System.out.println("text headPost : " + headPost.attr("data-position"));
                System.out.println("text company : " + company.attr("data-company"));
            jobsInform.setHeadPublication(headPost.attr("data-position"));
            jobsInform.setCompanyName(company.attr("data-company"));
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(startLink+linkDescription.attr("data-url"));
//            jobsInform = getDescription(startLink+linkDescription.attr("href"), jobsInform);
            if(!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform){
//        System.out.println("text link1 : " + linkToDescription);

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();


            Elements tablesDescription = document.select(".mainContainer").get(1).children();
//            Elements tablesHead = document.select(".detail-sectionTitle");
            List<ListImpl> list = new ArrayList<ListImpl>();
//            System.out.println("text link1 : " + tablesDescription);

            for (int i = 0; i<tablesDescription.size(); i++) {

//                System.out.println("text link1 : " + tablesDescription.size());
                if(tablesDescription.get(i).tagName().contains("h")){
                    if(list.size()>0) {
                        list.add(null);
                    }
                    list.add(addHead(tablesDescription.get(i)));
                } else if(tablesDescription.get(i).select("p").size() > 0 || tablesDescription.get(i).select("ul").size() > 0){
//                   System.out.println("text p/ul : " + tablesDescription.get(i));
                    for(Element element: tablesDescription.get(i).select(".col-md-12").first().children()){
                        if(element.tagName().equals("p")){
                            list.add(addParagraph(element));
//                           System.out.println("text p : " + element);
                        }else if(element.tagName().equals("ul")){
                            list.add(addList(element));
                        }
                    }
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
    private static ListImpl addHead(Element element){
        ListImpl list = new ListImpl();
        list.setListHeader(element.text());
        return list;
    }
    private static ListImpl addParagraph(Element element){
        if(element.select("strong").size()>0){
            return addHead(element.select("strong").first());
        }else {


            ListImpl list = new ListImpl();
            list.setTextFieldImpl(element.text());
            return list;
        }
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
