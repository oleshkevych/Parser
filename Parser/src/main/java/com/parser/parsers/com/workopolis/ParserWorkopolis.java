package com.parser.parsers.com.workopolis;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
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
 * Created by rolique_pc on 12/13/2016.
 */
public class ParserWorkopolis implements ParserMain {

    //    private String startLink = "http://www.workopolis.com/jobsearch/find-jobs#lg=en&st=POSTDATE%20%20%20%20&lr=50&pn=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;

    public ParserWorkopolis() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cdrupal%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cangular%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Creact%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cmeteor%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cnode%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cfrontend%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cjavascript%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cios%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");
        startLinksList.add("http://www.workopolis.com/jobsearch/find-jobs#lg=EN&ak=%7Cmobile%7C&st=POSTDATE%20%20%20%20&lr=50&pn=");

        int c = 0;
        for (String link : startLinksList) {
            int count = 1;
            try {
                boolean isContinue;
                do {
                    doc = Jsoup.connect(link + count)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();
                    Elements tables1 = doc.select(".search-result-item-link");
                    isContinue = runParse(tables1);
                    count++;
                }
                while (isContinue && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 200);
            } catch (IOException e) {
                e.printStackTrace();
                c++;
            }
        }
        if (c == startLinksList.size()) {
            jobsInforms = null;
        }

    }

    private boolean runParse(Elements tables2) {
        System.out.println("text date : " + jobsInforms.size());
        int countJobs = 0;
        for (Element element : tables2) {
            countJobs+= objectGenerator(element.select(".location").first(),
                    element.select(".job-title").first(),
                    element.select(".company").first(),
                    element.select("a.search-result-item-link").first());
        }
        return countJobs != 0;
    }

    private int objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink("http://www.workopolis.com" + linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
            return 1;
        }
        return 0;
    }
}
