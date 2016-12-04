package com.parser;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;


public class Main extends Application {
    private JFrame jFrame;
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        jFrame = new JFrame("ParserApp");
        jFrame.getContentPane()
                .add(new ParserApp().getPanelMain());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);

    }

}
