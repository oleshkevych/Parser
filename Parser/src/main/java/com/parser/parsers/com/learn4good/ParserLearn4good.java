package com.parser.parsers.com.learn4good;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PrintDescription;
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

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javafx.css.StyleOrigin.USER_AGENT;

/**
 * Created by rolique_pc on 12/16/2016.
 */
public class ParserLearn4good implements ParserMain {
    private String startLink = "https://www.learn4good.com/schools/frontend/index.php?controller=listing_search&action=display_results&page_number=1";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserLearn4good() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }
    private static Document renderPage(String url) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:\\Users\\rolique_pc\\Desktop\\ParserApp\\Parser\\Libs\\phantomjs.exe");

        WebDriver ghostDriver = new PhantomJSDriver(caps);
        try {
            ghostDriver.get(url);
//            WebDriverWait wait = new WebDriverWait(ghostDriver, 15);
//
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("job_list")));
            return Jsoup.parse(ghostDriver.getPageSource());
        } finally {
            ghostDriver.quit();
        }
    }
    private void parser() {


        doc = renderPage(startLink);
        System.out.println("text date : " + doc);



//        Date datePublished = null;
//        int count = 1;
//        do {
//            try {
//
//                datePublished = null;
//                doc = Jsoup.connect(startLink + count)
//                        .validateTLSCertificates(false)
//                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                        .timeout(5000)
//                        .get();
////                System.out.println("text date : " + doc);
//
//                Elements tables2 = doc.select("#main_job_list tr");
//                datePublished = runParse(tables2, 0);
//                count++;
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } while (dateClass.dateChecker(datePublished) /*&& jobsInforms.size() < 40*/);


    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = counter; i < tables2.size(); i += 1) {
                String stringDate = tables2.get(i).select(".posting_date").first().text();
            System.out.println("text date : " + stringDate);

            try {
                    datePublished = formatter.parse(stringDate);
                    objectGenerator(tables2.get(i).select(".loc_title").first(), tables2.get(i).select(".job_title").first(),
                            tables2.get(i).select("[itemprop='hiringOrganization']").first(), datePublished, tables2.get(i).select(".job_title").first());
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished) /*&& jobsInforms.size() < 40*/) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(linkDescription.attr("href"));
            jobsInform = getDescription(linkDescription.attr("href"), jobsInform);
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


            Elements tablesDescription = document.select("#content").first().select("div")
                    .select("*:not([id='search_container'])").select("*:not([id='under_content_afc'])");
            Element tablesHead = document.select("#content h1").first();
//            Element tablesPlace = document.select(".active-fields .displayFieldBlock").first().children().last();
//            jobsInform.setPlace(tablesPlace.text());
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(tablesHead));


//            for (int i = 0; i < tablesDescription.size(); i++) {
//
//                if(!tablesDescription.get(i).hasClass("top_section")) {
//                    if (tablesDescription.get(i).tagName().equals("div")) {
//                        for (Element e : tablesDescription.get(i).children()) {
//                            if (e.tagName().equals("p")) {
//                                list.add(addParagraph(e));
//                            } else if (e.tagName().equals("ul")) {
//                                list.add(addList(e));
//                            } else if (e.tagName().contains("h")) {
//                                list.add(addHead(e));
//                            } else if (e.tagName().contains("di")) {
//                                for (Element e1 : e.children()) {
//                                    if (e1.tagName().equals("p")) {
//                                        list.add(addParagraph(e1));
//                                    } else if (e1.tagName().equals("ul")) {
//                                        list.add(addList(e1));
//                                    } else if (e1.tagName().contains("h")) {
//                                        list.add(addHead(e1));
//                                    } else if (e1.tagName().equals("div")) {
//                                        ListImpl list1 = new ListImpl();
//                                        list1.setTextFieldImpl(e1.text());
//                                        list.add(list1);
//                                    }
//                                }
//                                ListImpl list1 = new ListImpl();
//                                list1.setTextFieldImpl(e.ownText());
//                                list.add(list1);
//                            }
//                        }
//                    }
//                    if (tablesDescription.get(i).tagName().equals("p")) {
//                        list.add(addParagraph(tablesDescription.get(i)));
//                    } else if (tablesDescription.get(i).tagName().equals("ul")) {
//                        list.add(addList(tablesDescription.get(i)));
//                    } else if (tablesDescription.get(i).tagName().contains("h")) {
//                        list.add(addHead(tablesDescription.get(i)));
//                    } else if (tablesDescription.get(i).tagName().equals("div")) {
//                        ListImpl list1 = new ListImpl();
//                        list1.setTextFieldImpl(tablesDescription.get(i).ownText());
//                        list.add(list1);
//                    }
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
