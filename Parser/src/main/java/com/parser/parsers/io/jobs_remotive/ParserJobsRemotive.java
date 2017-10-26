package com.parser.parsers.io.jobs_remotive;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import com.parser.parsers.PrintDescription;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserJobsRemotive implements ParserMain {
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserJobsRemotive() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {

        doc = PhantomJSStarter.startGhostJobsRemotive("http://jobs.remotive.io/?category=engineering");

//        String json = doc.toString().substring(doc.toString().indexOf("entries") - 1);
//        json = "{" + json.substring(0, json.indexOf("</script>"));
//
//        JsonParser jp = new JsonParser();
//        JsonElement jsonElement = jp.parse(json);
//        JsonObject jsonObject = jsonElement.getAsJsonObject();
//        JsonArray jArray = (JsonArray) jsonObject.get("entries");
//
//
//        JsonObject jobJson;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        for (int i = 0; i < jArray.size(); i++) {
//            jobJson = (jArray.get(i).getAsJsonObject());
//            String title = jobJson.get("role").getAsString();
//            String place = jobJson.get("region").getAsString();
//            String link =  jobJson.get("url").getAsString();
//            String company = jobJson.get("company").getAsString();
//            JobsInform jobsInform = new JobsInform();
//            jobsInform.setCompanyName(company);
//            jobsInform.setPlace(place);
//            jobsInform.setHeadPublication(title);
//            jobsInform.setPublicationLink(link);
//            List<ListImpl> list = new ArrayList<ListImpl>();
//            ListImpl list1 = new ListImpl();
//            list1.setListHeader(title);
//            list.add(list1);
//            jobsInform.setOrder(list);
//            if (!jobsInforms.contains(jobsInform)) {
//                jobsInforms.add(jobsInform);
//            }
//        }

        Elements tables2 = doc.select("ul.job_listings").first().children();
        runParse(tables2);
    }

    private void runParse(Elements tables2) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        for (Element element : tables2) {
            String stringDate = element.select(".job_listing-date").text();
            if (stringDate.contains("minut") || stringDate.contains("hour")) {
                datePublished = new Date();
            } else if (stringDate.contains("yesterday")) {
                datePublished = new Date(new Date().getTime() - 1 * 24 * 3600 * 1000);
            } else if (stringDate.contains("1 day")) {
                datePublished = new Date(new Date().getTime() - 1 * 24 * 3600 * 1000);
            } else if (stringDate.contains("2 day")) {
                datePublished = new Date(new Date().getTime() - 2 * 24 * 3600 * 1000);
            } else if (stringDate.contains("3 day")) {
                datePublished = new Date(new Date().getTime() - 3 * 24 * 3600 * 1000);
            } else if (stringDate.contains("4 day")) {
                datePublished = new Date(new Date().getTime() - 4 * 24 * 3600 * 1000);
            } else if (stringDate.contains("5 day")) {
                datePublished = new Date(new Date().getTime() - 5 * 24 * 3600 * 1000);
            } else if (stringDate.contains("6 day")) {
                datePublished = new Date(new Date().getTime() - 6 * 24 * 3600 * 1000);
            }
            objectGenerator(element.select(".job_listing-location").first(),
                    element.select(".job_listing-title").first(),
                    element.select("strong").first(),
                    datePublished,
                    element.select("a").first());
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished)) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPlace(place.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setPublicationLink(linkDescription.attr("href"));
            jobsInform = getDescription(linkDescription.attr("href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }
    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {
        try {
            Document document = Jsoup
                    .connect(linkToDescription)
                    .timeout(6000)
                    .method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .execute()
                    .parse();

            Elements tablesDescription = document.select("[itemprop='description']").first().children();
            Element tablesHead = document.select(".page-title").first();
            Element tablesCompany = document.select(".job-company").first();
            Element tablesPlace = document.select("[itemprop='jobLocation']").first();
            jobsInform.setCompanyName(tablesCompany.text());
            jobsInform.setPlace(tablesPlace.text());
            jobsInform.setHeadPublication(tablesHead.text());
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(tablesHead));

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
