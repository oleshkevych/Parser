package com.parser.async_tasks;

import com.parser.dbmanager.DbHelper;
import com.parser.entity.JobsInform;
import com.parser.entity.JobsInformForSearch;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by rolique_pc on 12/26/2016.
 */
public class TaskReadDb implements Callable<List<JobsInformForSearch>> {

    private String siteName;
    private JPanel linkPanel;

    public TaskReadDb(String siteName) {
        this.siteName = siteName;
    }

    public TaskReadDb(JPanel linkPanel) {
        this.linkPanel = linkPanel;
    }

    public List<JobsInformForSearch> call() {
        List<JobsInformForSearch> jobs = new ArrayList<>();
        if (siteName != null) {
            for(JobsInform j: new DbHelper().getJobsInformFromDb(siteName)) {

                jobs.add(new JobsInformForSearch(j, siteName));
            }
            return jobs;
        } else {
            for (int i = 0; i < linkPanel.getComponents().length; i++) {
                JPanel labelPanel = (JPanel) linkPanel.getComponents()[i];
                JLabel label = (JLabel) labelPanel.getComponent(0);
                String homeLink = label.getText();
                if (homeLink.contains(" ")) {
                    homeLink = label.getText().substring(0, label.getText().indexOf(" "));
                }
                for(JobsInform j: new DbHelper().getJobsInformFromDb(homeLink)) {

                    jobs.add(new JobsInformForSearch(j, homeLink));
                }
            }
            return jobs;
        }
    }

}
