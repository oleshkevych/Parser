package com.parser.parsers.com.indeed;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PrintDescription;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rolique_pc on 12/21/2016.
 */
public class ParserIndeed implements ParserMain {
    private String startLink = "http://www.canadajobs.com/canadajobs/jobs/viewjobresults.cfm?keywords=&category=&city=&start=";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
    private Document doc;
    private DateGenerator dateClass;

    public ParserIndeed() {
    }

    public List<JobsInform> startParse() {
        dateClass = new DateGenerator();
        parser();
        return jobsInforms;
    }

    private void parser() {
        List<String> stringCat = new ArrayList<>();
        stringCat.add("drupal");
        stringCat.add("angular");
        stringCat.add("react");
        stringCat.add("meteor");
        stringCat.add("node");
        stringCat.add("frontend");
        stringCat.add("javascript");
        stringCat.add("ios");
        stringCat.add("mobile");
        Map<String, String> countryLinksList = new HashMap<>();
        countryLinksList.put("https://ca.indeed.com/jobs?q=TTTTTT&sort=date", "Canada");
        countryLinksList.put("https://at.indeed.com/Jobs?q=TTTTTT&sort=date", "Austria");
        countryLinksList.put("https://be.indeed.com/Jobs?q=TTTTTT&sort=date", "Belgium");
        countryLinksList.put("https://cz.indeed.com/Jobs?q=TTTTTT&sort=date", "Czech Republic");
        countryLinksList.put("https://dk.indeed.com/Jobs?q=TTTTTT&sort=date", "Denmark");
        countryLinksList.put("https://ie.indeed.com/Jobs?q=TTTTTT&sort=date", "Ireland");
        countryLinksList.put("https://www.indeed.fi/jobs?q=TTTTTT&sort=date", "Finland");
        countryLinksList.put("https://www.indeed.nl/vacatures?q=TTTTTT&sort=date", "Netherlands");
        countryLinksList.put("https://no.indeed.com/Jobs?q=TTTTTT&sort=date", "Norway");
        countryLinksList.put("https://de.indeed.com/Jobs?q=TTTTTT&sort=date", "Germany");
        countryLinksList.put("https://se.indeed.com/Jobs?q=TTTTTT&sort=date", "Sweden");
        countryLinksList.put("https://www.indeed.ch/Stellen?q=TTTTTT&sort=date", "Switzerland");
        countryLinksList.put("https://www.indeed.co.uk/jobs?q=TTTTTT&sort=date", "United Kingdom");
        countryLinksList.put("https://www.indeed.com/jobs?q=TTTTTT&l=United+States&sort=date", "United States");

        Set<Map.Entry<String, String>> entries = countryLinksList.entrySet();

        for (Map.Entry<String, String> entry : entries) {
            for (String s : stringCat) {
                String loadLink = entry.getKey().replace("TTTTTT", s);

                try {

                    doc = Jsoup.connect(loadLink)
                            .validateTLSCertificates(false)
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                            .timeout(5000)
                            .get();

                    Elements tables2 = doc.select(".result");
                    Date lastDate = runParse(tables2, 0, entry.getValue());

                    if (dateClass.dateChecker(lastDate)) {
                        doc = Jsoup.connect(loadLink + "&start=10")
                                .validateTLSCertificates(false)
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                                .timeout(5000)
                                .get();

                        Elements tables1 = doc.select(".result");
                        runParse(tables1, 0, entry.getValue());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Date runParse(Elements tables2, int counter, String country) {
        System.out.println("text date : " + tables2.size());
        Date datePublished = null;
        for (int i = counter; i < tables2.size(); i += 1) {
            datePublished = null;
            String stringDate = tables2.get(i).select(".date").text();
            String date = stringDate.replaceAll("[^0-9]", "");
            if (!stringDate.contains("+") || (date.length() != 0 && Integer.parseInt(date) < 6)) {
                if (date.length() == 0) {
                    datePublished = new Date();
                } else {
                    datePublished = new Date(new Date().getTime() - Integer.parseInt(date) * 24 * 3600 * 1000);
                }
                objectGenerator(tables2.get(i).select(".location").first(),
                        tables2.get(i).select(".jobtitle").first(),
                        tables2.get(i).select(".company").first(),
                        tables2.get(i).select("a").first(), datePublished, country);
            }
        }
        return datePublished;
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription, Date datePublished, String country) {
        JobsInform jobsInform = new JobsInform();
        jobsInform.setPublishedDate(datePublished);
        jobsInform.setHeadPublication(headPost != null ? headPost.text() : "");
        jobsInform.setCompanyName(company != null ? company.text() : "");
        jobsInform.setPlace(country + " " + (place != null ? place.text() : ""));
        jobsInform.setPublicationLink(linkDescription.attr("abs:href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}