package com.parser.parsers.de.uberjobs;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserUberjobs implements ParserMain {
    private String startLink = "https://uberjobs.de";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private WebDriver ghostDriver;

    public ParserUberjobs() {
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
            doc = Jsoup.connect(startLink)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();
            doc = renderPage(startLink);
            Elements tables2 = doc.select(".clickable");
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

        }catch (Exception e){
            e.printStackTrace();
            ghostDriver.quit();
        }
        finally {
            ghostDriver.quit();
        }
    }

    private void runParse(Elements tables2, int counter) {
        System.out.println("Error : " +" " + tables2.size());

        for (int i = counter; i < tables2.size(); i += 1) {
            objectGenerator(tables2.get(i).select(".ort").first(), tables2.get(i).select(".job-title").first(),
                    tables2.get(i).select(".anbieter").first(), tables2.get(i).select("a").first());
        }

    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
        if (jobsInforms.size() < 50) {
//            System.out.println("Error : " +" " + linkDescription);
//            System.out.println("Error : " +" " + "https://uberjobs.de/" + linkDescription.attr("href"));
//            System.out.println("Error : " +" " + linkDescription.attr("href"));

            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(null);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink("https://uberjobs.de/" + linkDescription.attr("href"));
            jobsInform = getDescription("https://uberjobs.de/" + linkDescription.attr("href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        try {
            ghostDriver.get(linkToDescription);
            Document document = Jsoup.parse(ghostDriver.getPageSource());

            Elements tablesDescription = document.select(".job-info").first().children();
            Elements tablesHead = document.select(".job-title");
            List<ListImpl> list = new ArrayList<ListImpl>();

            list.add(addHead(tablesHead.first()));


            for (Element element : tablesDescription) {
                if (element.tagName().equals("p")) {
                    list.add(addParagraph(element));
                } else if (element.tagName().equals("ul")) {
                    list.add(addList(element));
                }
            }

            if (list.size() < 2) {
                list.add(addParagraph(tablesDescription.first()));
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
        ListImpl list = new ListImpl();
        list.setTextFieldImpl(element.text());
        return list;
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
