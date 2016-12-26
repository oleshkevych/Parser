package com.parser.async_tasks;

import com.parser.ParserApp;
import com.parser.entity.JobsInform;
import com.parser.entity.ParserMain;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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
        labelPanel.setBackground(new Color(0x717184));
        List<JobsInform> jobsInforms = classParser.startParse();

        String text = ((JLabel)parserApp.getSelectedComponent()).getText();
        if (text.contains(" ")) {
            text = text.substring(0, text.indexOf(" "));
        }
        if(text.equals(siteName)) {
            labelPanel.setBackground(new Color(-721665));
            parserApp.panelFiller(jobsInforms, siteName);
            parserApp.getExecutorDB().execute(new TaskDB(jobsInforms, siteName));
        }else{
            parserApp.getExecutorDB().execute(new TaskDB(labelPanel, jobsInforms, siteName));
        }
    }
}
