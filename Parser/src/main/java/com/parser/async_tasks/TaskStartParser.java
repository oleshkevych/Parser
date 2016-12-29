package com.parser.async_tasks;

import com.parser.ParserApp;
import com.parser.entity.JobsInform;
import com.parser.entity.ParserMain;

import javax.swing.*;
import java.util.List;

/**
 * Created by rolique_pc on 12/26/2016.
 */
public class TaskStartParser implements Runnable {
    private String homeLink;
    private ParserMain classParser;
    private JPanel labelPanel;
    private ParserApp parserApp;

    public TaskStartParser(JPanel labelPanel, ParserMain classParser, String homeLink, ParserApp parserApp) {
        this.classParser = classParser;
        this.labelPanel = labelPanel;
        this.homeLink = homeLink;
        this.parserApp = parserApp;
    }

    @Override
    public void run() {

        try {
            List<JobsInform> jobsInforms = classParser.startParse();

            parserApp.getExecutorDB().execute(new TaskDB(labelPanel, jobsInforms, homeLink));
        }catch (Exception e){
            parserApp.getExecutorDB().execute(new TaskDB(e.getMessage(), labelPanel));
            e.printStackTrace();
        }

    }
}
