package com.parser.entity;

/**
 * Created by rolique_pc on 12/28/2016.
 */
public class JobsInformForSearch extends JobsInform {

    private String siteName;

    public JobsInformForSearch(JobsInform j, String siteName) {
        setId(j.getId());
        setPublishedDate(j.getPublishedDate());
        setSeen(j.isSeen());
        setOrder(j.getOrder());
        setPlace(j.getPlace());
        setCompanyName(j.getCompanyName());
        setHeadPublication(j.getHeadPublication());
        setPublicationLink(j.getPublicationLink());
        this.siteName = siteName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}
