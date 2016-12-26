package com.parser.async_tasks;

import com.parser.dbmanager.DbHelper;

/**
 * Created by rolique_pc on 12/26/2016.
 */
public class TaskDBWriteSeen implements Runnable {

    private String homeLink;

    public TaskDBWriteSeen(String homeLink) {
        this.homeLink = homeLink;

    }

    @Override
    public void run() {
        new DbHelper().setIsSeen(homeLink);
    }

}
