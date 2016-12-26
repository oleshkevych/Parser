package com.parser;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main extends Application {
    private JFrame jFrame;
    private ParserApp parserApp;
    private ExecutorService executorForStart = Executors.newFixedThreadPool(3);


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        jFrame = new JFrame("ParserApp");
        parserApp = new ParserApp();
        executorForStart.execute(new Runnable() {
            @Override
            public void run() {
                parserApp.runParser();
            }
        });
        jFrame.getContentPane().add(parserApp.getPanelMain());
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                stopApp();
            }
        };
        jFrame.addWindowListener(exitListener);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);

    }

    private void stopApp() {
        System.out.println("Stop");
        if (!executorForStart.isShutdown()) {
            executorForStart.shutdown();
        }
        try {
            if (!parserApp.getExecutor().isShutdown()) {
                parserApp.getExecutor().shutdown();
            }
        } catch (NullPointerException n) {
            System.out.println("parserApp.getExecutor()");
            n.printStackTrace();
        }
        try {
            if (!parserApp.getExecutorDB().isShutdown()) {
                parserApp.getExecutorDB().shutdown();
            }
        } catch (NullPointerException n) {
            System.out.println("parserApp.getExecutor()");
            n.printStackTrace();
        }
    }

}
