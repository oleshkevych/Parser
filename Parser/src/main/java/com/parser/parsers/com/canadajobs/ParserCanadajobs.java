package com.parser.parsers.com.canadajobs;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.com.indeed.ParserIndeed;
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
 * Created by rolique_pc on 12/16/2016.
 */
public class ParserCanadajobs implements ParserMain {

    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private DateGenerator dateClass;

    public ParserCanadajobs() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=Drupal&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=angular&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=react&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=meteor&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=node&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=frontend&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=javascript&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=ios&category=&city=&start=1&page=1");
        startLinksList.add("http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=mobile&category=&city=&start=1&page=1");

        for (String link : startLinksList) {
            int count = 1;
            int count1 = 1;
            String loadLink = link;
            int countNew = 0;
            do {
                try {
                    countNew = 0;
                    Document doc = Jsoup.connect(loadLink + count1)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    Elements tables2 = doc.select(".results tr");
                    countNew = runParse(tables2);
                    count += 10;
                    count1++;
                    loadLink = link.replace("start=" + (count - 10), "start=" + (count));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            while (countNew != 0 && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 300);
        }
    }

    private int runParse(Elements tables2) {
        int countNew = 0;
        for (Element element : tables2) {
            countNew += objectGenerator(element.select("p").last(),
                    element.select("a").first(),
                    element.select("p").first(),
                    element.select("a").first());
        }
        return countNew;
    }

    private int objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.ownText());
        jobsInform.setPlace(place.text());

        if (linkDescription.attr("href").contains("http://ca.indeed.com")) {
            jobsInform.setPublicationLink(linkDescription.attr("href"));
        } else {
            jobsInform.setPublicationLink("http://www.canadajobs.com/canadajobs/jobs/" + linkDescription.attr("href"));
        }
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
            return 1;
        }
        return 0;
    }
}
