package com.parser.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * Created by rolique_pc on 12/5/2016.
 */
public class JobsInform implements Serializable {
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        if (!getHeadPublication().equals(that.getHeadPublication())) return false;
        if (getPlace() != null ? !getPlace().toLowerCase().equals(that.getPlace().toLowerCase()) : that.getPlace() != null)
            return false;
        if (!getPublicationLink().toLowerCase().equals(that.getPublicationLink().toLowerCase())) return false;
        return getCompanyName() != null ? getCompanyName().toLowerCase().equals(that.getCompanyName().toLowerCase()) : that.getCompanyName() == null;

    }

    @Override
    public int hashCode() {
        int result = getHeadPublication().hashCode();
        result = 31 * result + (getPlace() != null ? getPlace().hashCode() : 0);
        result = 31 * result + getPublicationLink().hashCode();
        result = 31 * result + (getCompanyName() != null ? getCompanyName().hashCode() : 0);
        return result;
    }
}
