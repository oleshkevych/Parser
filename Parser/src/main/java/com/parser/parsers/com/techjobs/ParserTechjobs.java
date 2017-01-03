package com.parser.parsers.com.techjobs;

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
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserTechjobs implements ParserMain {
    private String startLink = "http://www.techjobs.com/SearchResults.aspx?query=djAuMXxSVjpRdWlja2xpc3Rpbmd8U086ZGF0ZSBkZXNjfFBOOjF8UFM6NTB8Q1I6c2VhcmNoc3RhcnRkYXRlOkRhdGVSZXN0cmljdGVkJm13ZW5jMztAVG9kYXktNyZtd2VuYzM7T1AmbXdlbmMzO1JhbmdlfElWOnNlYXJjaHN0YXJ0ZGF0ZTpEYXRlUmVzdHJpY3RlZF9AVG9kYXktN19PUF9SYW5nZXxOQTp3b3JrYXJlYTpeQ29tcHV0aW5nL0lUJF9Db21wdXRpbmcvSVR8djAuMQ==&params=c2VhcmNoc3RhcnRkYXRlOjF8cXVlcnlmaWx0ZXI6fHdvcmthcmVhOjB8d29ya2FyZWFfbW9yZTowfEpvYmxvY2F0aW9uMTowfEpvYmxvY2F0aW9uMV9tb3JlOjB8Sm9ibG9jYXRpb246MHxKb2Jsb2NhdGlvbl9tb3JlOjB8Sm9ibG9jYXRpb24zOjB8Sm9ibG9jYXRpb24zX21vcmU6MHxKb2Jsb2NhdGlvbjQ6MHxKb2Jsb2NhdGlvbjRfbW9yZTowfEpvYmxvY2F0aW9uNTowfEpvYmxvY2F0aW9uNV9tb3JlOjB8Sm9idHlwZTowfEpvYnR5cGVfbW9yZTow";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;

    private WebDriver ghostDriver;

    public ParserTechjobs() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {
            doc = PhantomJSStarter.startGhost(startLink);
            Elements tables2 = doc.select(".borderContainer");
            runParse(tables2, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runParse(Elements tables2, int counter) {

        for (int i = counter; i < tables2.size(); i += 1) {
            objectGenerator(tables2.get(i).select(".Location").first(), tables2.get(i).select(".HeadingContainer").first(),
                    tables2.get(i).select(".Company").first(), tables2.get(i).select("a").first());
        }

    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {

        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(null);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.text());
        List<ListImpl> list = new ArrayList<ListImpl>();
        ListImpl list1 = new ListImpl();
        list1.setListHeader("Link Is Not valid");
        ListImpl list2 = new ListImpl();
        list2.setTextFieldImpl(headPost.text());
        list.add(list1);
        list.add(list2);
        jobsInform.setOrder(list);
        String link =linkDescription.attr("href");


        String jobIdNumber = link.substring(link.indexOf("LJA")+3, link.indexOf(",") - 1);
        String jobName = jobsInform.getHeadPublication().toLowerCase().replaceAll(" ", "_").replaceAll("[\\W]", "").replace("_", "-");
        //(\"[!@#/\\\\,()&£.-]\")
        String jobCount = jobsInforms.size() + 1 + "";
        jobsInform.setPublicationLink("http://www.techjobs.com/SearchResults/"+jobName+"-lja-"+jobIdNumber+".aspx?jobId=LJA-"+jobIdNumber+"&list=SearchResultsJobsIds&index="+jobCount+"&querydesc=SearchJobQueryDescription&viewedfrom=1");


            jobsInform = getDescription(jobsInform.getPublicationLink(), jobsInform);
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
//        }
    }

    private JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {

        try {
            Document document = Jsoup.connect(linkToDescription)
                    .validateTLSCertificates(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(5000)
                    .get();
            Elements tablesDescription = document.select(".JobAd").first().children();
            Element tableDate = document.select("[itemprop='datePosted']").first();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            jobsInform.setPublishedDate(formatter.parse(tableDate.text()));
            Elements tablesHead = document.select("h1");
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
