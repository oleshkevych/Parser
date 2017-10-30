package com.parser.parsers.com.jobs_smashingmagazine;

import com.parser.entity.DateGenerator;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.PhantomJSStarter;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URI;
import java.security.Provider;
import java.security.Security;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/15/2016.
 */
public class ParserJobsSmashingmagazine implements ParserMain {

    private final static String START_LINK = "https://jobs.smashingmagazine.com/";
    private List<JobsInform> jobsInforms = new ArrayList<JobsInform>();

    public ParserJobsSmashingmagazine() {
    }

    public List<JobsInform> startParse() {
        parser();
        return jobsInforms;
    }

    private void parser() {
        try {
            Provider[] providers = Security.getProviders();
            for (Provider provider : providers)
                System.out.println(provider.getInfo());
            Provider provider1 = providers[1];
            Security.insertProviderAt(new BouncyCastleProvider(),1);
            providers = Security.getProviders();
            for (Provider provider : providers)
                System.out.println(provider.getInfo());
            Document doc  = Jsoup.connect(START_LINK)
                        .validateTLSCertificates(false)
                        .timeout(6000)
                        .method(Connection.Method.GET)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                        .execute()
                        .parse();
            Security.insertProviderAt(provider1, 1);
            providers = Security.getProviders();
            for (Provider provider : providers)
                System.out.println(provider.getInfo());
            Elements tables2 = doc.select(".entry-list").first().children();
            runParse(tables2);
        } catch (Exception e) {
            e.printStackTrace();
            jobsInforms = null;
        }
    }

    private void runParse(Elements tables2) {
        System.out.println("text date : " + jobsInforms.size());
        for (Element aTables2 : tables2) {
            objectGenerator(aTables2.select(".entry-company").first(),
                    aTables2.select(".entry").first().child(0),
                    aTables2.select(".entry-company strong").first(),
                    aTables2.select("a").first());
        }
    }

    private void objectGenerator(Element place, Element headPost, Element company, Element linkDescription) {

        JobsInform jobsInform = new JobsInform();
        jobsInform.setHeadPublication(headPost.text());
        jobsInform.setCompanyName(company.text());
        jobsInform.setPlace(place.ownText());
        jobsInform.setPublicationLink(linkDescription.attr("href"));
        if (!jobsInforms.contains(jobsInform)) {
            jobsInforms.add(jobsInform);
        }
    }
}
