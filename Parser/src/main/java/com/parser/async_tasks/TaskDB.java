package com.parser.async_tasks;

import com.parser.dbmanager.DbHelper;
import com.parser.entity.JobsInform;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by rolique_pc on 12/26/2016.
 */
public class TaskDB implements Runnable {

    private String homeLink;
    private JPanel labelPanel;
    private List<JobsInform> jobsInforms;
    private String error;

    public TaskDB(JPanel labelPanel, List<JobsInform> jobsInforms, String homeLink) {
        this.homeLink = homeLink;
        this.labelPanel = labelPanel;
        this.jobsInforms = jobsInforms;
    }

    public TaskDB(JPanel labelPanel, String homeLink) {
        this.homeLink = homeLink;
        this.labelPanel = labelPanel;
        this.jobsInforms = null;
    }

    public TaskDB(String error, JPanel labelPanel) {
        this.error = error;
        this.labelPanel = labelPanel;
    }

    @Override
    public void run() {
        int newResults = 0;
        if (error != null) {
            JLabel label = (JLabel) labelPanel.getComponent(0);
            label.setForeground(new Color(0x5F0200));
            String text = label.getText();
            label.setText(text + " Error parser");
            labelPanel.setVisible(false);
            labelPanel.setBackground(new Color(-721665));
            labelPanel.setVisible(true);
        } else if (labelPanel == null) {
            new DbHelper().writeDB(homeLink, jobsInforms);
        } else {
            JLabel label = (JLabel) labelPanel.getComponent(0);
            if (jobsInforms == null) {
                newResults = new DbHelper().getNewResults(homeLink);
            } else {
                newResults = new DbHelper().writeDB(homeLink, jobsInforms);
                label.setForeground(new Color(0x5F0200));
            }
            if (newResults > 0) {
                String text = label.getText();
                label.setText(text + " new " + newResults);
            }
            labelPanel.setVisible(false);
            labelPanel.setBackground(new Color(-721665));
            labelPanel.setVisible(true);
        }
    }

}
