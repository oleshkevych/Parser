package com.parser;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.parser.cc.startus.ParserStartus;
import com.parser.io.remoteok.ParserRemoteok;
import com.parser.io.wfh.ParserWFH;
import com.parser.jobs.landing.ParserLandingJobs;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ParserApp {

    private JPanel panelMain;
    private JPanel descriptionPanel;
    private JPanel linkPanel;
    private JPanel jobPanel;
    private JLabel wfhLink;
    private JLabel jobLabel;
    private JLabel descriptionLabel;
    private JPanel wfhLabelPanel;
    private JPanel remoteokLabelPanel;
    private JLabel remoteok;
    private JPanel landingJobsPanel;
    private JLabel landingJobsLabel;
    private JPanel startusLabelPanel;
    private JLabel startusLabel;
    private JFrame jFrame = new JFrame();


    public JPanel getPanelMain() {
        return panelMain;
    }

    public ParserApp() {
        wfhLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("text speciality :111 ");
                jobPanel.removeAll();
                panelFiller(new ParserWFH().getJobsInforms());
//                super.mouseClicked(e);

                System.out.println("text speciality :111 ");

            }
        });
        remoteok.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("text speciality :222 ");
                jobPanel.removeAll();
                panelFiller(new ParserRemoteok().getJobsInforms());
//                super.mouseClicked(e);
                System.out.println("text speciality :222 ");
            }
        });
        landingJobsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("text speciality :333 ");
                jobPanel.removeAll();
                panelFiller(new ParserLandingJobs().getJobsInforms());
//                super.mouseClicked(e);
                System.out.println("text speciality :333 ");
            }
        });
        startusLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("text speciality :333 ");
                jobPanel.removeAll();
                panelFiller(new ParserStartus().getJobsInforms());
//                super.mouseClicked(e);
                System.out.println("text speciality :333 ");
            }
        });
    }

    private void panelFiller(final List<JobsInform> jobsInformList) {

        jobPanel.setVisible(false);
        JPanel mainJobPanel = new JPanel();
        for (int i = 0; i < jobsInformList.size(); i++) {
            JobsInform ji = jobsInformList.get(i);
            JPanel label1Panel = new JPanel();
            String stringDate = new SimpleDateFormat("dd-MM-yyyy").format(ji.getPublishedDate());
            JLabel label1 = new JLabel(ji.getHeadPublication() + "      PLACE: " + ji.getPlace());
            label1.setMaximumSize(new Dimension(540, 20));
            label1.setMinimumSize(new Dimension(540, 20));
            label1.setPreferredSize(new Dimension(540, 20));
            JLabel label2 = new JLabel("COMPANY: " + ji.getCompanyName() + "     " + stringDate);
            label1.setHorizontalAlignment(SwingConstants.LEFT);
            label2.setHorizontalAlignment(SwingConstants.LEFT);

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
                        container.add(new JLabel(new SimpleDateFormat("dd-MM-yyyy").format(jobsInformList.get(Integer.parseInt(e.getComponent().getName())).getPublishedDate())));
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

                                do {
                                    j++;
                                    if (order.get(j) != null) {

                                        if (order.get(j).getListHeader() != null) {
                                            text += /*"<FONT size=\"15\" color=\"#000099\"><U>Run to the source site</U></FONT>"*/order.get(j).getListHeader() + "\n";
                                        } else if (order.get(j).getListItem() != null) {
//                                                text += "<UL>";
                                            for (String s : order.get(j).getListItem()) {
                                                text += /*"    <LI> " +*/ "  --  " + s +  /*</LI> */"\n";
                                            }
//                                                text += "</UL>";

                                        } else {
                                            text += "  " + order.get(j).getTextFieldImpl() + "\n";
                                        }
                                    }
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
            label1Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            label1Panel.setAlignmentX(0.0f);
            label1Panel.setAlignmentY(0.0f);
            label1Panel.setAutoscrolls(true);
            label1Panel.setBackground(new Color(-721665));
            label1Panel.setMaximumSize(new Dimension(540, 40));
            label1Panel.setMinimumSize(new Dimension(540, 40));
            label1Panel.setPreferredSize(new Dimension(540, 40));
            mainJobPanel.add(label1Panel);
            label1Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));

        }
        mainJobPanel.setMinimumSize(new

                Dimension(580, 45 * jobsInformList.size()));
        mainJobPanel.setPreferredSize(new

                Dimension(580, 45 * jobsInformList.size()));
        mainJobPanel.setMaximumSize(new

                Dimension(510, 45 * jobsInformList.size()));
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

    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) { /* TODO: error handling */ }
        } else { /* TODO: error handling */ }
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
        panelMain.setMinimumSize(new Dimension(872, 832));
        panelMain.setPreferredSize(new Dimension(872, 832));
        panelMain.setVisible(true);
        panelMain.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "ParserApp", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font(panelMain.getFont().getName(), panelMain.getFont().getStyle(), 16), new Color(-16777216)));
        linkPanel = new JPanel();
        linkPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        linkPanel.setAutoscrolls(false);
        linkPanel.setBackground(new Color(-4473925));
        linkPanel.setEnabled(true);
        linkPanel.setFocusable(false);
        linkPanel.setFont(new Font("Times New Roman", linkPanel.getFont().getStyle(), 12));
        panelMain.add(linkPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(220, 500), new Dimension(220, 780), new Dimension(220, 780), 2, false));
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
        remoteok = new JLabel();
        remoteok.setAutoscrolls(false);
        remoteok.setEnabled(true);
        remoteok.setFocusable(false);
        remoteok.setFont(new Font("Times New Roman", remoteok.getFont().getStyle(), 12));
        remoteok.setForeground(new Color(-16777216));
        remoteok.setHorizontalAlignment(2);
        remoteok.setHorizontalTextPosition(2);
        remoteok.setMaximumSize(new Dimension(170, 30));
        remoteok.setMinimumSize(new Dimension(-1, -1));
        remoteok.setOpaque(false);
        remoteok.setPreferredSize(new Dimension(170, 30));
        remoteok.setText("remoteok");
        remoteok.setToolTipText("www.remoteok.io");
        remoteok.setVerifyInputWhenFocusTarget(false);
        remoteok.putClientProperty("html.disable", Boolean.TRUE);
        remoteokLabelPanel.add(remoteok);
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
}

