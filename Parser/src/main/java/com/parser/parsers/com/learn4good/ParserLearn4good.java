package com.parser.parsers.com.learn4good;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javafx.css.StyleOrigin.USER_AGENT;

/**
 * Created by rolique_pc on 12/16/2016.
 */
public class ParserLearn4good implements ParserMain {
    private String startLink = "https://www.learn4good.com/jobs/index.php?controller=job_list&action=display_search_results&page_number=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserLearn4good() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {


        // need http protocol
//            doc = Jsoup.connect(startLink)
//                    .validateTLSCertificates(false)
//                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                    .timeout(5000)
//                    .get();
//
//            Elements tables2 = doc.select(".searchResultsJobs tr");
////            System.out.println("text : " + tables2);
//            runParse(tables2, 0);

            String url = ("https://www.learn4good.com/jobs/index.php?controller=job_search&action=full_search");
//            String url = "https://selfsolve.apple.com/wcResults.do";
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Upgrade-Insecure-Requests", "1");

            String urlParameters = "country_id=0&state_id=0&city_id=0&online_status[]=onsite&online_status[]=online&job_category_ids[]=174&keywords=&min_edu_level_id=0&time_period=5&has_js=has_js";

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
//
//            //print result
            System.out.println(response.toString());
//
//
//        URL url = new URL(urlS);
//        BufferedReader r = new BufferedReader(new InputStreamReader(
//                ((HttpURLConnection) url.openConnection()).getInputStream()));
//
//        JsonParser jp = new JsonParser();
//
//            while (r.readLine()!=null) {
//                System.out.println(r.readLine());
//            }
//            JsonElement jsonElement = jp.parse(r);
//
//        r.close();

//        Date datePublished = null;
//        int count = 1;
//        do {
//            try {
//
//                datePublished = null;
//                doc = Jsoup.connect(startLink + count)
//                        .validateTLSCertificates(false)
//                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                        .timeout(5000)
//                        .get();
////                System.out.println("text date : " + doc);
//
//                Elements tables2 = doc.select("#main_job_list tr");
//                datePublished = runParse(tables2, 0);
//                count++;
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } while (dateClass.dateChecker(datePublished) /*&& jobsInforms.size() < 40*/);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Date runParse(Elements tables2, int counter) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = counter; i < tables2.size(); i += 1) {
                String stringDate = tables2.get(i).select(".posting_date").first().text();
            System.out.println("text date : " + stringDate);

            try {
                    datePublished = formatter.parse(stringDate);
                    objectGenerator(tables2.get(i).select(".loc_title").first(), tables2.get(i).select(".job_title").first(),
                            tables2.get(i).select("[itemprop='hiringOrganization']").first(), datePublished, tables2.get(i).select(".job_title").first());
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription) {
        if (dateClass.dateChecker(datePublished) /*&& jobsInforms.size() < 40*/) {
            JobsInform jobsInform = new JobsInform();
            jobsInform.setPublishedDate(datePublished);
            jobsInform.setHeadPublication(headPost.text());
            jobsInform.setCompanyName(company.text());
            jobsInform.setPlace(place.text());
            jobsInform.setPublicationLink(linkDescription.attr("href"));
            jobsInform = getDescription(linkDescription.attr("href"), jobsInform);
            if (!jobsInforms.contains(jobsInform)) {
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


            Elements tablesDescription = document.select("#content").first().select("div")
                    .select("*:not([id='search_container'])").select("*:not([id='under_content_afc'])");
            Element tablesHead = document.select("#content h1").first();
//            Element tablesPlace = document.select(".active-fields .displayFieldBlock").first().children().last();
//            jobsInform.setPlace(tablesPlace.text());
            List<ListImpl> list = new ArrayList<ListImpl>();
            list.add(addHead(tablesHead));


            for (int i = 0; i < tablesDescription.size(); i++) {

                if(!tablesDescription.get(i).hasClass("top_section")) {
                    if (tablesDescription.get(i).tagName().equals("div")) {
                        for (Element e : tablesDescription.get(i).children()) {
                            if (e.tagName().equals("p")) {
                                list.add(addParagraph(e));
                            } else if (e.tagName().equals("ul")) {
                                list.add(addList(e));
                            } else if (e.tagName().contains("h")) {
                                list.add(addHead(e));
                            } else if (e.tagName().contains("di")) {
                                for (Element e1 : e.children()) {
                                    if (e1.tagName().equals("p")) {
                                        list.add(addParagraph(e1));
                                    } else if (e1.tagName().equals("ul")) {
                                        list.add(addList(e1));
                                    } else if (e1.tagName().contains("h")) {
                                        list.add(addHead(e1));
                                    } else if (e1.tagName().equals("div")) {
                                        ListImpl list1 = new ListImpl();
                                        list1.setTextFieldImpl(e1.text());
                                        list.add(list1);
                                    }
                                }
                                ListImpl list1 = new ListImpl();
                                list1.setTextFieldImpl(e.ownText());
                                list.add(list1);
                            }
                        }
                    }
                    if (tablesDescription.get(i).tagName().equals("p")) {
                        list.add(addParagraph(tablesDescription.get(i)));
                    } else if (tablesDescription.get(i).tagName().equals("ul")) {
                        list.add(addList(tablesDescription.get(i)));
                    } else if (tablesDescription.get(i).tagName().contains("h")) {
                        list.add(addHead(tablesDescription.get(i)));
                    } else if (tablesDescription.get(i).tagName().equals("div")) {
                        ListImpl list1 = new ListImpl();
                        list1.setTextFieldImpl(tablesDescription.get(i).ownText());
                        list.add(list1);
                    }
                }
            }

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
        list.setListHeader(element.ownText());
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
