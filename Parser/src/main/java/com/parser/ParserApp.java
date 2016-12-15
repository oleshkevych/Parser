package com.parser;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.parser.dbmanager.DbHelper;
import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;
import com.parser.entity.ParserMain;
import com.parser.parsers.ca.eluta.ParserEluta;
import com.parser.parsers.cc.startus.ParserStartus;
import com.parser.parsers.ch.jobs.ParserJobs;
import com.parser.parsers.co.jobspresso.ParserJobspresso;
import com.parser.parsers.co.remote.ParserRemote;
import com.parser.parsers.co.workingnomads.ParserWorkingnomads;
import com.parser.parsers.com.authenticjobs.ParserAuthenticjobs;
import com.parser.parsers.com.berlinstartupjobs.ParserBerlinstartupjobs;
import com.parser.parsers.com.builtinnode.ParserBuiltinnode;
import com.parser.parsers.com.careerbuilder.ParserCareerbuilder;
import com.parser.parsers.com.dutchstartupjobs.ParserDutchstartupjobs;
import com.parser.parsers.com.eurojobs.ParserEurojobs;
import com.parser.parsers.com.flexjobs.ParserFlexjobs;
import com.parser.parsers.com.guru.ParserGugu;
import com.parser.parsers.com.jobs_smashingmagazine.ParserJobsSmashingmagazine;
import com.parser.parsers.com.juju.ParserJuju;
import com.parser.parsers.com.randstad.ParserRandstad;
import com.parser.parsers.com.simplyhired.ParserSimplyhired;
import com.parser.parsers.com.stackoverflow.ParserStackoverflow;
import com.parser.parsers.com.techjobs.ParserTechjobs;
import com.parser.parsers.com.themuse.ParserThemuse;
import com.parser.parsers.com.virtualvocations.ParserVirtualvocations;
import com.parser.parsers.com.weloveangular.ParserWeloveangular;
import com.parser.parsers.com.weworkmeteor.ParserWeworkmeteor;
import com.parser.parsers.com.weworkremotely.ParserWeworkremotely;
import com.parser.parsers.com.workopolis.ParserWorkopolis;
import com.parser.parsers.de.monster.ParserMonsterDe;
import com.parser.parsers.de.uberjobs.ParserUberjobs;
import com.parser.parsers.de.webentwicklerJobs.ParserWebentwicklerJobs;
import com.parser.parsers.dk.jobbank.ParserJobbank;
import com.parser.parsers.io.jobs_remotive.ParserJobsRemotive;
import com.parser.parsers.io.remoteok.ParserRemoteok;
import com.parser.parsers.io.webbjobb.ParserWebbjobb;
import com.parser.parsers.io.wfh.ParserWFH;
import com.parser.parsers.jobs.landing.ParserLandingJobs;
import com.parser.parsers.org.drupal.jobs.ParserDrupal;
import com.parser.parsers.se.startupjobs.ParserStartupjobsSe;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ParserApp implements MouseListener {

    private JPanel panelMain;
    private JPanel descriptionPanel;
    private JPanel linkPanel;
    private JPanel jobPanel;
    private JLabel wfhLink;
    private JLabel jobLabel;
    private JLabel descriptionLabel;
    private JPanel wfhLabelPanel;
    private JPanel remoteokLabelPanel;
    private JLabel remoteokLabel;
    private JPanel landingJobsPanel;
    private JLabel landingJobsLabel;
    private JPanel startusLabelPanel;
    private JLabel startusLabel;
    private JPanel virtualvocationsLabelPanel;
    private JLabel virtualvocationsLabel;
    private JPanel simplyhiredLabelPanel;
    private JLabel simplyhiredLabel;
    private JLabel stackoverflowLabel;
    private JPanel stackoverflowLabelPanel;
    private JPanel jujuLabelPanel;
    private JLabel jujuLabel;
    private JPanel drupalLubelPanel;
    private JLabel drupalLabel;
    private JPanel dutchstartupjobsLabelPanel;
    private JLabel dutchstartupjobsLabel;
    private JPanel monsterDeLabelPanel;
    private JLabel monsterDeLabel;
    private JPanel weloveangularLabelPanel;
    private JLabel weloveangularLabel;
    private JPanel weworkremotelyLabelPanel;
    private JLabel weworkremotelyLabel;
    private JPanel startupjobsLabelPanel;
    private JLabel startupjobsLabel;
    private JPanel berlinstartupjobsLabelPanel;
    private JLabel berlinstartupjobsLabel;
    private JPanel jobsChLabelPanel;
    private JLabel jobsChLabel;
    private JPanel flexjobslabelPanel;
    private JLabel flexjobsLabel;
    private JPanel builtinnodeLabelPanel;
    private JLabel builtinnodeLabel;
    private JPanel weworkmeteorLabelPanel;
    private JLabel weworkmeteorLabel;
    private JPanel jobbankLabelPanel;
    private JLabel jobbankLabel;
    private JPanel workingnomadsLabelPanel;
    private JLabel workingnomadsLabel;
    private JPanel remoteLabelPanel;
    private JLabel remoteLabel;
    private JPanel randstadLabelPanel;
    private JLabel randstadLabel;
    private JPanel workopolisLabelPanel;
    private JLabel workopolisLabel;
    private JPanel elutaLabelPanel;
    private JLabel elutaLabel;
    private JPanel jobspressoLabelPanel;
    private JLabel jobspressoLabel;
    private JPanel webbjobbLabelPrser;
    private JLabel webbjobbLabel;
    private JPanel jobsSmashingmagazineLabelPanel;
    private JLabel jobsSmashingmagazineLabel;
    private JPanel themuseLabelPanel;
    private JLabel themuseLabel;
    private JPanel techjobsLabelPanel;
    private JLabel techjobsLabel;
    private JPanel careerbuilderLabelPanel;
    private JLabel careerbuilderLabel;
    private JPanel webentwicklerJobsLabelPanel;
    private JLabel webentwicklerJobsLabel;
    private JPanel uberjobsLabelPanel;
    private JLabel uberjobsLabel;
    private JPanel guruLabelPanel;
    private JLabel guruLabel;
    private JPanel authenticjobsLabelParser;
    private JLabel authenticjobsLabel;
    private JPanel eurojobsLabelPanel;
    private JLabel eurojobsLabel;
    private JFrame jFrame = new JFrame();
    private Component c;
    private ExecutorService executorDB = Executors.newFixedThreadPool(1);


    public JPanel getPanelMain() {
        return panelMain;
    }

    public ParserApp() {
        Map<String, ParserMain> mapParsers = new HashMap<>();
        mapParsers.put("wfh.io", new ParserWFH());
        mapParsers.put("remoteok.io", new ParserRemoteok());
        mapParsers.put("landing.jobs", new ParserLandingJobs());
        mapParsers.put("startus.cc", new ParserStartus());
        mapParsers.put("virtualvocations.com", new ParserVirtualvocations());
        mapParsers.put("simplyhired.com", new ParserSimplyhired());
        mapParsers.put("stackoverflow.com", new ParserStackoverflow());
        mapParsers.put("juju.com", new ParserJuju());
        mapParsers.put("jobs.drupal.org", new ParserDrupal());
        mapParsers.put("dutchstartupjobs.com", new ParserDutchstartupjobs());
        mapParsers.put("monster.de", new ParserMonsterDe());
        mapParsers.put("weloveangular.com", new ParserWeloveangular());
        mapParsers.put("weworkremotely.com", new ParserWeworkremotely());
        mapParsers.put("startupjobs.se", new ParserStartupjobsSe());
        mapParsers.put("berlinstartupjobs.com", new ParserBerlinstartupjobs());
        mapParsers.put("jobs.ch", new ParserJobs());
        mapParsers.put("flexjobs.com", new ParserFlexjobs());
        mapParsers.put("builtinnode.com", new ParserBuiltinnode());
        mapParsers.put("weworkmeteor.com", new ParserWeworkmeteor());
        mapParsers.put("jobbank.dk", new ParserJobbank());
        mapParsers.put("workingnomads.co", new ParserWorkingnomads());
        mapParsers.put("remote.co", new ParserRemote());
        mapParsers.put("randstad.com", new ParserRandstad());
        mapParsers.put("workopolis.com", new ParserWorkopolis());
        mapParsers.put("eluta.ca", new ParserEluta());
        mapParsers.put("jobspresso.co", new ParserJobspresso());
        mapParsers.put("webbjobb.io", new ParserWebbjobb());
        mapParsers.put("jobs.smashingmagazine.com", new ParserJobsSmashingmagazine());
        mapParsers.put("themuse.com", new ParserThemuse());
        mapParsers.put("techjobs.com", new ParserTechjobs());
        mapParsers.put("careerbuilder.com", new ParserCareerbuilder());
        mapParsers.put("webentwickler-jobs.de", new ParserWebentwicklerJobs());
        mapParsers.put("uberjobs.de", new ParserUberjobs());
        mapParsers.put("guru.com", new ParserGugu());
        mapParsers.put("authenticjobs.com", new ParserAuthenticjobs());
        mapParsers.put("eurojobs.com", new ParserEurojobs());

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
//        for (int i = 0; i < linkPanel.getComponents().length; i++) {
//            linkPanel.getComponents()[i].setBackground(new Color(0x717184));
//            JPanel labelPanel = (JPanel) linkPanel.getComponents()[i];
//            JLabel label = (JLabel) labelPanel.getComponent(0);
//            String homeLink = label.getText();
//            executor.execute(new Task(labelPanel, mapParsers.get(homeLink), homeLink));
//        }

        JPanel labelPanel = (JPanel) linkPanel.getComponents()[35];
        labelPanel.setBackground(new Color(0x717184));
        JLabel label = (JLabel) labelPanel.getComponent(0);
        String homeLink = label.getText();
        executor.execute(new Task(labelPanel, mapParsers.get(homeLink), homeLink));

        c = wfhLink;
        wfhLink.addMouseListener(this);
        remoteokLabel.addMouseListener(this);
        landingJobsLabel.addMouseListener(this);
        startusLabel.addMouseListener(this);
        virtualvocationsLabel.addMouseListener(this);
        simplyhiredLabel.addMouseListener(this);
        stackoverflowLabel.addMouseListener(this);
        jujuLabel.addMouseListener(this);
        drupalLabel.addMouseListener(this);
        dutchstartupjobsLabel.addMouseListener(this);
        monsterDeLabel.addMouseListener(this);
        weloveangularLabel.addMouseListener(this);
        weworkremotelyLabel.addMouseListener(this);
        startupjobsLabel.addMouseListener(this);
        berlinstartupjobsLabel.addMouseListener(this);
        jobsChLabel.addMouseListener(this);
        flexjobsLabel.addMouseListener(this);
        builtinnodeLabel.addMouseListener(this);
        weworkmeteorLabel.addMouseListener(this);
        jobbankLabel.addMouseListener(this);
        workingnomadsLabel.addMouseListener(this);
        remoteLabel.addMouseListener(this);
        randstadLabel.addMouseListener(this);
        workopolisLabel.addMouseListener(this);
        elutaLabel.addMouseListener(this);
        jobspressoLabel.addMouseListener(this);
        webbjobbLabel.addMouseListener(this);
        jobsSmashingmagazineLabel.addMouseListener(this);
        themuseLabel.addMouseListener(this);
        techjobsLabel.addMouseListener(this);
        careerbuilderLabel.addMouseListener(this);
        webentwicklerJobsLabel.addMouseListener(this);
        uberjobsLabel.addMouseListener(this);
        guruLabel.addMouseListener(this);
        authenticjobsLabel.addMouseListener(this);
        eurojobsLabel.addMouseListener(this);
    }

    private void panelFiller(final List<JobsInform> jobsInformList, String homeLink) {

        jobPanel.setVisible(false);
        JPanel mainJobPanel = new JPanel();
        for (int i = 0; i < jobsInformList.size(); i++) {
            JobsInform ji = jobsInformList.get(i);
            JPanel label1Panel = new JPanel();
            String stringDate = ji.getPublishedDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(ji.getPublishedDate()) : "";
            JLabel label1 = new JLabel("VOCATION: " + ji.getHeadPublication());
            if (ji.isSeen()) {
                label1.setForeground(new Color(213123));
            } else {
                label1.setForeground(new Color(0x830200));
            }
            label1.setMaximumSize(new Dimension(540, 20));
            label1.setMinimumSize(new Dimension(540, 20));
            label1.setPreferredSize(new Dimension(540, 20));
            JLabel label2 = new JLabel("PLACE: " + ji.getPlace());
            label2.setMaximumSize(new Dimension(540, 20));
            label2.setMinimumSize(new Dimension(540, 20));
            label2.setPreferredSize(new Dimension(540, 20));
            JLabel label3 = new JLabel("COMPANY: " + ji.getCompanyName() + "     " + stringDate);
            label3.setForeground(new Color(0x308365));
            label1.setHorizontalAlignment(SwingConstants.LEFT);
            label2.setHorizontalAlignment(SwingConstants.LEFT);
            label3.setHorizontalAlignment(SwingConstants.LEFT);

            label1Panel.setVisible(true);
            label1Panel.setName("" + i);
            label1Panel.setLayout(new BoxLayout(label1Panel, BoxLayout.Y_AXIS));
            label1Panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        List<ListImpl> order = jobsInformList.get(Integer.parseInt(e.getComponent().getName())).getOrder();
                        final URI uri = new URI(jobsInformList.get(Integer.parseInt(e.getComponent().getName())).getPublicationLink());
                        descriptionPanel.removeAll();
                        descriptionPanel.setVisible(false);
                        JPanel container = new JPanel();
                        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
                        container.add(new JLabel(ji.getPublishedDate() != null ? (new SimpleDateFormat("dd-MM-yyyy").format(jobsInformList.get(Integer.parseInt(e.getComponent().getName())).getPublishedDate())) : ""));
                        JButton button = new JButton();
                        button.setText("<HTML><FONT size=\"3\" color=\"#000099\"><U>Run to the source site</U></FONT>"
                                + " </HTML>");
                        button.setHorizontalAlignment(SwingConstants.CENTER);
                        button.setBorderPainted(false);
                        button.setOpaque(false);
                        button.setBackground(Color.WHITE);
                        button.setToolTipText(uri.toString());
                        button.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                open(uri);
                            }
                        });
                        container.add(button);
                        jFrame.dispose();
                        jFrame = new JFrame();
                        jFrame.setSize(600, 800);


                        int counterDoc = 1;
                        if (order != null) {
                            for (ListImpl item : order) {
                                if (item == null) {
                                    counterDoc++;
                                }
                            }
                            int height = 0;
                            int counter = 0;
                            int j = -1;
                            do {
                                counter++;
                                String text = ""; /*"<HTML>"*/
                                ;
//

                                StyleContext sc = new StyleContext();
                                final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
                                JTextPane pane = new JTextPane(doc);
                                // Create and add the style
                                final Style heading2Style = sc.addStyle("Heading2", null);
                                heading2Style.addAttribute(StyleConstants.Foreground, Color.black);
                                heading2Style.addAttribute(StyleConstants.FontSize, 16);
                                heading2Style.addAttribute(StyleConstants.FontFamily, "serif");
                                heading2Style.addAttribute(StyleConstants.Bold, Boolean.TRUE);

                                if (order.size() > 0)
                                    do {
                                        j++;
//                                    System.out.println("date " + j + " size " + order.size());
//                                    System.out.println("date " + order.get(j));
                                        if (order.size() > j && order.get(j) != null) {

                                            if (order.get(j).getListHeader() != null) {
                                                text += /*"<FONT size=\"15\" color=\"#000099\"><U>Run to the source site</U></FONT>"*/order.get(j).getListHeader() + "\n";
                                            }
                                            if (order.get(j).getListItem() != null) {
//                                                text += "<UL>";
                                                for (String s : order.get(j).getListItem()) {
                                                    text += /*"    <LI> " +*/ "  --  " + s +  /*</LI> */"\n";
                                                }
//                                                text += "</UL>";

                                            }
                                            if (order.get(j).getTextFieldImpl() != null) {
                                                text += "  " + order.get(j).getTextFieldImpl() + "\n";
                                            }
                                        }
//                                    System.out.println("date " + text);
                                    } while (order.get(j) != null && j < order.size() - 1);
                                pane.setLayout(null);
//                                pane.setBounds(0, 0, 470, getContentHeight(text));

//                                    text += "</HTML>";
                                height += getContentHeight(text);
                                try {
                                    // Add the text to the document
                                    doc.insertString(0, text, null);

                                    // Finally, apply the style to the heading
                                    doc.setParagraphAttributes(0, 1, heading2Style, false);

                                    container.add(Box.createVerticalStrut(10));
                                    container.add(pane);
                                } catch (BadLocationException e1) {
                                }

                            } while (counterDoc > counter && j < order.size() - 1);
                            container.setMinimumSize(new Dimension(440, height));
                            container.setPreferredSize(new Dimension(440, height));
                            container.setMaximumSize(new Dimension(440, height));
                        }
                        jFrame.add(new JScrollPane(container));
                        jFrame.setLocationRelativeTo(null);
                        jFrame.setVisible(true);


