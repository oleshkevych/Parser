package com.parser.async_tasks;

import com.parser.dbmanager.DbHelper;
import com.parser.entity.JobsInform;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by rolique_pc on 12/26/2016.
 */
public class TaskReadDb implements Callable<List<JobsInform>> {

    private String siteName;
    private JPanel linkPanel;

    public TaskReadDb(String siteName) {
        this.siteName = siteName;
    }

    public TaskReadDb(JPanel linkPanel) {
        this.linkPanel = linkPanel;
    }

    public List<JobsInform> call() {
        if (siteName != null) {
            return new DbHelper().getJobsInformFromDb(siteName);
        } else {
            List<JobsInform> jobs = new ArrayList<>();
            for (int i = 0; i < linkPanel.getComponents().length; i++) {
                JPanel labelPanel = (JPanel) linkPanel.getComponents()[i];
                JLabel label = (JLabel) labelPanel.getComponent(0);
                String homeLink = label.getText();
                if (homeLink.contains(" ")) {
                    homeLink = label.getText().substring(0, label.getText().indexOf(" "));
                }
                jobs.addAll(new DbHelper().getJobsInformFromDb(homeLink));
            }
            return jobs;
        }
    }

}
