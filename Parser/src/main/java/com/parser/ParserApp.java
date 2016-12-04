package com.parser;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class ParserApp {

    private JPanel panelMain;
    private JPanel descriptionPanel;
    private JPanel linkPanel;
    private JPanel jobPanel;
    private JLabel linkLabel;
    private JLabel jobLabel;
    private JLabel descriptionLabel;
    private JPanel label1Panel;

    public JPanel getPanelMain() {
        return panelMain;
    }

    public ParserApp() {
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                jobPanel.removeAll();
                JPanel mainJobPanel = new JPanel();
                for (int i = 0; i < 40; i++) {
                    JPanel label1Panel = new JPanel();
                    JLabel label = new JLabel("job " + i);
                    label.setName("job " + i);
                    label1Panel.setVisible(true);
                    label1Panel.setName("job" + i);
                    label1Panel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            e.getComponent().setVisible(false);

                            final String text = "Attributes, Styles and Style Contexts\n"
                                    + "The simple PlainDocument class that you saw in the previous "
                                    + "chapter is only capable of holding text. The more complex text "
                                    + "components use a more sophisticated model that implements the "
                                    + "StyledDocument interface. StyledDocument is a sub-interface of "
                                    + "Document that contains methods for manipulating attributes that "
                                    + "control the way in which the text in the document is displayed. "
                                    + "The Swing text package contains a concrete implementation of "
                                    + "StyledDocument called DefaultStyledDocument that is used as the "
                                    + "default model for JTextPane and is also the base class from which "
                                    + "more specific models, such as the HTMLDocument class that handles "
                                    + "input in HTML format, can be created. In order to make use of "
                                    + "DefaultStyledDocument and JTextPane, you need to understand how "
                                    + "The simple PlainDocument class that you saw in the previous "
                                    + "chapter is only capable of holding text. The more complex text "
                                    + "components use a more sophisticated model that implements the "
                                    + "StyledDocument interface. StyledDocument is a sub-interface of "
                                    + "Document that contains methods for manipulating attributes that "
                                    + "control the way in which the text in the document is displayed. "
                                    + "The Swing text package contains a concrete implementation of "
                                    + "StyledDocument called DefaultStyledDocument that is used as the "
                                    + "default model for JTextPane and is also the base class from which "
                                    + "more specific models, such as the HTMLDocument class that handles "
                                    + "input in HTML format, can be created. In order to make use of "
                                    + "DefaultStyledDocument and JTextPane, you need to understand how "
                                    + "The simple PlainDocument class that you saw in the previous "
                                    + "chapter is only capable of holding text. The more complex text "
                                    + "components use a more sophisticated model that implements the "
                                    + "StyledDocument interface. StyledDocument is a sub-interface of "
                                    + "Document that contains methods for manipulating attributes that "
                                    + "control the way in which the text in the document is displayed. "
                                    + "The Swing text package contains a concrete implementation of "
                                    + "StyledDocument called DefaultStyledDocument that is used as the "
                                    + "default model for JTextPane and is also the base class from which "
                                    + "more specific models, such as the HTMLDocument class that handles "
                                    + "input in HTML format, can be created. In order to make use of "
                                    + "DefaultStyledDocument and JTextPane, you need to understand how "
                                    + "The simple PlainDocument class that you saw in the previous "
                                    + "chapter is only capable of holding text. The more complex text "
                                    + "components use a more sophisticated model that implements the "
                                    + "StyledDocument interface. StyledDocument is a sub-interface of "
                                    + "Document that contains methods for manipulating attributes that "
                                    + "control the way in which the text in the document is displayed. "
                                    + "The Swing text package contains a concrete implementation of "
                                    + "StyledDocument called DefaultStyledDocument that is used as the "
                                    + "default model for JTextPane and is also the base class from which "
                                    + "more specific models, such as the HTMLDocument class that handles "
                                    + "input in HTML format, can be created. In order to make use of "
                                    + "DefaultStyledDocument and JTextPane, you need to understand how "
                                    + "Swing represents and uses attributes.\n";


                            descriptionPanel.removeAll();
                            descriptionPanel.setVisible(false);
                            StyleContext sc = new StyleContext();
                            final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
                            JTextPane pane = new JTextPane(doc);

                            // Create and add the style
                            final Style heading2Style = sc.addStyle("Heading2", null);
                            heading2Style.addAttribute(StyleConstants.Foreground, Color.red);
                            heading2Style.addAttribute(StyleConstants.FontSize, new

                                    Integer(16));
                            heading2Style.addAttribute(StyleConstants.FontFamily, "serif");
                            heading2Style.addAttribute(StyleConstants.Bold, new

                                    Boolean(true));

                            try {
                                // Add the text to the document
                                doc.insertString(0, text, null);

                                // Finally, apply the style to the heading
                                doc.setParagraphAttributes(0, 1, heading2Style, false);
                            } catch (BadLocationException e1) {
                            }

//                            JScrollPane pane1 = new JScrollPane(pane,
//                                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
//                                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//                            pane1.setAutoscrolls(true);
//                            pane1.setBackground(new
//
//                                    Color(-721665));
//                            pane1.setAlignmentX(0.0f);
//                            pane1.setAlignmentY(0.0f);
//                            pane1.setMaximumSize(new
//
//                                    Dimension(280, 680));
//                            pane1.setMinimumSize(descriptionPanel.getMinimumSize());
//                            pane1.setPreferredSize(new
//
//                                    Dimension(280, 680));
//
//
//                            descriptionPanel.add(pane1);


//                            JPanel label1Panel = new JPanel();
//                            label1Panel.add(new JTextPane()))"));
//                            label1Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//                            label1Panel.setAlignmentX(0.0f);
//                            label1Panel.setAlignmentY(0.0f);
//                            label1Panel.setAutoscrolls(true);
//                            label1Panel.setBackground(new Color(-721665));
//                            JScrollPane pane = new JScrollPane(label1Panel,
//                                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
//                                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//                            pane.setAutoscrolls(true);
//                            pane.setBackground(new Color(-721665));
//                            pane.setAlignmentX(0.0f);
//                            pane.setAlignmentY(0.0f);
//                            pane.setMaximumSize(new Dimension(280, 680));
//                            pane.setMinimumSize(new Dimension(280, 680));
//                            pane.setPreferredSize(new Dimension(280, 680));
                            JFrame jFrame = new JFrame();
                            jFrame.setSize(500, 700);
                            jFrame.add(new JScrollPane(pane));

                            jFrame.setLocationRelativeTo(null);
                            jFrame.setVisible(true);
//                            JScrollPane scroll = new JScrollPane(jFrame, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
//                                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//                            scroll.setAutoscrolls(true);
//                            scroll.setSize(200, 500);
//                            descriptionPanel.add(scroll);
//
////                            label1Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
//                            descriptionPanel.setVisible(true);
                        }
                    });
                    label1Panel.add(label);
                    label1Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    label1Panel.setAlignmentX(0.0f);
                    label1Panel.setAlignmentY(0.0f);
                    label1Panel.setAutoscrolls(true);
                    label1Panel.setBackground(new Color(-721665));
                    label1Panel.setMaximumSize(new Dimension(410, 30));
                    label1Panel.setMinimumSize(new Dimension(280, 30));
                    label1Panel.setPreferredSize(new Dimension(280, 30));
                    mainJobPanel.add(label1Panel);
                    label1Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));

                }
                mainJobPanel.setMinimumSize(new

                        Dimension(280, 35 * 40));
                mainJobPanel.setPreferredSize(new

                        Dimension(280, 35 * 40));
                mainJobPanel.setMaximumSize(new

                        Dimension(410, 35 * 40));
                JScrollPane pane = new JScrollPane(mainJobPanel,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                pane.setAutoscrolls(true);
                pane.setBackground(new

                        Color(-721665));
                pane.setAlignmentX(0.0f);
                pane.setAlignmentY(0.0f);
                pane.setMaximumSize(new

                        Dimension(280, 680));
                pane.setMinimumSize(mainJobPanel.getMinimumSize());
                pane.setPreferredSize(new

                        Dimension(280, 680));

                jobPanel.add(pane);
                jobPanel.setVisible(true);
            }

        });
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
        panelMain.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.setAutoscrolls(false);
        panelMain.setBackground(new Color(-7631989));
        panelMain.setEnabled(true);
        panelMain.setFocusable(false);
        panelMain.setInheritsPopupMenu(false);
        panelMain.setMaximumSize(new Dimension(1600, 1000));
        panelMain.setMinimumSize(new Dimension(732, 632));
        panelMain.setPreferredSize(new Dimension(732, 632));
        panelMain.setVisible(true);
        panelMain.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "ParserApp", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font(panelMain.getFont().getName(), panelMain.getFont().getStyle(), 16), new Color(-16777216)));
        linkPanel = new JPanel();
        linkPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        linkPanel.setAutoscrolls(false);
        linkPanel.setBackground(new Color(-4473925));
        linkPanel.setEnabled(true);
        linkPanel.setFocusable(false);
        linkPanel.setFont(new Font("Times New Roman", linkPanel.getFont().getStyle(), 12));
        panelMain.add(linkPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(200, 500), new Dimension(187, 800), new Dimension(200, -1), 0, false));
        linkPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Links", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, new Font("DialogInput", Font.BOLD, 18), new Color(-16777216)));
        label1Panel = new JPanel();
        label1Panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        label1Panel.setAlignmentX(0.0f);
        label1Panel.setAlignmentY(0.0f);
        label1Panel.setAutoscrolls(true);
        label1Panel.setBackground(new Color(-721665));
        label1Panel.setMaximumSize(new Dimension(210, 30));
        label1Panel.setMinimumSize(new Dimension(180, 30));
        label1Panel.setPreferredSize(new Dimension(180, 30));
        linkPanel.add(label1Panel);
        label1Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, null, new Color(-16777216)));
        linkLabel = new JLabel();
        linkLabel.setAutoscrolls(false);
        linkLabel.setEnabled(true);
        linkLabel.setFocusable(false);
        linkLabel.setFont(new Font("Times New Roman", linkLabel.getFont().getStyle(), 12));
        linkLabel.setForeground(new Color(-16777216));
        linkLabel.setHorizontalAlignment(2);
        linkLabel.setHorizontalTextPosition(2);
        linkLabel.setMaximumSize(new Dimension(170, 30));
        linkLabel.setMinimumSize(new Dimension(-1, -1));
        linkLabel.setOpaque(false);
        linkLabel.setPreferredSize(new Dimension(170, 30));
        linkLabel.setText("Start text  ");
        linkLabel.setToolTipText("start TEXT");
        linkLabel.setVerifyInputWhenFocusTarget(false);
        linkLabel.putClientProperty("html.disable", Boolean.TRUE);
        label1Panel.add(linkLabel);
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
        jobPanel.setVisible(false);
        panelMain.add(jobPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(200, 500), new Dimension(300, 800), new Dimension(400, 1000), 0, false));
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
        panelMain.add(descriptionPanel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(200, 500), new Dimension(300, 800), null, 0, false));
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

