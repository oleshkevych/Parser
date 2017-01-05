package com.parser.parsers.com.jobs_justlanded;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rolique_pc on 1/3/2017.
 */
public class ParserJobsJustlanded implements ParserMain {
    private String startLink = "https://jobs.justlanded.com/en/TTTTT/Web-development?q_cid=10&q_ji=&q_jt=&q_k=&q_l%5B%5D=en&q_l_opt=all&q_salary_max=&q_salary_min=&q_salary_period_id=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserJobsJustlanded() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> countries = new ArrayList<>();
        countries.add("Austria");
        countries.add("Belgium");
        countries.add("Canada");
        countries.add("Czech-Republic");
        countries.add("Denmark");
        countries.add("Finland");
        countries.add("Germany");
        countries.add("Ireland");
        countries.add("Israel");
        countries.add("Netherlands");
        countries.add("Norway");
        countries.add("Sweden");
        countries.add("Switzerland");
        countries.add("United-Kingdom");
        countries.add("United-States");
        for (String s : countries) {
            doc = PhantomJSStarter.startGhostJustlanded(startLink.replace("TTTTT", s));

            Elements tables2 = doc.select(".listings li");
            runParse(tables2, 0, s);

        }
    }

    private void runParse(Elements tables2, int counter, String s) {
        System.out.println("text date : " + tables2.size());
        for (int i = counter; i < tables2.size(); i += 1) {
            objectGenerator(s, tables2.get(i).select("[itemprop='title']").first(),
                    tables2.get(i).select("a").first(), tables2.get(i).select(".title-wrapper a").first());
        }

    }

    private void objectGenerator(String place, Element headPost, Element company, Element linkDescription) {

        JobsInform jobsInform = new JobsInform();
        jobsInform.setPlace(place);
        if (linkDescription != null) {
            jobsInform.setPublicationLink(linkDescription.attr("href"));
            jobsInform = getDescription(linkDescription.attr("href"), jobsInform);

            if (dateClass.dateChecker(jobsInform.getPublishedDate()) && !jobsInforms.contains(jobsInform)) {
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


            Elements tablesDescription = document.select("[itemprop='description']").first().children();
            Element tablesHead = document.select("[itemprop='title']").first();
            Element tablesDate = document.select("time").first();
            Element tablesCompany = document.select(".postinfo span").first();
            Element tablesPlace = document.select("[itemprop='jobLocation']").first();
            jobsInform.setCompanyName(tablesCompany.text());
            jobsInform.setPlace(tablesPlace.text());
            jobsInform.setHeadPublication(tablesHead.text());
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(tablesHead));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String stringDate = tablesDate.text();
            String last = stringDate.substring(stringDate.lastIndexOf("/") + 1);
            stringDate = stringDate.substring(0, stringDate.lastIndexOf("/") + 1) + 20 + last;
            jobsInform.setPublishedDate(formatter.parse(stringDate));

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
