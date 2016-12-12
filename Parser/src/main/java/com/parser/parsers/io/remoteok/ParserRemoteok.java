package com.parser.parsers.io.remoteok;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.parsers.com.weworkremotely.ParserWeworkremotely;
import com.parser.parsers.io.wfh.ParserWFH;
import com.parser.parsers.jobs.landing.ParserLandingJobs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/6/2016.
 */
public class ParserRemoteok {

        private String startLink = "https://remoteok.io";
        private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();
        private Document doc;
        private DateGenerator dateClass;

        public ParserRemoteok(){
            dateClass = new DateGenerator();
            parser();
            System.out.println("FINISH ");

        }

        public List<JobsInform> getJobsInforms() {
            return jobsInforms;
        }

        private void parser() {
            try {


                // need http protocol
                doc = Jsoup.connect(startLink).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();

                Elements tables2 = doc.select("tbody .job td");

                runParse(tables2, 6);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void runParse(Elements tables2, int counter) {
            System.out.println("text date : " + tables2.size());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = counter; i<tables2.size(); i+=8) {
//                System.out.println("text date : " + tables2.get(i));

                Date datePublished = null;
                try {
                    datePublished = formatter.parse(tables2.get(i).child(0).text());
//                    System.out.println("text date : " + datePublished);
                    objectGenerator(tables2.get(i).child(2), tables2.get(i+2).child(0), tables2.get(i+2).child(2), datePublished, tables2.get(i+7));
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                } catch (IndexOutOfBoundsException e){
                    System.out.println(e.getMessage());
                    System.out.println(tables2.get(i));
                }

            }
        }

        private void objectGenerator(Element place, Element headPost, Element company, Date datePublished, Element linkDescription){
            if(dateClass.dateChecker(datePublished)) {
                JobsInform jobsInform = new JobsInform();
//                System.out.println("text place : " + place.text());
//                System.out.println("text headPost : " + headPost.text());
//                System.out.println("text company : " + company.text());
//                System.out.println("text link1 : " + linkDescription.child(0).attr("abs:href"));
                jobsInform.setPublishedDate(datePublished);
                jobsInform.setHeadPublication(headPost.text());
                jobsInform.setCompanyName(company.text());
                jobsInform.setPlace(place.text());
                jobsInform.setPublicationLink(linkDescription.child(0).attr("abs:href"));
                jobsInform = getDescription(linkDescription.child(0).attr("abs:href"), jobsInform);
                if(!jobsInforms.contains(jobsInform)) {
                    jobsInforms.add(jobsInform);
                }
            }
        }

        public static JobsInform getDescription(String linkToDescription, JobsInform jobsInform){

            try {
                Document document = Jsoup.connect(linkToDescription).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(5000).get();
                if(document.select(".navbar-brand img").attr("alt").contains("white")){
                    return ParserWFH.getDescription(linkToDescription, jobsInform);
                }

                if(document.select(".perma-logo img").attr("alt").contains("Work Remotely")){
                    return ParserWeworkremotely.getDescription(linkToDescription, jobsInform);
                }

                if(document.select(".ld-navbar-brand-link .ld-logo-standard").size()>0){
                    return ParserLandingJobs.getDescription(linkToDescription, jobsInform);
                }
                String locate = document.select(".location").text();
                if(locate.length()>0){
                    jobsInform.setPlace(locate);

                }
                String company = document.select(".employer").text();
                if(locate.length()>0){
                    jobsInform.setCompanyName(company);

                }
                Elements tablesDescription = document.select(".description");
                Elements tablesHeaders = document.select(".detail-sectionTitle");
                List<ListImpl> list = new ArrayList<ListImpl>();

                for (int i = 0; i<tablesDescription.size(); i++) {
                    list.add(null);
                    list.add(addHead(tablesHeaders.get(i)));

                    Elements ps = tablesDescription.get(i).select("p");
                    Elements uls = tablesDescription.get(i).select("ul");



                    if (ps.size() > 0) {
                        for(Element p: ps) {
                            list.add(addParagraph(p));
                        }
                    }
                    if (uls.size() > 0) {
                        for(Element ul: uls) {
                            list.add(addList(ul));
                        }
                    }
                }

                list.add(null);
                jobsInform.setOrder(list);


                return jobsInform;
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage()+" "+jobsInform.getPublicationLink());
                e.printStackTrace();
                return jobsInform;
            }

        }
        private static ListImpl addHead(Element element){
            ListImpl list = new ListImpl();
            list.setListHeader(element.text());
            return list;
        }
        private static ListImpl addParagraph(Element element){
            ListImpl list = new ListImpl();
            list.setTextFieldImpl(element.text());
            return list;
        }
        private static ListImpl addList(Element element){
            ListImpl list = new ListImpl();
            List<String> strings = new ArrayList<String>();
            for(Element e : element.getAllElements()) {
                strings.add(e.text());
            }
            list.setListItem(strings);
            return list;
        }



}
