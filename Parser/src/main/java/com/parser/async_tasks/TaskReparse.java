package com.parser.async_tasks;

import com.parser.ParserApp;
import com.parser.dbmanager.DbHelper;
import com.parser.entity.JobsInform;
import com.parser.entity.JobsInformForSearch;
import com.parser.entity.ParserMain;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rolique_pc on 12/26/2016.
 */
public class TaskReparse implements Runnable {

    private String siteName;
    private ParserMain classParser;
    private ParserApp parserApp;
    private JPanel labelPanel;

    public TaskReparse(String siteName, ParserMain classParser, ParserApp parserApp, Component component) {
        this.siteName = siteName;
        this.classParser = classParser;
        this.parserApp = parserApp;
        this.labelPanel = (JPanel) component;
    }

    public void run() {
        try {
            labelPanel.setBackground(new Color(0x717184));
            List<JobsInform> jobsInforms = classParser.startParse();

            String text = ((JLabel) parserApp.getSelectedComponent()).getText();
            if (text.contains(" ")) {
                text = text.substring(0, text.indexOf(" "));
            }
            if (text.equals(siteName)) {
                labelPanel.setBackground(new Color(-721665));
                List<JobsInformForSearch> jobs = new ArrayList<>();
                new DbHelper().writeDB(siteName, jobsInforms);
                jobs.addAll(new DbHelper().getJobsInformFromDb(siteName).stream().map(j -> new JobsInformForSearch(j, siteName)).collect(Collectors.toList()));
                parserApp.panelFiller(jobs, siteName);
            } else {
                parserApp.getExecutorDB().execute(new TaskDB(labelPanel, jobsInforms, siteName));
            }
        }catch (Exception e){
            parserApp.getExecutorDB().execute(new TaskDB(e.getMessage(), labelPanel));
            e.printStackTrace();
        }
    }
}
