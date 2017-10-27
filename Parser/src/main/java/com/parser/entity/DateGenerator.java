package com.parser.entity;

import java.util.Date;

/**
 * Created by rolique_pc on 12/5/2016.
 */
public class DateGenerator {

    private Date lastAvailable;

    public DateGenerator(){
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        lastAvailable = new Date(System.currentTimeMillis() - (90 * DAY_IN_MS));
    }
    public Date getLastDate(){
        return lastAvailable;
    }

    public boolean dateChecker(Date date){
        return date != null && date.after(lastAvailable);
    }
}
