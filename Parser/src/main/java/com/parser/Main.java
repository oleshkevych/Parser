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
                try {
                    parserApp.runParser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
//        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);

    }

    private void stopApp() {
        System.out.println("Stop");
        jFrame.setVisible(false);
        if ((executorForStart != null)) {
            executorForStart.shutdown();
        }
        try {
            if (parserApp.getExecutor() != null && !parserApp.getExecutor().isShutdown()) {
                parserApp.getExecutor().getQueue().clear();
            }
        } catch (NullPointerException n) {
            System.out.println("parserApp.getExecutor()");
            n.printStackTrace();
        }
        try {
            if (parserApp.getExecutorDB() != null && !parserApp.getExecutorDB().isShutdown()) {
                parserApp.getExecutorDB().getQueue().clear();
            }
        } catch (NullPointerException n) {
            System.out.println("parserApp.getExecutor()");
            n.printStackTrace();
        }
        closeApp();
    }
    private void closeApp(){
        if(parserApp.getExecutor().getActiveCount() == 0 && parserApp.getExecutorDB().getActiveCount() == 0){
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            try {
                this.stop();
            }catch (Exception e){
                System.out.println("this.stop();");

                e.printStackTrace();
            }
        }else{
            try {
                Thread.sleep(1000);
                System.out.println("I'ms leepping ");
                closeApp();
            }catch (Exception e){
                System.out.println("Thread.sleep(1000);");

                e.printStackTrace();
            }
        }
    }

}
