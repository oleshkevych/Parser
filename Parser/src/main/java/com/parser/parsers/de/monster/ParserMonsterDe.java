package com.parser.parsers.de.monster;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
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
 * Created by rolique_pc on 12/7/2016.
 */
public class ParserMonsterDe implements ParserMain {

    private String startLink = "http://www.monster.de/jobs/suche/?q=Festanstellung+Freie-Mitarbeit-Dienstvertrag+Vollzeit+Teilzeit+Befristet_88888?q=IT&intcid=HP_Nav";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private DateGenerator dateClass;

    public ParserMonsterDe() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> startLinksList = new ArrayList<>();
        startLinksList.add("http://www.monster.de/jobs/suche/?q=Drupal+&where=&intcid=swoop_HeroSearch&cy=de&rad=20");
        startLinksList.add("http://www.monster.de/jobs/suche/?q=Angular+&where=&intcid=swoop_HeroSearch&cy=de&rad=20");
        startLinksList.add("http://www.monster.de/jobs/suche/?q=React+&where=&intcid=swoop_HeroSearch&cy=de&rad=20");
        startLinksList.add("http://www.monster.de/jobs/suche/?q=Meteor+&where=&intcid=swoop_HeroSearch&cy=de&rad=20");
        startLinksList.add("http://www.monster.de/jobs/suche/?q=Node+&where=&intcid=swoop_HeroSearch&cy=de&rad=20");
        startLinksList.add("http://www.monster.de/jobs/suche/?q=Frontend+&where=&intcid=swoop_HeroSearch&cy=de&rad=20");
        startLinksList.add("http://www.monster.de/jobs/suche/?q=JavaScript+&where=&intcid=swoop_HeroSearch&cy=de&rad=20");
        startLinksList.add("http://www.monster.de/jobs/suche/?q=iOS+&where=&intcid=swoop_HeroSearch&cy=de&rad=20");
        startLinksList.add("http://www.monster.de/jobs/suche/?q=mobile+&where=&intcid=swoop_HeroSearch&cy=de&rad=20");

        int c = 0;
        for (String link : startLinksList) {
            try {

                Document doc = Jsoup.connect(link)
                        .validateTLSCertificates(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .timeout(5000)
                        .get();

                Elements tables2 = doc.select(".js_result_row");
                runParse(tables2);

                Date datePublished = null;
                int count = 2;
                do {
                    try {

                        datePublished = null;
                        doc = Jsoup.connect(link + "&page=" + count)
                                .validateTLSCertificates(false)
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                                .timeout(5000)
                                .get();

                        Elements tables1 = doc.select(".js_result_row");
                        datePublished = runParse(tables1);
                        count++;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                while (dateClass.dateChecker(datePublished) && jobsInforms.size() < (startLinksList.indexOf(link) + 1) * 200);

            } catch (IOException e) {
                e.printStackTrace();
                c++;
            }
        }
        if (c == startLinksList.size()) {
            jobsInforms = null;
        }
    }

    private Date runParse(Elements tables2) {
        System.out.println("text date : " + jobsInforms.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (Element element : tables2) {
            String stringDate = element.select("time").first().attr("datetime").substring(0, 10);
            try {
                datePublished = formatter.parse(stringDate);
                objectGenerator(element.select(".job-specs-location").first(),
                        element.select(".jobTitle").first(),
                        element.select(".company").first(),
                        datePublished,
                        element.select(".jobTitle a").first());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.text());
        jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
