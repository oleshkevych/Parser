package com.parser.async_tasks;

import com.parser.dbmanager.DbHelper;

/**
 * Created by rolique_pc on 12/26/2016.
 */
public class TaskSetDateUpdateDb implements Runnable {


        public TaskSetDateUpdateDb() {
        }

        public void run() {
            new DbHelper().setDateLastUpdate();
        }

}
