package com.parser.async_tasks;

import com.parser.dbmanager.DbHelper;
import com.parser.entity.JobsInformForSearch;

/**
 * Created by rolique_pc on 12/26/2016.
 */
public class TaskDBWriteSeen implements Runnable {

    private JobsInformForSearch j;

    public TaskDBWriteSeen(JobsInformForSearch j) {
        this.j = j;

    }

    @Override
    public void run() {
        new DbHelper().setIsSeen(j);
    }

}