//                            jFrame.add(new JScrollPane(container));


//                            JScrollPane scroll = new JScrollPane(jFrame, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
//                                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//                            scroll.setAutoscrolls(true);
//                            scroll.setSize(200, 500);
//                            descriptionPanel.add(scroll);
//
////                            wfhLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
//                            descriptionPanel.setVisible(true);
                    } catch (URISyntaxException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage());
                    }
                }
            });
            label1Panel.add(label1);
            label1Panel.add(label2);
            label1Panel.add(label3);
            label1Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            label1Panel.setAlignmentX(0.0f);
            label1Panel.setAlignmentY(0.0f);
            label1Panel.setAutoscrolls(true);
            label1Panel.setBackground(new Color(-721665));
            label1Panel.setMaximumSize(new Dimension(540, 60));
            label1Panel.setMinimumSize(new Dimension(540, 60));
            label1Panel.setPreferredSize(new Dimension(540, 60));
            mainJobPanel.add(label1Panel);
            label1Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));

        }
        boolean isChanged = false;
        for (JobsInform j : jobsInformList) {
            if (isChanged || !j.isSeen()) {
                isChanged = true;
            }
            j.setSeen(true);
        }
        if (isChanged) {
            executorDB.execute(new TaskDBWriteSeen(jobsInformList, homeLink));
        }
        mainJobPanel.setMinimumSize(new

                Dimension(580, 65 * jobsInformList.size()));
        mainJobPanel.setPreferredSize(new

                Dimension(580, 65 * jobsInformList.size()));
        mainJobPanel.setMaximumSize(new

                Dimension(510, 65 * jobsInformList.size()));
        JScrollPane pane = new JScrollPane(mainJobPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setAutoscrolls(true);
        pane.setBackground(new Color(-721665));
        pane.setAlignmentX(0.0f);
        pane.setAlignmentY(0.0f);
        pane.setMaximumSize(new Dimension(580, 740));
        pane.setMinimumSize(mainJobPanel.getMinimumSize());
        pane.setPreferredSize(new Dimension(580, 740));
        jobPanel.add(pane);
        jobPanel.setVisible(true);
        panelMain.setSize(652, 832);
    }


    private int getContentHeight(String content) {
        JTextPane dummyEditorPane = new JTextPane();
        dummyEditorPane.setSize(410, Short.MAX_VALUE);
        dummyEditorPane.setText(content);

        return dummyEditorPane.getPreferredSize().height;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("text speciality :111 ");
        linkPanel.setVisible(false);
        JLabel label = (JLabel) e.getComponent();
        c.setForeground(new Color(-16777216));
        c = label;
        c.setForeground(new Color(0x696969));
        linkPanel.setVisible(true);
        jobPanel.removeAll();
        if (label.getText().contains(" ")) {
            String text = label.getText().substring(0, label.getText().indexOf(" "));
            label.setText(text);
        }
        Future<List<JobsInform>> taskJobsInforms = executorDB.submit(new TaskReadDb(label.getText()));
        try {
            panelFiller(taskJobsInforms.get(), label.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, 30));
        panelMain.setAutoscrolls(false);
        panelMain.setBackground(new Color(-7631989));
        panelMain.setEnabled(true);
        panelMain.setFocusable(false);
        panelMain.setInheritsPopupMenu(false);
        panelMain.setMaximumSize(new Dimension(1600, 1000));
        panelMain.setMinimumSize(new Dimension(872, 1032));
        panelMain.setPreferredSize(new Dimension(872, 1032));
        panelMain.setVisible(true);
        panelMain.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "ParserApp", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font(panelMain.getFont().getName(), panelMain.getFont().getStyle(), 16), new Color(-16777216)));
        linkPanel = new JPanel();
        linkPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        linkPanel.setAutoscrolls(true);
        linkPanel.setBackground(new Color(-4473925));
        linkPanel.setEnabled(true);
        linkPanel.setFocusable(false);
        linkPanel.setFont(new Font("Times New Roman", linkPanel.getFont().getStyle(), 12));
        panelMain.add(linkPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(440, 780), new Dimension(440, 780), new Dimension(440, 780), 2, false));
        linkPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Links", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, new Font("DialogInput", Font.BOLD, 18), new Color(-16777216)));
        wfhLabelPanel = new JPanel();
        wfhLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wfhLabelPanel.setAlignmentX(0.0f);
        wfhLabelPanel.setAlignmentY(0.0f);
        wfhLabelPanel.setAutoscrolls(true);
        wfhLabelPanel.setBackground(new Color(-721665));
        wfhLabelPanel.setMaximumSize(new Dimension(210, 30));
        wfhLabelPanel.setMinimumSize(new Dimension(180, 30));
        wfhLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(wfhLabelPanel);
        wfhLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        wfhLink = new JLabel();
        wfhLink.setAutoscrolls(false);
        wfhLink.setEnabled(true);
        wfhLink.setFocusable(false);
        wfhLink.setFont(new Font("Times New Roman", wfhLink.getFont().getStyle(), 12));
        wfhLink.setForeground(new Color(-16777216));
        wfhLink.setHorizontalAlignment(2);
        wfhLink.setHorizontalTextPosition(2);
        wfhLink.setMaximumSize(new Dimension(170, 30));
        wfhLink.setMinimumSize(new Dimension(-1, -1));
        wfhLink.setOpaque(false);
        wfhLink.setPreferredSize(new Dimension(170, 30));
        wfhLink.setText("wfh.io");
        wfhLink.setToolTipText("www.wfh.io/jobs/3353-devops-engineer-waldo");
        wfhLink.setVerifyInputWhenFocusTarget(false);
        wfhLink.putClientProperty("html.disable", Boolean.TRUE);
        wfhLabelPanel.add(wfhLink);
        remoteokLabelPanel = new JPanel();
        remoteokLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        remoteokLabelPanel.setAlignmentX(0.0f);
        remoteokLabelPanel.setAlignmentY(0.0f);
        remoteokLabelPanel.setAutoscrolls(true);
        remoteokLabelPanel.setBackground(new Color(-721665));
        remoteokLabelPanel.setMaximumSize(new Dimension(210, 30));
        remoteokLabelPanel.setMinimumSize(new Dimension(180, 30));
        remoteokLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(remoteokLabelPanel);
        remoteokLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        remoteokLabel = new JLabel();
        remoteokLabel.setAutoscrolls(false);
        remoteokLabel.setEnabled(true);
        remoteokLabel.setFocusable(false);
        remoteokLabel.setFont(new Font("Times New Roman", remoteokLabel.getFont().getStyle(), 12));
        remoteokLabel.setForeground(new Color(-16777216));
        remoteokLabel.setHorizontalAlignment(2);
        remoteokLabel.setHorizontalTextPosition(2);
        remoteokLabel.setMaximumSize(new Dimension(170, 30));
        remoteokLabel.setMinimumSize(new Dimension(-1, -1));
        remoteokLabel.setOpaque(false);
        remoteokLabel.setPreferredSize(new Dimension(170, 30));
        remoteokLabel.setText("remoteok.io");
        remoteokLabel.setToolTipText("www.remoteok.io");
        remoteokLabel.setVerifyInputWhenFocusTarget(false);
        remoteokLabel.putClientProperty("html.disable", Boolean.TRUE);
        remoteokLabelPanel.add(remoteokLabel);
        landingJobsPanel = new JPanel();
        landingJobsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        landingJobsPanel.setAlignmentX(0.0f);
        landingJobsPanel.setAlignmentY(0.0f);
        landingJobsPanel.setAutoscrolls(true);
        landingJobsPanel.setBackground(new Color(-721665));
        landingJobsPanel.setMaximumSize(new Dimension(210, 30));
        landingJobsPanel.setMinimumSize(new Dimension(180, 30));
        landingJobsPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(landingJobsPanel);
        landingJobsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        landingJobsLabel = new JLabel();
        landingJobsLabel.setAutoscrolls(false);
        landingJobsLabel.setEnabled(true);
        landingJobsLabel.setFocusable(false);
        landingJobsLabel.setFont(new Font("Times New Roman", landingJobsLabel.getFont().getStyle(), 12));
        landingJobsLabel.setForeground(new Color(-16777216));
        landingJobsLabel.setHorizontalAlignment(2);
        landingJobsLabel.setHorizontalTextPosition(2);
        landingJobsLabel.setMaximumSize(new Dimension(170, 30));
        landingJobsLabel.setMinimumSize(new Dimension(-1, -1));
        landingJobsLabel.setOpaque(false);
        landingJobsLabel.setPreferredSize(new Dimension(170, 30));
        landingJobsLabel.setText("landing.jobs");
        landingJobsLabel.setToolTipText("https://landing.jobs");
        landingJobsLabel.setVerifyInputWhenFocusTarget(false);
        landingJobsLabel.putClientProperty("html.disable", Boolean.TRUE);
        landingJobsPanel.add(landingJobsLabel);
        startusLabelPanel = new JPanel();
        startusLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        startusLabelPanel.setAlignmentX(0.0f);
        startusLabelPanel.setAlignmentY(0.0f);
        startusLabelPanel.setAutoscrolls(true);
        startusLabelPanel.setBackground(new Color(-721665));
        startusLabelPanel.setMaximumSize(new Dimension(210, 30));
        startusLabelPanel.setMinimumSize(new Dimension(180, 30));
        startusLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(startusLabelPanel);
        startusLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        startusLabel = new JLabel();
        startusLabel.setAutoscrolls(false);
        startusLabel.setEnabled(true);
        startusLabel.setFocusable(false);
        startusLabel.setFont(new Font("Times New Roman", startusLabel.getFont().getStyle(), 12));
        startusLabel.setForeground(new Color(-16777216));
        startusLabel.setHorizontalAlignment(2);
        startusLabel.setHorizontalTextPosition(2);
        startusLabel.setMaximumSize(new Dimension(170, 30));
        startusLabel.setMinimumSize(new Dimension(-1, -1));
        startusLabel.setOpaque(false);
        startusLabel.setPreferredSize(new Dimension(170, 30));
        startusLabel.setText("startus.cc");
        startusLabel.setToolTipText("http://www.startus.cc/");
        startusLabel.setVerifyInputWhenFocusTarget(false);
        startusLabel.putClientProperty("html.disable", Boolean.TRUE);
        startusLabelPanel.add(startusLabel);
        virtualvocationsLabelPanel = new JPanel();
        virtualvocationsLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        virtualvocationsLabelPanel.setAlignmentX(0.0f);
        virtualvocationsLabelPanel.setAlignmentY(0.0f);
        virtualvocationsLabelPanel.setAutoscrolls(true);
        virtualvocationsLabelPanel.setBackground(new Color(-721665));
        virtualvocationsLabelPanel.setMaximumSize(new Dimension(210, 30));
        virtualvocationsLabelPanel.setMinimumSize(new Dimension(180, 30));
        virtualvocationsLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(virtualvocationsLabelPanel);
        virtualvocationsLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        virtualvocationsLabel = new JLabel();
        virtualvocationsLabel.setAutoscrolls(false);
        virtualvocationsLabel.setEnabled(true);
        virtualvocationsLabel.setFocusable(false);
        virtualvocationsLabel.setFont(new Font("Times New Roman", virtualvocationsLabel.getFont().getStyle(), 12));
        virtualvocationsLabel.setForeground(new Color(-16777216));
        virtualvocationsLabel.setHorizontalAlignment(2);
        virtualvocationsLabel.setHorizontalTextPosition(2);
        virtualvocationsLabel.setMaximumSize(new Dimension(170, 30));
        virtualvocationsLabel.setMinimumSize(new Dimension(-1, -1));
        virtualvocationsLabel.setOpaque(false);
        virtualvocationsLabel.setPreferredSize(new Dimension(170, 30));
        virtualvocationsLabel.setText("virtualvocations.com");
        virtualvocationsLabel.setToolTipText("http://www.virtualvocations.com/job");
        virtualvocationsLabel.setVerifyInputWhenFocusTarget(false);
        virtualvocationsLabel.putClientProperty("html.disable", Boolean.TRUE);
        virtualvocationsLabelPanel.add(virtualvocationsLabel);
        simplyhiredLabelPanel = new JPanel();
        simplyhiredLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        simplyhiredLabelPanel.setAlignmentX(0.0f);
        simplyhiredLabelPanel.setAlignmentY(0.0f);
        simplyhiredLabelPanel.setAutoscrolls(true);
        simplyhiredLabelPanel.setBackground(new Color(-721665));
        simplyhiredLabelPanel.setMaximumSize(new Dimension(210, 30));
        simplyhiredLabelPanel.setMinimumSize(new Dimension(180, 30));
        simplyhiredLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(simplyhiredLabelPanel);
        simplyhiredLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        simplyhiredLabel = new JLabel();
        simplyhiredLabel.setAutoscrolls(false);
        simplyhiredLabel.setEnabled(true);
        simplyhiredLabel.setFocusable(false);
        simplyhiredLabel.setFont(new Font("Times New Roman", simplyhiredLabel.getFont().getStyle(), 12));
        simplyhiredLabel.setForeground(new Color(-16777216));
        simplyhiredLabel.setHorizontalAlignment(2);
        simplyhiredLabel.setHorizontalTextPosition(2);
        simplyhiredLabel.setMaximumSize(new Dimension(170, 30));
        simplyhiredLabel.setMinimumSize(new Dimension(-1, -1));
        simplyhiredLabel.setOpaque(false);
        simplyhiredLabel.setPreferredSize(new Dimension(170, 30));
        simplyhiredLabel.setText("simplyhired.com");
        simplyhiredLabel.setToolTipText("http://www.simplyhired.com/");
        simplyhiredLabel.setVerifyInputWhenFocusTarget(false);
        simplyhiredLabel.putClientProperty("html.disable", Boolean.TRUE);
        simplyhiredLabelPanel.add(simplyhiredLabel);
        stackoverflowLabelPanel = new JPanel();
        stackoverflowLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        stackoverflowLabelPanel.setAlignmentX(0.0f);
        stackoverflowLabelPanel.setAlignmentY(0.0f);
        stackoverflowLabelPanel.setAutoscrolls(true);
        stackoverflowLabelPanel.setBackground(new Color(-721665));
        stackoverflowLabelPanel.setMaximumSize(new Dimension(210, 30));
        stackoverflowLabelPanel.setMinimumSize(new Dimension(180, 30));
        stackoverflowLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(stackoverflowLabelPanel);
        stackoverflowLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        stackoverflowLabel = new JLabel();
        stackoverflowLabel.setAutoscrolls(false);
        stackoverflowLabel.setEnabled(true);
        stackoverflowLabel.setFocusable(false);
        stackoverflowLabel.setFont(new Font("Times New Roman", stackoverflowLabel.getFont().getStyle(), 12));
        stackoverflowLabel.setForeground(new Color(-16777216));
        stackoverflowLabel.setHorizontalAlignment(2);
        stackoverflowLabel.setHorizontalTextPosition(2);
        stackoverflowLabel.setMaximumSize(new Dimension(170, 30));
        stackoverflowLabel.setMinimumSize(new Dimension(-1, -1));
        stackoverflowLabel.setOpaque(false);
        stackoverflowLabel.setPreferredSize(new Dimension(170, 30));
        stackoverflowLabel.setText("stackoverflow.com");
        stackoverflowLabel.setToolTipText("http://www.stackoverflow.com");
        stackoverflowLabel.setVerifyInputWhenFocusTarget(false);
        stackoverflowLabel.putClientProperty("html.disable", Boolean.TRUE);
        stackoverflowLabelPanel.add(stackoverflowLabel);
        jujuLabelPanel = new JPanel();
        jujuLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        jujuLabelPanel.setAlignmentX(0.0f);
        jujuLabelPanel.setAlignmentY(0.0f);
        jujuLabelPanel.setAutoscrolls(true);
        jujuLabelPanel.setBackground(new Color(-721665));
        jujuLabelPanel.setMaximumSize(new Dimension(210, 30));
        jujuLabelPanel.setMinimumSize(new Dimension(180, 30));
        jujuLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(jujuLabelPanel);
        jujuLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        jujuLabel = new JLabel();
        jujuLabel.setAutoscrolls(false);
        jujuLabel.setEnabled(true);
        jujuLabel.setFocusable(false);
        jujuLabel.setFont(new Font("Times New Roman", jujuLabel.getFont().getStyle(), 12));
        jujuLabel.setForeground(new Color(-16777216));
        jujuLabel.setHorizontalAlignment(2);
        jujuLabel.setHorizontalTextPosition(2);
        jujuLabel.setMaximumSize(new Dimension(170, 30));
        jujuLabel.setMinimumSize(new Dimension(-1, -1));
        jujuLabel.setOpaque(false);
        jujuLabel.setPreferredSize(new Dimension(170, 30));
        jujuLabel.setText("juju.com");
        jujuLabel.setToolTipText("http://www.juju.com");
        jujuLabel.setVerifyInputWhenFocusTarget(false);
        jujuLabel.putClientProperty("html.disable", Boolean.TRUE);
        jujuLabelPanel.add(jujuLabel);
        drupalLubelPanel = new JPanel();
        drupalLubelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        drupalLubelPanel.setAlignmentX(0.0f);
        drupalLubelPanel.setAlignmentY(0.0f);
        drupalLubelPanel.setAutoscrolls(true);
        drupalLubelPanel.setBackground(new Color(-721665));
        drupalLubelPanel.setMaximumSize(new Dimension(210, 30));
        drupalLubelPanel.setMinimumSize(new Dimension(180, 30));
        drupalLubelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(drupalLubelPanel);
        drupalLubelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        drupalLabel = new JLabel();
        drupalLabel.setAutoscrolls(false);
        drupalLabel.setEnabled(true);
        drupalLabel.setFocusable(false);
        drupalLabel.setFont(new Font("Times New Roman", drupalLabel.getFont().getStyle(), 12));
        drupalLabel.setForeground(new Color(-16777216));
        drupalLabel.setHorizontalAlignment(2);
        drupalLabel.setHorizontalTextPosition(2);
        drupalLabel.setMaximumSize(new Dimension(170, 30));
        drupalLabel.setMinimumSize(new Dimension(-1, -1));
        drupalLabel.setOpaque(false);
        drupalLabel.setPreferredSize(new Dimension(170, 30));
        drupalLabel.setText("jobs.drupal.org");
        drupalLabel.setToolTipText("http://jobs.drupal.org");
        drupalLabel.setVerifyInputWhenFocusTarget(false);
        drupalLabel.putClientProperty("html.disable", Boolean.TRUE);
        drupalLubelPanel.add(drupalLabel);
        dutchstartupjobsLabelPanel = new JPanel();
        dutchstartupjobsLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        dutchstartupjobsLabelPanel.setAlignmentX(0.0f);
        dutchstartupjobsLabelPanel.setAlignmentY(0.0f);
        dutchstartupjobsLabelPanel.setAutoscrolls(true);
        dutchstartupjobsLabelPanel.setBackground(new Color(-721665));
        dutchstartupjobsLabelPanel.setMaximumSize(new Dimension(210, 30));
        dutchstartupjobsLabelPanel.setMinimumSize(new Dimension(180, 30));
        dutchstartupjobsLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(dutchstartupjobsLabelPanel);
        dutchstartupjobsLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        dutchstartupjobsLabel = new JLabel();
        dutchstartupjobsLabel.setAutoscrolls(false);
        dutchstartupjobsLabel.setEnabled(true);
        dutchstartupjobsLabel.setFocusable(false);
        dutchstartupjobsLabel.setFont(new Font("Times New Roman", dutchstartupjobsLabel.getFont().getStyle(), 12));
        dutchstartupjobsLabel.setForeground(new Color(-16777216));
        dutchstartupjobsLabel.setHorizontalAlignment(2);
        dutchstartupjobsLabel.setHorizontalTextPosition(2);
        dutchstartupjobsLabel.setMaximumSize(new Dimension(170, 30));
        dutchstartupjobsLabel.setMinimumSize(new Dimension(-1, -1));
        dutchstartupjobsLabel.setOpaque(false);
        dutchstartupjobsLabel.setPreferredSize(new Dimension(170, 30));
        dutchstartupjobsLabel.setText("dutchstartupjobs.com");
        dutchstartupjobsLabel.setToolTipText("http://dutchstartupjobs.com");
        dutchstartupjobsLabel.setVerifyInputWhenFocusTarget(false);
        dutchstartupjobsLabel.putClientProperty("html.disable", Boolean.TRUE);
        dutchstartupjobsLabelPanel.add(dutchstartupjobsLabel);
        monsterDeLabelPanel = new JPanel();
        monsterDeLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        monsterDeLabelPanel.setAlignmentX(0.0f);
        monsterDeLabelPanel.setAlignmentY(0.0f);
        monsterDeLabelPanel.setAutoscrolls(true);
        monsterDeLabelPanel.setBackground(new Color(-721665));
        monsterDeLabelPanel.setMaximumSize(new Dimension(210, 30));
        monsterDeLabelPanel.setMinimumSize(new Dimension(180, 30));
        monsterDeLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(monsterDeLabelPanel);
        monsterDeLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        monsterDeLabel = new JLabel();
        monsterDeLabel.setAutoscrolls(false);
        monsterDeLabel.setEnabled(true);
        monsterDeLabel.setFocusable(false);
        monsterDeLabel.setFont(new Font("Times New Roman", monsterDeLabel.getFont().getStyle(), 12));
        monsterDeLabel.setForeground(new Color(-16777216));
        monsterDeLabel.setHorizontalAlignment(2);
        monsterDeLabel.setHorizontalTextPosition(2);
        monsterDeLabel.setMaximumSize(new Dimension(170, 30));
        monsterDeLabel.setMinimumSize(new Dimension(-1, -1));
        monsterDeLabel.setOpaque(false);
        monsterDeLabel.setPreferredSize(new Dimension(170, 30));
        monsterDeLabel.setText("monster.de");
        monsterDeLabel.setToolTipText("http://www.monster.de");
        monsterDeLabel.setVerifyInputWhenFocusTarget(false);
        monsterDeLabel.putClientProperty("html.disable", Boolean.TRUE);
        monsterDeLabelPanel.add(monsterDeLabel);
        weloveangularLabelPanel = new JPanel();
        weloveangularLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        weloveangularLabelPanel.setAlignmentX(0.0f);
        weloveangularLabelPanel.setAlignmentY(0.0f);
        weloveangularLabelPanel.setAutoscrolls(true);
        weloveangularLabelPanel.setBackground(new Color(-721665));
        weloveangularLabelPanel.setMaximumSize(new Dimension(210, 30));
        weloveangularLabelPanel.setMinimumSize(new Dimension(180, 30));
        weloveangularLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(weloveangularLabelPanel);
        weloveangularLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        weloveangularLabel = new JLabel();
        weloveangularLabel.setAutoscrolls(false);
        weloveangularLabel.setEnabled(true);
        weloveangularLabel.setFocusable(false);
        weloveangularLabel.setFont(new Font("Times New Roman", weloveangularLabel.getFont().getStyle(), 12));
        weloveangularLabel.setForeground(new Color(-16777216));
        weloveangularLabel.setHorizontalAlignment(2);
        weloveangularLabel.setHorizontalTextPosition(2);
        weloveangularLabel.setMaximumSize(new Dimension(170, 30));
        weloveangularLabel.setMinimumSize(new Dimension(-1, -1));
        weloveangularLabel.setOpaque(false);
        weloveangularLabel.setPreferredSize(new Dimension(170, 30));
        weloveangularLabel.setText("weloveangular.com");
        weloveangularLabel.setToolTipText("http://www.weloveangular.com");
        weloveangularLabel.setVerifyInputWhenFocusTarget(false);
        weloveangularLabel.putClientProperty("html.disable", Boolean.TRUE);
        weloveangularLabelPanel.add(weloveangularLabel);
        weworkremotelyLabelPanel = new JPanel();
        weworkremotelyLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        weworkremotelyLabelPanel.setAlignmentX(0.0f);
        weworkremotelyLabelPanel.setAlignmentY(0.0f);
        weworkremotelyLabelPanel.setAutoscrolls(true);
        weworkremotelyLabelPanel.setBackground(new Color(-721665));
        weworkremotelyLabelPanel.setMaximumSize(new Dimension(210, 30));
        weworkremotelyLabelPanel.setMinimumSize(new Dimension(180, 30));
        weworkremotelyLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(weworkremotelyLabelPanel);
        weworkremotelyLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        weworkremotelyLabel = new JLabel();
        weworkremotelyLabel.setAutoscrolls(false);
        weworkremotelyLabel.setEnabled(true);
        weworkremotelyLabel.setFocusable(false);
        weworkremotelyLabel.setFont(new Font("Times New Roman", weworkremotelyLabel.getFont().getStyle(), 12));
        weworkremotelyLabel.setForeground(new Color(-16777216));
        weworkremotelyLabel.setHorizontalAlignment(2);
        weworkremotelyLabel.setHorizontalTextPosition(2);
        weworkremotelyLabel.setMaximumSize(new Dimension(170, 30));
        weworkremotelyLabel.setMinimumSize(new Dimension(-1, -1));
        weworkremotelyLabel.setOpaque(false);
        weworkremotelyLabel.setPreferredSize(new Dimension(170, 30));
        weworkremotelyLabel.setText("weworkremotely.com");
        weworkremotelyLabel.setToolTipText("http://www.weworkremotely.com");
        weworkremotelyLabel.setVerifyInputWhenFocusTarget(false);
        weworkremotelyLabel.putClientProperty("html.disable", Boolean.TRUE);
        weworkremotelyLabelPanel.add(weworkremotelyLabel);
        startupjobsLabelPanel = new JPanel();
        startupjobsLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        startupjobsLabelPanel.setAlignmentX(0.0f);
        startupjobsLabelPanel.setAlignmentY(0.0f);
        startupjobsLabelPanel.setAutoscrolls(true);
        startupjobsLabelPanel.setBackground(new Color(-721665));
        startupjobsLabelPanel.setMaximumSize(new Dimension(210, 30));
        startupjobsLabelPanel.setMinimumSize(new Dimension(180, 30));
        startupjobsLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(startupjobsLabelPanel);
        startupjobsLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        startupjobsLabel = new JLabel();
        startupjobsLabel.setAutoscrolls(false);
        startupjobsLabel.setEnabled(true);
        startupjobsLabel.setFocusable(false);
        startupjobsLabel.setFont(new Font("Times New Roman", startupjobsLabel.getFont().getStyle(), 12));
        startupjobsLabel.setForeground(new Color(-16777216));
        startupjobsLabel.setHorizontalAlignment(2);
        startupjobsLabel.setHorizontalTextPosition(2);
        startupjobsLabel.setMaximumSize(new Dimension(170, 30));
        startupjobsLabel.setMinimumSize(new Dimension(-1, -1));
        startupjobsLabel.setOpaque(false);
        startupjobsLabel.setPreferredSize(new Dimension(170, 30));
        startupjobsLabel.setText("startupjobs.se");
        startupjobsLabel.setToolTipText("http://startupjobs.se/");
        startupjobsLabel.setVerifyInputWhenFocusTarget(false);
        startupjobsLabel.putClientProperty("html.disable", Boolean.TRUE);
        startupjobsLabelPanel.add(startupjobsLabel);
        berlinstartupjobsLabelPanel = new JPanel();
        berlinstartupjobsLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        berlinstartupjobsLabelPanel.setAlignmentX(0.0f);
        berlinstartupjobsLabelPanel.setAlignmentY(0.0f);
        berlinstartupjobsLabelPanel.setAutoscrolls(true);
        berlinstartupjobsLabelPanel.setBackground(new Color(-721665));
        berlinstartupjobsLabelPanel.setMaximumSize(new Dimension(210, 30));
        berlinstartupjobsLabelPanel.setMinimumSize(new Dimension(180, 30));
        berlinstartupjobsLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(berlinstartupjobsLabelPanel);
        berlinstartupjobsLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        berlinstartupjobsLabel = new JLabel();
        berlinstartupjobsLabel.setAutoscrolls(false);
        berlinstartupjobsLabel.setEnabled(true);
        berlinstartupjobsLabel.setFocusable(false);
        berlinstartupjobsLabel.setFont(new Font("Times New Roman", berlinstartupjobsLabel.getFont().getStyle(), 12));
        berlinstartupjobsLabel.setForeground(new Color(-16777216));
        berlinstartupjobsLabel.setHorizontalAlignment(2);
        berlinstartupjobsLabel.setHorizontalTextPosition(2);
        berlinstartupjobsLabel.setMaximumSize(new Dimension(170, 30));
        berlinstartupjobsLabel.setMinimumSize(new Dimension(-1, -1));
        berlinstartupjobsLabel.setOpaque(false);
        berlinstartupjobsLabel.setPreferredSize(new Dimension(170, 30));
        berlinstartupjobsLabel.setText("berlinstartupjobs.com");
        berlinstartupjobsLabel.setToolTipText("http://berlinstartupjobs.com/");
        berlinstartupjobsLabel.setVerifyInputWhenFocusTarget(false);
        berlinstartupjobsLabel.putClientProperty("html.disable", Boolean.TRUE);
        berlinstartupjobsLabelPanel.add(berlinstartupjobsLabel);
        jobsChLabelPanel = new JPanel();
        jobsChLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        jobsChLabelPanel.setAlignmentX(0.0f);
        jobsChLabelPanel.setAlignmentY(0.0f);
        jobsChLabelPanel.setAutoscrolls(true);
        jobsChLabelPanel.setBackground(new Color(-721665));
        jobsChLabelPanel.setMaximumSize(new Dimension(210, 30));
        jobsChLabelPanel.setMinimumSize(new Dimension(180, 30));
        jobsChLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(jobsChLabelPanel);
        jobsChLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        jobsChLabel = new JLabel();
        jobsChLabel.setAutoscrolls(false);
        jobsChLabel.setEnabled(true);
        jobsChLabel.setFocusable(false);
        jobsChLabel.setFont(new Font("Times New Roman", jobsChLabel.getFont().getStyle(), 12));
        jobsChLabel.setForeground(new Color(-16777216));
        jobsChLabel.setHorizontalAlignment(2);
        jobsChLabel.setHorizontalTextPosition(2);
        jobsChLabel.setMaximumSize(new Dimension(170, 30));
        jobsChLabel.setMinimumSize(new Dimension(-1, -1));
        jobsChLabel.setOpaque(false);
        jobsChLabel.setPreferredSize(new Dimension(170, 30));
        jobsChLabel.setText("jobs.ch");
        jobsChLabel.setToolTipText("http://www.jobs.ch/");
        jobsChLabel.setVerifyInputWhenFocusTarget(false);
        jobsChLabel.putClientProperty("html.disable", Boolean.TRUE);
        jobsChLabelPanel.add(jobsChLabel);
        flexjobslabelPanel = new JPanel();
        flexjobslabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        flexjobslabelPanel.setAlignmentX(0.0f);
        flexjobslabelPanel.setAlignmentY(0.0f);
        flexjobslabelPanel.setAutoscrolls(true);
        flexjobslabelPanel.setBackground(new Color(-721665));
        flexjobslabelPanel.setMaximumSize(new Dimension(210, 30));
        flexjobslabelPanel.setMinimumSize(new Dimension(180, 30));
        flexjobslabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(flexjobslabelPanel);
        flexjobslabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        flexjobsLabel = new JLabel();
        flexjobsLabel.setAutoscrolls(false);
        flexjobsLabel.setEnabled(true);
        flexjobsLabel.setFocusable(false);
        flexjobsLabel.setFont(new Font("Times New Roman", flexjobsLabel.getFont().getStyle(), 12));
        flexjobsLabel.setForeground(new Color(-16777216));
        flexjobsLabel.setHorizontalAlignment(2);
        flexjobsLabel.setHorizontalTextPosition(2);
        flexjobsLabel.setMaximumSize(new Dimension(170, 30));
        flexjobsLabel.setMinimumSize(new Dimension(-1, -1));
        flexjobsLabel.setOpaque(false);
        flexjobsLabel.setPreferredSize(new Dimension(170, 30));
        flexjobsLabel.setText("flexjobs.com");
        flexjobsLabel.setToolTipText("http://www.flexjobs.com");
        flexjobsLabel.setVerifyInputWhenFocusTarget(false);
        flexjobsLabel.putClientProperty("html.disable", Boolean.TRUE);
        flexjobslabelPanel.add(flexjobsLabel);
        builtinnodeLabelPanel = new JPanel();
        builtinnodeLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        builtinnodeLabelPanel.setAlignmentX(0.0f);
        builtinnodeLabelPanel.setAlignmentY(0.0f);
        builtinnodeLabelPanel.setAutoscrolls(true);
        builtinnodeLabelPanel.setBackground(new Color(-721665));
        builtinnodeLabelPanel.setMaximumSize(new Dimension(210, 30));
        builtinnodeLabelPanel.setMinimumSize(new Dimension(180, 30));
        builtinnodeLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(builtinnodeLabelPanel);
        builtinnodeLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        builtinnodeLabel = new JLabel();
        builtinnodeLabel.setAutoscrolls(false);
        builtinnodeLabel.setEnabled(true);
        builtinnodeLabel.setFocusable(false);
        builtinnodeLabel.setFont(new Font("Times New Roman", builtinnodeLabel.getFont().getStyle(), 12));
        builtinnodeLabel.setForeground(new Color(-16777216));
        builtinnodeLabel.setHorizontalAlignment(2);
        builtinnodeLabel.setHorizontalTextPosition(2);
        builtinnodeLabel.setMaximumSize(new Dimension(170, 30));
        builtinnodeLabel.setMinimumSize(new Dimension(-1, -1));
        builtinnodeLabel.setOpaque(false);
        builtinnodeLabel.setPreferredSize(new Dimension(170, 30));
        builtinnodeLabel.setText("builtinnode.com");
        builtinnodeLabel.setToolTipText("http://builtinnode.com/");
        builtinnodeLabel.setVerifyInputWhenFocusTarget(false);
        builtinnodeLabel.putClientProperty("html.disable", Boolean.TRUE);
        builtinnodeLabelPanel.add(builtinnodeLabel);
        weworkmeteorLabelPanel = new JPanel();
        weworkmeteorLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        weworkmeteorLabelPanel.setAlignmentX(0.0f);
        weworkmeteorLabelPanel.setAlignmentY(0.0f);
        weworkmeteorLabelPanel.setAutoscrolls(true);
        weworkmeteorLabelPanel.setBackground(new Color(-721665));
        weworkmeteorLabelPanel.setMaximumSize(new Dimension(210, 30));
        weworkmeteorLabelPanel.setMinimumSize(new Dimension(180, 30));
        weworkmeteorLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(weworkmeteorLabelPanel);
        weworkmeteorLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        weworkmeteorLabel = new JLabel();
        weworkmeteorLabel.setAutoscrolls(false);
        weworkmeteorLabel.setEnabled(true);
        weworkmeteorLabel.setFocusable(false);
        weworkmeteorLabel.setFont(new Font("Times New Roman", weworkmeteorLabel.getFont().getStyle(), 12));
        weworkmeteorLabel.setForeground(new Color(-16777216));
        weworkmeteorLabel.setHorizontalAlignment(2);
        weworkmeteorLabel.setHorizontalTextPosition(2);
        weworkmeteorLabel.setMaximumSize(new Dimension(170, 30));
        weworkmeteorLabel.setMinimumSize(new Dimension(-1, -1));
        weworkmeteorLabel.setOpaque(false);
        weworkmeteorLabel.setPreferredSize(new Dimension(170, 30));
        weworkmeteorLabel.setText("weworkmeteor.com");
        weworkmeteorLabel.setToolTipText("http://www.weworkmeteor.com");
        weworkmeteorLabel.setVerifyInputWhenFocusTarget(false);
        weworkmeteorLabel.putClientProperty("html.disable", Boolean.TRUE);
        weworkmeteorLabelPanel.add(weworkmeteorLabel);
        jobbankLabelPanel = new JPanel();
        jobbankLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        jobbankLabelPanel.setAlignmentX(0.0f);
        jobbankLabelPanel.setAlignmentY(0.0f);
        jobbankLabelPanel.setAutoscrolls(true);
        jobbankLabelPanel.setBackground(new Color(-721665));
        jobbankLabelPanel.setMaximumSize(new Dimension(210, 30));
        jobbankLabelPanel.setMinimumSize(new Dimension(180, 30));
        jobbankLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(jobbankLabelPanel);
        jobbankLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        jobbankLabel = new JLabel();
        jobbankLabel.setAutoscrolls(false);
        jobbankLabel.setEnabled(true);
        jobbankLabel.setFocusable(false);
        jobbankLabel.setFont(new Font("Times New Roman", jobbankLabel.getFont().getStyle(), 12));
        jobbankLabel.setForeground(new Color(-16777216));
        jobbankLabel.setHorizontalAlignment(2);
        jobbankLabel.setHorizontalTextPosition(2);
        jobbankLabel.setMaximumSize(new Dimension(170, 30));
        jobbankLabel.setMinimumSize(new Dimension(-1, -1));
        jobbankLabel.setOpaque(false);
        jobbankLabel.setPreferredSize(new Dimension(170, 30));
        jobbankLabel.setText("jobbank.dk");
        jobbankLabel.setToolTipText("http://jobbank.dk");
        jobbankLabel.setVerifyInputWhenFocusTarget(false);
        jobbankLabel.putClientProperty("html.disable", Boolean.TRUE);
        jobbankLabelPanel.add(jobbankLabel);
        workingnomadsLabelPanel = new JPanel();
        workingnomadsLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        workingnomadsLabelPanel.setAlignmentX(0.0f);
        workingnomadsLabelPanel.setAlignmentY(0.0f);
        workingnomadsLabelPanel.setAutoscrolls(true);
        workingnomadsLabelPanel.setBackground(new Color(-721665));
        workingnomadsLabelPanel.setMaximumSize(new Dimension(210, 30));
        workingnomadsLabelPanel.setMinimumSize(new Dimension(180, 30));
        workingnomadsLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(workingnomadsLabelPanel);
        workingnomadsLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        workingnomadsLabel = new JLabel();
        workingnomadsLabel.setAutoscrolls(false);
        workingnomadsLabel.setEnabled(true);
        workingnomadsLabel.setFocusable(false);
        workingnomadsLabel.setFont(new Font("Times New Roman", workingnomadsLabel.getFont().getStyle(), 12));
        workingnomadsLabel.setForeground(new Color(-16777216));
        workingnomadsLabel.setHorizontalAlignment(2);
        workingnomadsLabel.setHorizontalTextPosition(2);
        workingnomadsLabel.setMaximumSize(new Dimension(170, 30));
        workingnomadsLabel.setMinimumSize(new Dimension(-1, -1));
        workingnomadsLabel.setOpaque(false);
        workingnomadsLabel.setPreferredSize(new Dimension(170, 30));
        workingnomadsLabel.setText("workingnomads.co");
        workingnomadsLabel.setToolTipText("http://www.workingnomads.co");
        workingnomadsLabel.setVerifyInputWhenFocusTarget(false);
        workingnomadsLabel.putClientProperty("html.disable", Boolean.TRUE);
        workingnomadsLabelPanel.add(workingnomadsLabel);
        remoteLabelPanel = new JPanel();
        remoteLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        remoteLabelPanel.setAlignmentX(0.0f);
        remoteLabelPanel.setAlignmentY(0.0f);
        remoteLabelPanel.setAutoscrolls(true);
        remoteLabelPanel.setBackground(new Color(-721665));
        remoteLabelPanel.setMaximumSize(new Dimension(210, 30));
        remoteLabelPanel.setMinimumSize(new Dimension(180, 30));
        remoteLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(remoteLabelPanel);
        remoteLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        remoteLabel = new JLabel();
        remoteLabel.setAutoscrolls(false);
        remoteLabel.setEnabled(true);
        remoteLabel.setFocusable(false);
        remoteLabel.setFont(new Font("Times New Roman", remoteLabel.getFont().getStyle(), 12));
        remoteLabel.setForeground(new Color(-16777216));
        remoteLabel.setHorizontalAlignment(2);
        remoteLabel.setHorizontalTextPosition(2);
        remoteLabel.setMaximumSize(new Dimension(170, 30));
        remoteLabel.setMinimumSize(new Dimension(-1, -1));
        remoteLabel.setOpaque(false);
        remoteLabel.setPreferredSize(new Dimension(170, 30));
        remoteLabel.setText("remote.co");
        remoteLabel.setToolTipText("http://www.remote.co");
        remoteLabel.setVerifyInputWhenFocusTarget(false);
        remoteLabel.putClientProperty("html.disable", Boolean.TRUE);
        remoteLabelPanel.add(remoteLabel);
        randstadLabelPanel = new JPanel();
        randstadLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        randstadLabelPanel.setAlignmentX(0.0f);
        randstadLabelPanel.setAlignmentY(0.0f);
        randstadLabelPanel.setAutoscrolls(true);
        randstadLabelPanel.setBackground(new Color(-721665));
        randstadLabelPanel.setMaximumSize(new Dimension(210, 30));
        randstadLabelPanel.setMinimumSize(new Dimension(180, 30));
        randstadLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(randstadLabelPanel);
        randstadLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        randstadLabel = new JLabel();
        randstadLabel.setAutoscrolls(false);
        randstadLabel.setEnabled(true);
        randstadLabel.setFocusable(false);
        randstadLabel.setFont(new Font("Times New Roman", randstadLabel.getFont().getStyle(), 12));
        randstadLabel.setForeground(new Color(-16777216));
        randstadLabel.setHorizontalAlignment(2);
        randstadLabel.setHorizontalTextPosition(2);
        randstadLabel.setMaximumSize(new Dimension(170, 30));
        randstadLabel.setMinimumSize(new Dimension(-1, -1));
        randstadLabel.setOpaque(false);
        randstadLabel.setPreferredSize(new Dimension(170, 30));
        randstadLabel.setText("randstad.com");
        randstadLabel.setToolTipText("http://www.randstad.com");
        randstadLabel.setVerifyInputWhenFocusTarget(false);
        randstadLabel.putClientProperty("html.disable", Boolean.TRUE);
        randstadLabelPanel.add(randstadLabel);
        workopolisLabelPanel = new JPanel();
        workopolisLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        workopolisLabelPanel.setAlignmentX(0.0f);
        workopolisLabelPanel.setAlignmentY(0.0f);
        workopolisLabelPanel.setAutoscrolls(true);
        workopolisLabelPanel.setBackground(new Color(-721665));
        workopolisLabelPanel.setMaximumSize(new Dimension(210, 30));
        workopolisLabelPanel.setMinimumSize(new Dimension(180, 30));
        workopolisLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(workopolisLabelPanel);
        workopolisLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        workopolisLabel = new JLabel();
        workopolisLabel.setAutoscrolls(false);
        workopolisLabel.setEnabled(true);
        workopolisLabel.setFocusable(false);
        workopolisLabel.setFont(new Font("Times New Roman", workopolisLabel.getFont().getStyle(), 12));
        workopolisLabel.setForeground(new Color(-16777216));
        workopolisLabel.setHorizontalAlignment(2);
        workopolisLabel.setHorizontalTextPosition(2);
        workopolisLabel.setMaximumSize(new Dimension(170, 30));
        workopolisLabel.setMinimumSize(new Dimension(-1, -1));
        workopolisLabel.setOpaque(false);
        workopolisLabel.setPreferredSize(new Dimension(170, 30));
        workopolisLabel.setText("workopolis.com");
        workopolisLabel.setToolTipText("http://www.workopolis.com");
        workopolisLabel.setVerifyInputWhenFocusTarget(false);
        workopolisLabel.putClientProperty("html.disable", Boolean.TRUE);
        workopolisLabelPanel.add(workopolisLabel);
        elutaLabelPanel = new JPanel();
        elutaLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        elutaLabelPanel.setAlignmentX(0.0f);
        elutaLabelPanel.setAlignmentY(0.0f);
        elutaLabelPanel.setAutoscrolls(true);
        elutaLabelPanel.setBackground(new Color(-721665));
        elutaLabelPanel.setMaximumSize(new Dimension(210, 30));
        elutaLabelPanel.setMinimumSize(new Dimension(180, 30));
        elutaLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(elutaLabelPanel);
        elutaLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        elutaLabel = new JLabel();
        elutaLabel.setAutoscrolls(false);
        elutaLabel.setEnabled(true);
        elutaLabel.setFocusable(false);
        elutaLabel.setFont(new Font("Times New Roman", elutaLabel.getFont().getStyle(), 12));
        elutaLabel.setForeground(new Color(-16777216));
        elutaLabel.setHorizontalAlignment(2);
        elutaLabel.setHorizontalTextPosition(2);
        elutaLabel.setMaximumSize(new Dimension(170, 30));
        elutaLabel.setMinimumSize(new Dimension(-1, -1));
        elutaLabel.setOpaque(false);
        elutaLabel.setPreferredSize(new Dimension(170, 30));
        elutaLabel.setText("eluta.ca");
        elutaLabel.setToolTipText("http://www.eluta.ca");
        elutaLabel.setVerifyInputWhenFocusTarget(false);
        elutaLabel.putClientProperty("html.disable", Boolean.TRUE);
        elutaLabelPanel.add(elutaLabel);
        jobspressoLabelPanel = new JPanel();
        jobspressoLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        jobspressoLabelPanel.setAlignmentX(0.0f);
        jobspressoLabelPanel.setAlignmentY(0.0f);
        jobspressoLabelPanel.setAutoscrolls(true);
        jobspressoLabelPanel.setBackground(new Color(-721665));
        jobspressoLabelPanel.setMaximumSize(new Dimension(210, 30));
        jobspressoLabelPanel.setMinimumSize(new Dimension(180, 30));
        jobspressoLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(jobspressoLabelPanel);
        jobspressoLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        jobspressoLabel = new JLabel();
        jobspressoLabel.setAutoscrolls(false);
        jobspressoLabel.setEnabled(true);
        jobspressoLabel.setFocusable(false);
        jobspressoLabel.setFont(new Font("Times New Roman", jobspressoLabel.getFont().getStyle(), 12));
        jobspressoLabel.setForeground(new Color(-16777216));
        jobspressoLabel.setHorizontalAlignment(2);
        jobspressoLabel.setHorizontalTextPosition(2);
        jobspressoLabel.setMaximumSize(new Dimension(170, 30));
        jobspressoLabel.setMinimumSize(new Dimension(-1, -1));
        jobspressoLabel.setOpaque(false);
        jobspressoLabel.setPreferredSize(new Dimension(170, 30));
        jobspressoLabel.setText("jobspresso.co");
        jobspressoLabel.setToolTipText("http://www.jobspresso.co");
        jobspressoLabel.setVerifyInputWhenFocusTarget(false);
        jobspressoLabel.putClientProperty("html.disable", Boolean.TRUE);
        jobspressoLabelPanel.add(jobspressoLabel);
        webbjobbLabelPrser = new JPanel();
        webbjobbLabelPrser.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        webbjobbLabelPrser.setAlignmentX(0.0f);
        webbjobbLabelPrser.setAlignmentY(0.0f);
        webbjobbLabelPrser.setAutoscrolls(true);
        webbjobbLabelPrser.setBackground(new Color(-721665));
        webbjobbLabelPrser.setMaximumSize(new Dimension(210, 30));
        webbjobbLabelPrser.setMinimumSize(new Dimension(180, 30));
        webbjobbLabelPrser.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(webbjobbLabelPrser);
        webbjobbLabelPrser.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        webbjobbLabel = new JLabel();
        webbjobbLabel.setAutoscrolls(false);
        webbjobbLabel.setEnabled(true);
        webbjobbLabel.setFocusable(false);
        webbjobbLabel.setFont(new Font("Times New Roman", webbjobbLabel.getFont().getStyle(), 12));
        webbjobbLabel.setForeground(new Color(-16777216));
        webbjobbLabel.setHorizontalAlignment(2);
        webbjobbLabel.setHorizontalTextPosition(2);
        webbjobbLabel.setMaximumSize(new Dimension(170, 30));
        webbjobbLabel.setMinimumSize(new Dimension(-1, -1));
        webbjobbLabel.setOpaque(false);
        webbjobbLabel.setPreferredSize(new Dimension(170, 30));
        webbjobbLabel.setText("webbjobb.io");
        webbjobbLabel.setToolTipText("http://www.webbjobb.io");
        webbjobbLabel.setVerifyInputWhenFocusTarget(false);
        webbjobbLabel.putClientProperty("html.disable", Boolean.TRUE);
        webbjobbLabelPrser.add(webbjobbLabel);
        jobsSmashingmagazineLabelPanel = new JPanel();
        jobsSmashingmagazineLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        jobsSmashingmagazineLabelPanel.setAlignmentX(0.0f);
        jobsSmashingmagazineLabelPanel.setAlignmentY(0.0f);
        jobsSmashingmagazineLabelPanel.setAutoscrolls(true);
        jobsSmashingmagazineLabelPanel.setBackground(new Color(-721665));
        jobsSmashingmagazineLabelPanel.setMaximumSize(new Dimension(210, 30));
        jobsSmashingmagazineLabelPanel.setMinimumSize(new Dimension(180, 30));
        jobsSmashingmagazineLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(jobsSmashingmagazineLabelPanel);
        jobsSmashingmagazineLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        jobsSmashingmagazineLabel = new JLabel();
        jobsSmashingmagazineLabel.setAutoscrolls(false);
        jobsSmashingmagazineLabel.setEnabled(true);
        jobsSmashingmagazineLabel.setFocusable(false);
        jobsSmashingmagazineLabel.setFont(new Font("Times New Roman", jobsSmashingmagazineLabel.getFont().getStyle(), 12));
        jobsSmashingmagazineLabel.setForeground(new Color(-16777216));
        jobsSmashingmagazineLabel.setHorizontalAlignment(2);
        jobsSmashingmagazineLabel.setHorizontalTextPosition(2);
        jobsSmashingmagazineLabel.setMaximumSize(new Dimension(170, 30));
        jobsSmashingmagazineLabel.setMinimumSize(new Dimension(-1, -1));
        jobsSmashingmagazineLabel.setOpaque(false);
        jobsSmashingmagazineLabel.setPreferredSize(new Dimension(170, 30));
        jobsSmashingmagazineLabel.setText("jobs.smashingmagazine.com");
        jobsSmashingmagazineLabel.setToolTipText("http://jobs.smashingmagazine.com/");
        jobsSmashingmagazineLabel.setVerifyInputWhenFocusTarget(false);
        jobsSmashingmagazineLabel.putClientProperty("html.disable", Boolean.TRUE);
        jobsSmashingmagazineLabelPanel.add(jobsSmashingmagazineLabel);
        themuseLabelPanel = new JPanel();
        themuseLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        themuseLabelPanel.setAlignmentX(0.0f);
        themuseLabelPanel.setAlignmentY(0.0f);
        themuseLabelPanel.setAutoscrolls(true);
        themuseLabelPanel.setBackground(new Color(-721665));
        themuseLabelPanel.setMaximumSize(new Dimension(210, 30));
        themuseLabelPanel.setMinimumSize(new Dimension(180, 30));
        themuseLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(themuseLabelPanel);
        themuseLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        themuseLabel = new JLabel();
        themuseLabel.setAutoscrolls(false);
        themuseLabel.setEnabled(true);
        themuseLabel.setFocusable(false);
        themuseLabel.setFont(new Font("Times New Roman", themuseLabel.getFont().getStyle(), 12));
        themuseLabel.setForeground(new Color(-16777216));
        themuseLabel.setHorizontalAlignment(2);
        themuseLabel.setHorizontalTextPosition(2);
        themuseLabel.setMaximumSize(new Dimension(170, 30));
        themuseLabel.setMinimumSize(new Dimension(-1, -1));
        themuseLabel.setOpaque(false);
        themuseLabel.setPreferredSize(new Dimension(170, 30));
        themuseLabel.setText("themuse.com");
        themuseLabel.setToolTipText("http://www.themuse.com");
        themuseLabel.setVerifyInputWhenFocusTarget(false);
        themuseLabel.putClientProperty("html.disable", Boolean.TRUE);
        themuseLabelPanel.add(themuseLabel);
        techjobsLabelPanel = new JPanel();
        techjobsLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        techjobsLabelPanel.setAlignmentX(0.0f);
        techjobsLabelPanel.setAlignmentY(0.0f);
        techjobsLabelPanel.setAutoscrolls(true);
        techjobsLabelPanel.setBackground(new Color(-721665));
        techjobsLabelPanel.setMaximumSize(new Dimension(210, 30));
        techjobsLabelPanel.setMinimumSize(new Dimension(180, 30));
        techjobsLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(techjobsLabelPanel);
        techjobsLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        techjobsLabel = new JLabel();
        techjobsLabel.setAutoscrolls(false);
        techjobsLabel.setEnabled(true);
        techjobsLabel.setFocusable(false);
        techjobsLabel.setFont(new Font("Times New Roman", techjobsLabel.getFont().getStyle(), 12));
        techjobsLabel.setForeground(new Color(-16777216));
        techjobsLabel.setHorizontalAlignment(2);
        techjobsLabel.setHorizontalTextPosition(2);
        techjobsLabel.setMaximumSize(new Dimension(170, 30));
        techjobsLabel.setMinimumSize(new Dimension(-1, -1));
        techjobsLabel.setOpaque(false);
        techjobsLabel.setPreferredSize(new Dimension(170, 30));
        techjobsLabel.setText("techjobs.com");
        techjobsLabel.setToolTipText("http://www.techjobs.com");
        techjobsLabel.setVerifyInputWhenFocusTarget(false);
        techjobsLabel.putClientProperty("html.disable", Boolean.TRUE);
        techjobsLabelPanel.add(techjobsLabel);
        careerbuilderLabelPanel = new JPanel();
        careerbuilderLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        careerbuilderLabelPanel.setAlignmentX(0.0f);
        careerbuilderLabelPanel.setAlignmentY(0.0f);
        careerbuilderLabelPanel.setAutoscrolls(true);
        careerbuilderLabelPanel.setBackground(new Color(-721665));
        careerbuilderLabelPanel.setMaximumSize(new Dimension(210, 30));
        careerbuilderLabelPanel.setMinimumSize(new Dimension(180, 30));
        careerbuilderLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(careerbuilderLabelPanel);
        careerbuilderLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        careerbuilderLabel = new JLabel();
        careerbuilderLabel.setAutoscrolls(false);
        careerbuilderLabel.setEnabled(true);
        careerbuilderLabel.setFocusable(false);
        careerbuilderLabel.setFont(new Font("Times New Roman", careerbuilderLabel.getFont().getStyle(), 12));
        careerbuilderLabel.setForeground(new Color(-16777216));
        careerbuilderLabel.setHorizontalAlignment(2);
        careerbuilderLabel.setHorizontalTextPosition(2);
        careerbuilderLabel.setMaximumSize(new Dimension(170, 30));
        careerbuilderLabel.setMinimumSize(new Dimension(-1, -1));
        careerbuilderLabel.setOpaque(false);
        careerbuilderLabel.setPreferredSize(new Dimension(170, 30));
        careerbuilderLabel.setText("careerbuilder.com");
        careerbuilderLabel.setToolTipText("http://www.careerbuilder.com");
        careerbuilderLabel.setVerifyInputWhenFocusTarget(false);
        careerbuilderLabel.putClientProperty("html.disable", Boolean.TRUE);
        careerbuilderLabelPanel.add(careerbuilderLabel);
        webentwicklerJobsLabelPanel = new JPanel();
        webentwicklerJobsLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        webentwicklerJobsLabelPanel.setAlignmentX(0.0f);
        webentwicklerJobsLabelPanel.setAlignmentY(0.0f);
        webentwicklerJobsLabelPanel.setAutoscrolls(true);
        webentwicklerJobsLabelPanel.setBackground(new Color(-721665));
        webentwicklerJobsLabelPanel.setMaximumSize(new Dimension(210, 30));
        webentwicklerJobsLabelPanel.setMinimumSize(new Dimension(180, 30));
        webentwicklerJobsLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(webentwicklerJobsLabelPanel);
        webentwicklerJobsLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        webentwicklerJobsLabel = new JLabel();
        webentwicklerJobsLabel.setAutoscrolls(false);
        webentwicklerJobsLabel.setEnabled(true);
        webentwicklerJobsLabel.setFocusable(false);
        webentwicklerJobsLabel.setFont(new Font("Times New Roman", webentwicklerJobsLabel.getFont().getStyle(), 12));
        webentwicklerJobsLabel.setForeground(new Color(-16777216));
        webentwicklerJobsLabel.setHorizontalAlignment(2);
        webentwicklerJobsLabel.setHorizontalTextPosition(2);
        webentwicklerJobsLabel.setMaximumSize(new Dimension(170, 30));
        webentwicklerJobsLabel.setMinimumSize(new Dimension(-1, -1));
        webentwicklerJobsLabel.setOpaque(false);
        webentwicklerJobsLabel.setPreferredSize(new Dimension(170, 30));
        webentwicklerJobsLabel.setText("webentwickler-jobs.de");
        webentwicklerJobsLabel.setToolTipText("http://www.webentwickler-jobs.de");
        webentwicklerJobsLabel.setVerifyInputWhenFocusTarget(false);
        webentwicklerJobsLabel.putClientProperty("html.disable", Boolean.TRUE);
        webentwicklerJobsLabelPanel.add(webentwicklerJobsLabel);
        uberjobsLabelPanel = new JPanel();
        uberjobsLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        uberjobsLabelPanel.setAlignmentX(0.0f);
        uberjobsLabelPanel.setAlignmentY(0.0f);
        uberjobsLabelPanel.setAutoscrolls(true);
        uberjobsLabelPanel.setBackground(new Color(-721665));
        uberjobsLabelPanel.setMaximumSize(new Dimension(210, 30));
        uberjobsLabelPanel.setMinimumSize(new Dimension(180, 30));
        uberjobsLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(uberjobsLabelPanel);
        uberjobsLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        uberjobsLabel = new JLabel();
        uberjobsLabel.setAutoscrolls(false);
        uberjobsLabel.setEnabled(true);
        uberjobsLabel.setFocusable(false);
        uberjobsLabel.setFont(new Font("Times New Roman", uberjobsLabel.getFont().getStyle(), 12));
        uberjobsLabel.setForeground(new Color(-16777216));
        uberjobsLabel.setHorizontalAlignment(2);
        uberjobsLabel.setHorizontalTextPosition(2);
        uberjobsLabel.setMaximumSize(new Dimension(170, 30));
        uberjobsLabel.setMinimumSize(new Dimension(-1, -1));
        uberjobsLabel.setOpaque(false);
        uberjobsLabel.setPreferredSize(new Dimension(170, 30));
        uberjobsLabel.setText("uberjobs.de");
        uberjobsLabel.setToolTipText("http://uberjobs.de");
        uberjobsLabel.setVerifyInputWhenFocusTarget(false);
        uberjobsLabel.putClientProperty("html.disable", Boolean.TRUE);
        uberjobsLabelPanel.add(uberjobsLabel);
        guruLabelPanel = new JPanel();
        guruLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        guruLabelPanel.setAlignmentX(0.0f);
        guruLabelPanel.setAlignmentY(0.0f);
        guruLabelPanel.setAutoscrolls(true);
        guruLabelPanel.setBackground(new Color(-721665));
        guruLabelPanel.setMaximumSize(new Dimension(210, 30));
        guruLabelPanel.setMinimumSize(new Dimension(180, 30));
        guruLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(guruLabelPanel);
        guruLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        guruLabel = new JLabel();
        guruLabel.setAutoscrolls(false);
        guruLabel.setEnabled(true);
        guruLabel.setFocusable(false);
        guruLabel.setFont(new Font("Times New Roman", guruLabel.getFont().getStyle(), 12));
        guruLabel.setForeground(new Color(-16777216));
        guruLabel.setHorizontalAlignment(2);
        guruLabel.setHorizontalTextPosition(2);
        guruLabel.setMaximumSize(new Dimension(170, 30));
        guruLabel.setMinimumSize(new Dimension(-1, -1));
        guruLabel.setOpaque(false);
        guruLabel.setPreferredSize(new Dimension(170, 30));
        guruLabel.setText("guru.com");
        guruLabel.setToolTipText("http://www.guru.com");
        guruLabel.setVerifyInputWhenFocusTarget(false);
        guruLabel.putClientProperty("html.disable", Boolean.TRUE);
        guruLabelPanel.add(guruLabel);
        authenticjobsLabelParser = new JPanel();
        authenticjobsLabelParser.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        authenticjobsLabelParser.setAlignmentX(0.0f);
        authenticjobsLabelParser.setAlignmentY(0.0f);
        authenticjobsLabelParser.setAutoscrolls(true);
        authenticjobsLabelParser.setBackground(new Color(-721665));
        authenticjobsLabelParser.setMaximumSize(new Dimension(210, 30));
        authenticjobsLabelParser.setMinimumSize(new Dimension(180, 30));
        authenticjobsLabelParser.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(authenticjobsLabelParser);
        authenticjobsLabelParser.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        authenticjobsLabel = new JLabel();
        authenticjobsLabel.setAutoscrolls(false);
        authenticjobsLabel.setEnabled(true);
        authenticjobsLabel.setFocusable(false);
        authenticjobsLabel.setFont(new Font("Times New Roman", authenticjobsLabel.getFont().getStyle(), 12));
        authenticjobsLabel.setForeground(new Color(-16777216));
        authenticjobsLabel.setHorizontalAlignment(2);
        authenticjobsLabel.setHorizontalTextPosition(2);
        authenticjobsLabel.setMaximumSize(new Dimension(170, 30));
        authenticjobsLabel.setMinimumSize(new Dimension(-1, -1));
        authenticjobsLabel.setOpaque(false);
        authenticjobsLabel.setPreferredSize(new Dimension(170, 30));
        authenticjobsLabel.setText("authenticjobs.com");
        authenticjobsLabel.setToolTipText("http://authenticjobs.com");
        authenticjobsLabel.setVerifyInputWhenFocusTarget(false);
        authenticjobsLabel.putClientProperty("html.disable", Boolean.TRUE);
        authenticjobsLabelParser.add(authenticjobsLabel);
        eurojobsLabelPanel = new JPanel();
        eurojobsLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        eurojobsLabelPanel.setAlignmentX(0.0f);
        eurojobsLabelPanel.setAlignmentY(0.0f);
        eurojobsLabelPanel.setAutoscrolls(true);
        eurojobsLabelPanel.setBackground(new Color(-721665));
        eurojobsLabelPanel.setMaximumSize(new Dimension(210, 30));
        eurojobsLabelPanel.setMinimumSize(new Dimension(180, 30));
        eurojobsLabelPanel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(eurojobsLabelPanel);
        eurojobsLabelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        eurojobsLabel = new JLabel();
        eurojobsLabel.setAutoscrolls(false);
        eurojobsLabel.setEnabled(true);
        eurojobsLabel.setFocusable(false);
        eurojobsLabel.setFont(new Font("Times New Roman", eurojobsLabel.getFont().getStyle(), 12));
        eurojobsLabel.setForeground(new Color(-16777216));
        eurojobsLabel.setHorizontalAlignment(2);
        eurojobsLabel.setHorizontalTextPosition(2);
        eurojobsLabel.setMaximumSize(new Dimension(170, 30));
        eurojobsLabel.setMinimumSize(new Dimension(-1, -1));
        eurojobsLabel.setOpaque(false);
        eurojobsLabel.setPreferredSize(new Dimension(170, 30));
        eurojobsLabel.setText("eurojobs.com");
        eurojobsLabel.setToolTipText("http://www.eurojobs.com");
        eurojobsLabel.setVerifyInputWhenFocusTarget(false);
        eurojobsLabel.putClientProperty("html.disable", Boolean.TRUE);
        eurojobsLabelPanel.add(eurojobsLabel);
        jobPanel = new JPanel();
        jobPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        jobPanel.setAutoscrolls(false);
        jobPanel.setBackground(new Color(-4473925));
        jobPanel.setEnabled(false);
        jobPanel.setFocusCycleRoot(true);
        jobPanel.setFocusTraversalPolicyProvider(true);
        jobPanel.setFocusable(true);
        jobPanel.setFont(new Font("Times New Roman", jobPanel.getFont().getStyle(), 12));
        jobPanel.setInheritsPopupMenu(true);
        jobPanel.setVisible(true);
        panelMain.add(jobPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(600, 500), new Dimension(600, 780), new Dimension(600, 780), 0, false));
        jobPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Jobs", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, new Font("DialogInput", Font.BOLD, 18), new Color(-16777216)));
        jobLabel = new JLabel();
        jobLabel.setAutoscrolls(false);
        jobLabel.setEnabled(true);
        jobLabel.setFocusable(false);
        jobLabel.setFont(new Font("Times New Roman", jobLabel.getFont().getStyle(), 12));
        jobLabel.setForeground(new Color(-16777216));
        jobLabel.setHorizontalAlignment(0);
        jobLabel.setHorizontalTextPosition(2);
        jobLabel.setMaximumSize(new Dimension(300, 15));
        jobLabel.setMinimumSize(new Dimension(300, 15));
        jobLabel.setOpaque(false);
        jobLabel.setPreferredSize(new Dimension(300, 15));
        jobLabel.setText("Start text1");
        jobLabel.setToolTipText("start TEXT");
        jobPanel.add(jobLabel);
        descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        descriptionPanel.setAutoscrolls(false);
        descriptionPanel.setBackground(new Color(-4473925));
        descriptionPanel.setEnabled(false);
        descriptionPanel.setFocusable(false);
        descriptionPanel.setFont(new Font("Times New Roman", descriptionPanel.getFont().getStyle(), 12));
        descriptionPanel.setVisible(false);
        panelMain.add(descriptionPanel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(400, 500), new Dimension(300, -1), null, 0, false));
        descriptionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Description", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, new Font("DialogInput", Font.BOLD, 18), new Color(-16777216)));
        descriptionLabel = new JLabel();
        descriptionLabel.setAutoscrolls(false);
        descriptionLabel.setEnabled(true);
        descriptionLabel.setFocusable(false);
        descriptionLabel.setFont(new Font("Times New Roman", descriptionLabel.getFont().getStyle(), 12));
        descriptionLabel.setForeground(new Color(-16777216));
        descriptionLabel.setHorizontalAlignment(0);
        descriptionLabel.setHorizontalTextPosition(2);
        descriptionLabel.setMaximumSize(new Dimension(300, 15));
        descriptionLabel.setMinimumSize(new Dimension(300, 15));
        descriptionLabel.setOpaque(false);
        descriptionLabel.setPreferredSize(new Dimension(300, 15));
        descriptionLabel.setText("Start text2");
        descriptionLabel.setToolTipText("start TEXT");
        descriptionPanel.add(descriptionLabel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

    private class Task implements Runnable {
        private String homeLink;
        private ParserMain classParser;
        private JPanel labelPanel;

        public Task(JPanel labelPanel, ParserMain classParser, String homeLink) {
            this.classParser = classParser;
            this.labelPanel = labelPanel;
            this.homeLink = homeLink;
        }

        @Override
        public void run() {

            List<JobsInform> jobsInforms = classParser.startParse();

            executorDB.execute(new TaskDB(labelPanel, jobsInforms, homeLink));

        }
    }

    private class TaskDB implements Runnable {

        private String homeLink;
        private JPanel labelPanel;
        private List<JobsInform> jobsInforms;

        public TaskDB(JPanel labelPanel, List<JobsInform> jobsInforms, String homeLink) {
            this.homeLink = homeLink;
            this.labelPanel = labelPanel;
            this.jobsInforms = jobsInforms;

        }

        @Override
        public void run() {
            int newResults = new DbHelper().writeDB(homeLink, jobsInforms);
            labelPanel.setVisible(false);
            labelPanel.setBackground(new Color(-721665));
            JLabel label = (JLabel) labelPanel.getComponent(0);
            String text = label.getText();
            label.setText(text + " new " + newResults);
            label.setForeground(new Color(0x5F0200));
            labelPanel.setVisible(true);
        }
    }

    private class TaskDBWriteSeen implements Runnable {

        private String homeLink;
        private List<JobsInform> jobsInforms;

        public TaskDBWriteSeen(List<JobsInform> jobsInforms, String homeLink) {
            this.homeLink = homeLink;
            this.jobsInforms = jobsInforms;

        }

        @Override
        public void run() {
            new DbHelper().writeDB(homeLink, jobsInforms);
        }
    }

    private class TaskReadDb implements Callable<List<JobsInform>> {

        private String siteName;

        public TaskReadDb(String siteName) {
            this.siteName = siteName;

        }

        public List<JobsInform> call() {
            return new DbHelper().getJobsInformFromDb(siteName);
        }
    }

    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) { /* TODO: error handling */ }
        } else { /* TODO: error handling */ }
    }


}

