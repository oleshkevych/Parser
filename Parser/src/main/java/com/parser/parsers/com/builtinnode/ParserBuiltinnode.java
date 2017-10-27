package com.parser.parsers.com.builtinnode;


import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/9/2016.
 */
public class ParserBuiltinnode implements ParserMain {

    private static final String START_LINK = "http://builtinnode.com/node-jobs";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private DateGenerator dateClass;

    public ParserBuiltinnode() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        Document doc = PhantomJSStarter.startGhost(START_LINK);

        Elements tables2 = doc.select(".template-perl-job-board");
        runParse(tables2);
    }

    private void runParse(Elements elements) {
        System.out.println("text date : " + elements.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        for (Element element : elements) {
            String stringDate = element.select("ul").first().select("li").last().text();
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(element.select("ul").first().select("br").first().nextElementSibling(), element.select(".heading").first(),
                        element.select("a").last(), datePublished, element.select(".heading a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished) && jobsInforms.size() < 1500) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            if (!company.text().contains("Read more")) {
                jobsInform.setCompanyName(company.text());
            } else {
                jobsInform.setCompanyName("");
            }
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }
}
