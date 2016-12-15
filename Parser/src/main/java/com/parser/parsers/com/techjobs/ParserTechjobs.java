package com.parser.parsers.com.techjobs;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.jobs.landing.ParserLandingJobs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserTechjobs implements ParserMain {
    private String startLink = "http://www.techjobs.com/SearchResults.aspx?query=djAuMXxSVjpRdWlja2xpc3Rpbmd8U086UmVsZXZhbnN8UE46MXxQUzo1MHx2MC4x&params=c2VhcmNoc3RhcnRkYXRlOjF8cXVlcnlmaWx0ZXI6fHdvcmthcmVhOjB8d29ya2FyZWFfbW9yZTowfEpvYmxvY2F0aW9uMTowfEpvYmxvY2F0aW9uMV9tb3JlOjB8Sm9ibG9jYXRpb246MHxKb2Jsb2NhdGlvbl9tb3JlOjB8Sm9ibG9jYXRpb24zOjB8Sm9ibG9jYXRpb24zX21vcmU6MHxKb2Jsb2NhdGlvbjQ6MHxKb2Jsb2NhdGlvbjRfbW9yZTowfEpvYmxvY2F0aW9uNTowfEpvYmxvY2F0aW9uNV9tb3JlOjB8Sm9idHlwZTowfEpvYnR5cGVfbW9yZTow";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;

    private WebDriver ghostDriver;
    public ParserTechjobs() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private Document renderPage(String url) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:\\Users\\rolique_pc\\Desktop\\ParserApp\\Parser\\Libs\\phantomjs.exe");
        caps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.60 Safari/537.17");
        caps.setCapability("phantomjs.page.settings.host", "www.sportslogos.net");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{
                "--web-security=false",
                "--ssl-protocol=any",
                "--ignore-ssl-errors=true"
        });
        ghostDriver = new PhantomJSDriver(caps);
        ghostDriver.get(url);
        return Jsoup.parse(ghostDriver.getPageSource());
    }

    private void parser() {
        try {
//            doc = Jsoup.connect(startLink)
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(5000)
//                    .get();
            doc = renderPage(startLink);
//            System.out.println("text : " + doc);

            Elements tables2 = doc.select(".borderContainer");
//            System.out.println("text : " + tables2);
            runParse(tables2, 0);

//            Date datePublished = null;
//            int count = 2;
////            do {
//                try {
//
//
//                    // need http protocol
//                    doc = Jsoup.connect(startLink + "&pg=" + count)
//                            .validateTLSCertificates(false).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();
//
//                    Elements tables1 = doc.select(".listResults .-item");
////            System.out.println("text : " + tables2);
//                    datePublished = runParse(tables1, 0);
//                    count++;
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }while(dateClass.dateChecker(datePublished));

        } catch (Exception e) {
            e.printStackTrace();
            ghostDriver.quit();
        }
        finally {
            ghostDriver.quit();
        }
    }

    private void runParse(Elements tables2, int counter) {

        for (int i = counter; i < tables2.size(); i += 1) {
//            System.out.println("text date : " + tables2.get(i));

//

            objectGenerator(tables2.get(i).select(".Location").first(), tables2.get(i).select(".HeadingContainer").first(),
                    tables2.get(i).select(".Company").first(), tables2.get(i).select("a").first());
        }

    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
        if (jobsInforms.size() < 50) {
            JobsInform jobsInform = new JobsInform();
//            System.out.println("text place : " + place.text());
//            System.out.println("text headPost : " + headPost.text());
//            System.out.println("text company : " + company.text());
//            System.out.println("text link data : " + linkDescription);
//            System.out.println("text link : " + linkDescription.attr("href"));
            jobsInform.setPublishedDate(null);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
//            jobsInform.setPublicationLink("http://www.techjobs.com/SearchResults.aspx?" + linkDescription.attr("href"));
            jobsInform = getDescription("http://www.techjobs.com/SearchResults.aspx?" + linkDescription.attr("href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public  JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {
//        System.out.println("text link1 : " + linkToDescription);

        try {
            ghostDriver.get(linkToDescription);
            Document document = Jsoup.parse(ghostDriver.getPageSource());
/*            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();*/

            jobsInform.setPublicationLink(ghostDriver.getCurrentUrl());
            Elements tablesDescription = document.select("[itemprop='description']").first().children();
            Elements tablesHead = document.select("h1");
            List<ListImpl> list = new ArrayList<ListImpl>();

            list.add(addHead(tablesHead.first()));


            for (Element element : tablesDescription) {
                if (element.tagName().equals("p")) {
                    list.add(addParagraph(element));
                } else if (element.tagName().equals("ul")) {
                    list.add(addList(element));
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
