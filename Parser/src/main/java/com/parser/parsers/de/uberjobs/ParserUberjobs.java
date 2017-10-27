package com.parser.parsers.de.uberjobs;

import com.parser.entity.JobsInform;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserUberjobs implements ParserMain {

    private static final String START_LINK = "https://uberjobs.de";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();

    public ParserUberjobs() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        Document doc = PhantomJSStarter.startGhost(START_LINK);
        Elements tables2 = doc.select(".clickable");
        runParse(tables2);
        if (tables2.size() == 0) {
            jobsInforms = null;
        }
    }

    private void runParse(Elements tables2) {

        for (Element element : tables2)
            objectGenerator(element.select(".ort").first(),
                    element.select(".job-title").first(),
                    element.select(".anbieter").first(),
                    element.select("a").first());
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(null);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink("https://uberjobs.de/" + linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
