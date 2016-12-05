package com.parser;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by rolique_pc on 12/5/2016.
 */
public class JobsInform implements Serializable{
    private Date publishedDate;
    private String headPublication;
    private String headDescription;
    private List<ListImpl> order;
    private String publicationLink;
    private boolean isSeen;

    public JobsInform() {
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

    public String getHeadDescription() {
        return headDescription;
    }

    public void setHeadDescription(String headDescription) {
        this.headDescription = headDescription;
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
}
