package com.parser.entity;


import com.parser.entity.ListImpl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * Created by rolique_pc on 12/5/2016.
 */
public class JobsInform implements Serializable{
    private Date publishedDate;
    private String headPublication;
    private String place;
    private List<ListImpl> order;
    private String publicationLink;
    private String companyName;
    private boolean isSeen;

    public JobsInform() {
        setSeen(false);
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getHeadPublication() {
        return headPublication;
    }

    public void setHeadPublication(String headPublication) {
        this.headPublication = headPublication;
    }

    public List<ListImpl> getOrder() {
        return order;
    }

    public void setOrder(List<ListImpl> order) {
        this.order = order;
    }

    public String getPublicationLink() {
        return publicationLink;
    }

    public void setPublicationLink(String publicationLink) {
        this.publicationLink = publicationLink;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobsInform)) return false;

        JobsInform that = (JobsInform) o;

        if (!getPublishedDate().equals(that.getPublishedDate())) return false;
        return getPublicationLink().equals(that.getPublicationLink());

    }

    @Override
    public int hashCode() {
        int result = getPublishedDate().hashCode();
        result = 31 * result + getPublicationLink().hashCode();
        return result;
    }
}
