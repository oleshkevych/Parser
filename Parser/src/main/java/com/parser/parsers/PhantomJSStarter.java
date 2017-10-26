package com.parser.parsers;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.parser.Main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

/**
 * Created by rolique_pc on 12/29/2016.
 */
public class PhantomJSStarter {

    public static Document startGhost(String link) {
        WebDriver ghostDriver = startPhantom();
        try {
            ghostDriver.get(link);
            new WebDriverWait(ghostDriver, 15);
            return Jsoup.parse(ghostDriver.getPageSource());
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
    }

    public static Document startGhostJobsRemotive(String link) {
        WebDriver ghostDriver = startPhantom();
        try {
            ghostDriver.get(link);
            WebDriverWait wdw = new WebDriverWait(ghostDriver, 30);
            wdw.until(ExpectedConditions.visibilityOfElementLocated(By.className("job_listing-location")));
            return Jsoup.parse(ghostDriver.getPageSource());
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
    }

    public static Document startGhostJustlanded(String link) {
        WebDriver ghostDriver = startPhantom();
        try {
            ghostDriver.get(link);
            WebDriverWait wdw = new WebDriverWait(ghostDriver, 15);
            wdw.until(ExpectedConditions.visibilityOfElementLocated(By.className("listings")));
            return Jsoup.parse(ghostDriver.getPageSource());
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
    }

    public static Document startGhostF6s(String link) {
        WebDriver ghostDriver = startPhantom();
        try {
            ghostDriver.get(link);
            WebDriverWait wdw = new WebDriverWait(ghostDriver, 15);
            wdw.until(ExpectedConditions.visibilityOfElementLocated(By.className("result-info")));
            return Jsoup.parse(ghostDriver.getPageSource());
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
    }

    public static Document renderPage(String url, boolean description) {

        WebDriver ghostDriver = startPhantom();
        try {
            ghostDriver.get(url);
            WebDriverWait wait = new WebDriverWait(ghostDriver, 15);
            if (description) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("job")));
            } else {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("jobSmall")));
            }
            return Jsoup.parse(ghostDriver.getPageSource());
        } finally {
            ghostDriver.close();
            ghostDriver.quit();
        }
    }

    public static PhantomJSDriver startPhantom() {
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
        path = path.substring(0, path.lastIndexOf("/")) + File.separator + "lib" + File.separator + "phantomjs" + File.separator + "phantomjs.exe";

        path = path.replaceAll("%20", " ");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability("takesScreenshot", false);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, path);
        caps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.60 Safari/537.17");
//        caps.setCapability("phantomjs.page.settings.host", "https://www.themuse.com/jobs?keyword=drupal&filter=true&page=1&sort=primary_attributes_updated_at");
//        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{
//                "--web-security=false",
//                "--ssl-protocol=any",
//                "--ignore-ssl-errors=true"
//        });
        return new PhantomJSDriver(caps);

    }
}
