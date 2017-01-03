package com.parser.parsers.io.jobs_remotive;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserJobsRemotive implements ParserMain {
    private String startLink = "https://jobs.justlanded.com/en/TTTTT/Web-development?q_cid=10&q_ji=&q_jt=&q_k=&q_l%5B%5D=en&q_l_opt=all&q_salary_max=&q_salary_min=&q_salary_period_id=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;
    private WebDriver ghostDriver;

    public ParserJobsRemotive() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
//        try {
//            String urlS = ("http://api-public.addthis.com/url/shares.json?url=http%3A%2F%2Fjobs.remotive.io%2F&callback=_ate.cbs.rcb_5utu0");
//            URL url = new URL(urlS);
//            BufferedReader r = new BufferedReader(new InputStreamReader(
//                    ((HttpURLConnection) url.openConnection()).getInputStream()));
//            JsonParser jp = new JsonParser();
//
//            JsonElement jsonElement = jp.parse(r);
//
//            JsonObject jsonObject = jsonElement.getAsJsonObject();


//            System.out.println("text date : ParserJobspresso" + r.readLine());
////             need http protocol
//            r.close();
//            doc = Jsoup.connect("http://api.mixpanel.com/track/?data=eyJldmVudCI6ICJtcF9wYWdlX3ZpZXciLCJwcm9wZXJ0aWVzIjogeyIkb3MiOiAiV2luZG93cyIsIiRicm93c2VyIjogIkNocm9tZSIsIiRjdXJyZW50X3VybCI6ICJodHRwOi8vam9icy5yZW1vdGl2ZS5pby8iLCIkYnJvd3Nlcl92ZXJzaW9uIjogNTUsIiRzY3JlZW5faGVpZ2h0IjogMTA4MCwiJHNjcmVlbl93aWR0aCI6IDE5MjAsIm1wX2xpYiI6ICJ3ZWIiLCIkbGliX3ZlcnNpb24iOiAiMi45LjE3IiwiZGlzdGluY3RfaWQiOiAiMTU4ZDQ1NjJhYTExNWItMDE3MzIwOGE5NDA5MzQtNWM0ZjIzMWMtMWZhNDAwLTE1OGQ0NTYyYWEzMTVmIiwiJGluaXRpYWxfcmVmZXJyZXIiOiAiJGRpcmVjdCIsIiRpbml0aWFsX3JlZmVycmluZ19kb21haW4iOiAiJGRpcmVjdCIsIm1wX3BhZ2UiOiAiaHR0cDovL2pvYnMucmVtb3RpdmUuaW8vIiwibXBfYnJvd3NlciI6ICJDaHJvbWUiLCJtcF9wbGF0Zm9ybSI6ICJXaW5kb3dzIiwidG9rZW4iOiAiNmVjZWQyODM1YTBkYTUxMTkwZThjMmZkZDAzZTMxOGUifX0%3D&ip=1&_=1481798770674")
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(5000)
//                    .get();
//            System.out.println("text place1 : " + doc);

//        List<String> countries = new ArrayList<>();
//        countries.add("Austria");
//        countries.add("Belgium");
//        countries.add("Canada");
//        countries.add("Czech-Republic");
//        countries.add("Denmark");
//        countries.add("Finland");
//        countries.add("Germany");
//        countries.add("Ireland");
//        countries.add("Israel");
//        countries.add("Netherlands");
//        countries.add("Norway");
//        countries.add("Sweden");
//        countries.add("Switzerland");
//        countries.add("United-Kingdom");
//        countries.add("United-States");
//        for (String s : countries) {
        doc = PhantomJSStarter.startGhost("http://jobs.remotive.io/?category=engineering");
//        System.out.println("text place2 : " + doc);

        String json = doc.toString().substring(doc.toString().indexOf("entries") - 1);
        json = "{" + json.substring(0, json.indexOf("</script>"));
        System.out.println("text place2 : " + json);

        JsonParser jp = new JsonParser();
        JsonElement jsonElement = jp.parse(json);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jArray = (JsonArray) jsonObject.get("entries");


        JsonObject jobJson;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        for (int i = 0; i < jArray.size(); i++) {
            jobJson = (jArray.get(i).getAsJsonObject());
            String title = jobJson.get("role").getAsString();
            String place = jobJson.get("region").getAsString();
            String link =  jobJson.get("url").getAsString();
            String company = jobJson.get("company").getAsString();


            System.out.println("text place : " + place);
            System.out.println("text headPost : " + title);
            System.out.println("text company : " + company);
            System.out.println("text link : " + link);
            JobsInform jobsInform = new JobsInform();
            jobsInform.setCompanyName(company);
            jobsInform.setPlace(place);
            jobsInform.setHeadPublication(title);
            jobsInform.setPublicationLink(link);
            List<ListImpl> list = new ArrayList<ListImpl>();
            ListImpl list1 = new ListImpl();
            list1.setListHeader(title);
            list.add(list1);
            jobsInform.setOrder(list);
            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }

//            Elements tables2 = doc.select(".listings li");
//            runParse(tables2, 0, s);

//        }
//        }catch (IOException e){
//
//            e.printStackTrace();
//        }
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
//            System.out.println("text place : " + place.text());
//            System.out.println("text headPost : " + headPost.text());
//            System.out.println("text company : " + company.attr("alt").replaceAll(headPost.text(), ""));
//            jobsInform.setHeadPublication(headPost.text());
//            jobsInform.setCompanyName(company.attr("alt").replaceAll(headPost.text(), ""));
        jobsInform.setPlace(place);
        System.out.println("text  : " + linkDescription);
        if (linkDescription != null) {
            jobsInform.setPublicationLink(linkDescription.attr("href"));
            jobsInform = getDescription(linkDescription.attr("href"), jobsInform);

            if (!jobsInforms.contains(jobsInform)) {
                jobsInforms.add(jobsInform);
            }
        }

    }

    public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform) {
//        System.out.println("text link1 : " + linkToDescription);

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
            System.out.println("text place : " + tablesPlace.text());
            System.out.println("text headPost : " + jobsInform.getHeadPublication());
            System.out.println("text company : " + tablesCompany.text());
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(tablesHead));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            jobsInform.setPublishedDate(formatter.parse(tablesDate.text()));
//            System.out.println("text link1 : " + tablesDescription);

