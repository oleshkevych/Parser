package com.parser.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rolique_pc on 12/5/2016.
 */
public class ListImpl implements Serializable{
    private String listHeader;
    private List<String> listItem;
    private String textFieldImpl;


    public ListImpl() {
    }

    public List<String> getListItem() {
        return listItem;
    }

    public void setListItem(List<String> listItem) {
        this.listItem = listItem;
    }

    public String getListHeader() {
        return listHeader;
    }

    public void setListHeader(String listHeader) {
        this.listHeader = listHeader;
    }

    public String getTextFieldImpl() {
        return textFieldImpl;
    }

    public void setTextFieldImpl(String textFieldImpl) {
        this.textFieldImpl = textFieldImpl;
    }

    @Override
    public String toString() {
        return "ListImpl{" +
                "listHeader='" + listHeader + '\'' +
                ", listItem=" + listItem +
                ", textFieldImpl='" + textFieldImpl + '\'' +
                '}';
    }
}
