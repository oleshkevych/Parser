package com.parser.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/27/2016.
 */
public class ExportJob {
    private String postingDate;
    private String jobTitle;
    private String companyName;
    private String sourceName;
    private String jobLink;
    private String location;


    public ExportJob(JobsInform j, String sourceName) {
        jobTitle = j.getHeadPublication();
        companyName = j.getCompanyName();
        jobLink = j.getPublicationLink();
        location = j.getPlace();
        postingDate = (j.getPublishedDate() != new Date(1) && j.getPublishedDate() != null) ? new SimpleDateFormat("dd-MM-yyyy").format(j.getPublishedDate()) : "";
        this.sourceName = sourceName;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getJobLink() {
        return jobLink;
    }

    public void setJobLink(String jobLink) {
        this.jobLink = jobLink;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
