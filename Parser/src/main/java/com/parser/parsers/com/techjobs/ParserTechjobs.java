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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserTechjobs implements ParserMain {

    private String startLink = "http://www.techjobs.com/SearchResults.aspx?query=djAuMXxSVjpRdWlja2xpc3Rpbmd8U086ZGF0ZSBkZXNjfFBOOjF8UFM6NTB8Q1I6c2VhcmNoc3RhcnRkYXRlOkRhdGVSZXN0cmljdGVkJm13ZW5jMztAVG9kYXktNyZtd2VuYzM7T1AmbXdlbmMzO1JhbmdlfElWOnNlYXJjaHN0YXJ0ZGF0ZTpEYXRlUmVzdHJpY3RlZF9AVG9kYXktN19PUF9SYW5nZXxOQTp3b3JrYXJlYTpeRGV2ZWxvcGVyJF9EZXZlbG9wZXJ+XiJJVCBTeXN0ZW1zIiRfSVQgU3lzdGVtc35eIklUIFN1cHBvcnQiJF9JVCBTdXBwb3J0fl4iU29mdHdhcmUgRW5naW5lZXJpbmciJF9Tb2Z0d2FyZSBFbmdpbmVlcmluZ35eIklUIFNlY3VyaXR5IiRfSVQgU2VjdXJpdHl+XkRhdGFiYXNlcyRfRGF0YWJhc2Vzfl4iUUEgLyBUZXN0aW5nIiRfUUEgLyBUZXN0aW5nfl4iVUkgRGVzaWduIiRfVUkgRGVzaWdufl4iTW9iaWxlIC8gQXBwcyIkX01vYmlsZSAvIEFwcHN8djAuMQ==&params=c2VhcmNoc3RhcnRkYXRlOjF8cXVlcnlmaWx0ZXI6fHdvcmthcmVhOjB8d29ya2FyZWFfbW9yZToxfEpvYmxvY2F0aW9uMTowfEpvYmxvY2F0aW9uMV9tb3JlOjB8Sm9ibG9jYXRpb246MHxKb2Jsb2NhdGlvbl9tb3JlOjB8Sm9ibG9jYXRpb24zOjB8Sm9ibG9jYXRpb24zX21vcmU6MHxKb2Jsb2NhdGlvbjQ6MHxKb2Jsb2NhdGlvbjRfbW9yZTowfEpvYmxvY2F0aW9uNTowfEpvYmxvY2F0aW9uNV9tb3JlOjB8Sm9idHlwZTowfEpvYnR5cGVfbW9yZTow";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();

    public ParserTechjobs() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        Document doc = PhantomJSStarter.startGhost(startLink);
        Elements tables2 = doc.select(".borderContainer");
        runParse(tables2);
        if (tables2.size() == 0) {
            jobsInforms = null;
        }
    }

    private void runParse(Elements tables2) {
        for (Element element : tables2)
            objectGenerator(element.select(".Location").first(),
                    element.select(".HeadingContainer").first(),
                    element.select(".Company").first(),
                    element.select("a").first());
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {

        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(null);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.text());
        String link = linkDescription.attr("href");

        String jobIdNumber = link.substring(link.indexOf("LJA") + 3, link.indexOf(",") - 1);
        String jobName = jobsInform.getHeadPublication().toLowerCase().replaceAll(" ", "_").replaceAll("[\\W]", "").replace("_", "-");
        //(\"[!@#/\\\\,()&£.-]\")
        String jobCount = jobsInforms.size() + 1 + "";
        jobsInform.setPublicationLink("http://www.techjobs.com/SearchResults/" + jobName + "-lja-" + jobIdNumber + ".aspx?jobId=LJA-" + jobIdNumber + "&list=SearchResultsJobsIds&index=" + jobCount + "&querydesc=SearchJobQueryDescription&viewedfrom=1");

        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
