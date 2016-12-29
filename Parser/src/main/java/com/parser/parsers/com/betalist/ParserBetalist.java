package com.parser.parsers.com.betalist;

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
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rolique_pc on 12/26/2016.
 */
public class ParserBetalist implements ParserMain {

    //    private String startLink = "http://www.builtinaustin.com/jobs#/jobs?f[]=tags%3ATTTTT%7Cim_job_categories%3A7110%3Aterm";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;
    private WebDriver ghostDriver;

    public ParserBetalist() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();

        return jobsInforms;
    }



    private void parser() {
        List<String> stringCat = new ArrayList<>();
        stringCat.add("drupal");
        stringCat.add("angular");
        stringCat.add("react");
        stringCat.add("meteor");
        stringCat.add("node");
        stringCat.add("frontend");
        stringCat.add("javascript");
        stringCat.add("ios");
        stringCat.add("mobile");

        String startLink = "https://betalist.com/jobs?q=";
        for (String category : stringCat) {
            try {

                doc = PhantomJSStarter.startGhost(startLink + category);
                Elements tables2 = doc.select(".jobCards--searchResults .ais-hits--item");
                runParse(tables2, 0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        for (int i = counter; i < tables2.size(); i += 1) {
            objectGenerator(tables2.get(i).select(".jobCard__details__location").first(), tables2.get(i).select(".jobCard__details__title").first(),
                    tables2.get(i).select(".jobCard__details__company").first(), tables2.get(i).select(".jobCard__details__title").first());
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(null);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink("http://betalist.com" + linkDescription.attr("href"));
        jobsInform = getDescription("http://betalist.com" + linkDescription.attr("href"), jobsInform);
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }

    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();


            Elements tablesDescription = document.select(".jobListing__main__text");
            Elements tablesHead = document.select(".visualHeader__title");
            List<ListImpl> list = new ArrayList<ListImpl>();

            list.add(addHead(tablesHead.first()));
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
        list.setListHeader(element.text());
        return list;
    }

}