//            for (int i = 0; i < tablesDescription.size(); i++) {

////                System.out.println("text link1 : " + tablesDescription.size());
//                if (tablesDescription.get(i).tagName().contains("h")) {
//                    if (list.size() > 0) {
//                        list.add(null);
//                    }
//                    list.add(addHead(tablesDescription.get(i)));
//                } else if (tablesDescription.get(i).select("p").size() > 0 || tablesDescription.get(i).select("ul").size() > 0) {
////                   System.out.println("text p/ul : " + tablesDescription.get(i));
//                    for (Element element : tablesDescription.get(i).select(".col-md-12").first().children()) {
//                        if (element.tagName().equals("p")) {
//                            list.add(addParagraph(element));
////                           System.out.println("text p : " + element);
//                        } else if (element.tagName().equals("ul")) {
//                            list.add(addList(element));
//                        }
//                    }
//                }

//            }

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

    private static ListImpl addParagraph(Element element) {
        if (element.select("strong").size() > 0) {
            return addHead(element.select("strong").first());
        } else {


            ListImpl list = new ListImpl();
            list.setTextFieldImpl(element.text());
            return list;
        }
    }

    private static ListImpl addList(Element element) {
        ListImpl list = new ListImpl();
        List<String> strings = new ArrayList<String>();
        for (Element e : element.getAllElements()) {
            strings.add(e.text());
        }
        list.setListItem(strings);
        return list;
    }
}
