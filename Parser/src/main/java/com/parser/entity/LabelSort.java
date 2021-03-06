package com.parser.entity;

/**
 * Created by rolique_pc on 1/5/2017.
 */
public class LabelSort {

    private String name;
    private Integer topPosition;

    public LabelSort(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTopPosition() {
        return topPosition;
    }

    public void setTopPosition(Integer topPosition) {
        this.topPosition = topPosition;
    }

    @Override
    public String toString() {
        return "LabelSort{" +
                "name='" + name + '\'' +
                ", topPosition=" + topPosition +
                '}';
    }
}
