package com.parser.dbmanager;

import com.parser.entity.LabelSort;
import com.parser.entity.ParserMain;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by rolique_pc on 1/5/2017.
 */
public class DbHelperSort {


    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    // Database table names
    private final static String PRIORITY = "PriorityNumber";

    // Database column names
    private final static String ID = "Id";
    private final static String STRING_NAME = "StringName";
    private final static String PRIORITY_INDICATOR = "PriorityIndicator";


    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;
    private List<String> listParsers = new ArrayList<>();
    private Map<String, ParserMain> map;

    public DbHelperSort() {

//        listParsers.add("wfh.io");
//        listParsers.add("remoteok.io");
//        listParsers.add("landing.jobs");
//        listParsers.add("startus.cc");
//        listParsers.add("virtualvocations.com");
//        listParsers.add("simplyhired.com");
//        listParsers.add("stackoverflow.com");
//        listParsers.add("juju.com");
//        listParsers.add("jobs.drupal.org");
//        listParsers.add("dutchstartupjobs.com");
//        listParsers.add("monster.de");
//        listParsers.add("weloveangular.com");
//        listParsers.add("weworkremotely.com");
//        listParsers.add("startupjobs.se");
//        listParsers.add("berlinstartupjobs.com");
//        listParsers.add("flexjobs.com");
//        listParsers.add("builtinnode.com");
//        listParsers.add("weworkmeteor.com");
//        listParsers.add("jobbank.dk");
//        listParsers.add("workingnomads.co");
//        listParsers.add("remote.co");
//        listParsers.add("randstad.com");
//        listParsers.add("workopolis.com");
//        listParsers.add("eluta.ca");
//        listParsers.add("webbjobb.io");
//        listParsers.add("jobspresso.co");
//        listParsers.add("jobs.ch");
//        listParsers.add("jobs.smashingmagazine.com");
//        listParsers.add("themuse.com");
//        listParsers.add("techjobs.com");
//        listParsers.add("careerbuilder.com");
//        listParsers.add("webentwickler-jobs.de");
//        listParsers.add("uberjobs.de");
//        listParsers.add("guru.com");
//        listParsers.add("authenticjobs.com");
//        listParsers.add("eurojobs.com");
//        listParsers.add("technojobs.co.uk");
//        listParsers.add("learn4good.com");
//        listParsers.add("canadajobs.com");
//        listParsers.add("drupal.org.uk");
//        listParsers.add("ziprecruiter.com");
//        listParsers.add("drupalcenter.de");
//        listParsers.add("indeed.com");
//        listParsers.add("wowjobs.ca");
//        listParsers.add("builtinaustin.com");
//        listParsers.add("betalist.com");
//        listParsers.add("unicornhunt.io");
//        listParsers.add("jobs.justlanded.com");
//        listParsers.add("jobs.remotive.io");


    }

    public Connection connect(boolean fillNewTable) {
        try {
            Class.forName("org.sqlite.JDBC");
            String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);

            try {
                path = path.substring(0, path.lastIndexOf("/"));
            } catch (Exception e) {
//                e.printStackTrace();
            }
            new File("lib").mkdir();
            if (fillNewTable) {
                connection = DriverManager.getConnection("jdbc:sqlite:" + "lib/" + "PriorityDB.s3db");
            } else {
                if (!new File("lib/" + "PriorityDB.s3db").exists()) {
                    fillPriorityTable();
                }
                connection = DriverManager.getConnection("jdbc:sqlite:" + "lib/" + "PriorityDB.s3db");
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void open(boolean fillNewTable) {
        try {
            statement = connect(fillNewTable).createStatement();
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        try {
            statement.execute(CREATE_TABLE + "'" + PRIORITY + "' (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    STRING_NAME + " STRING, " +
                    PRIORITY_INDICATOR + " INTEGER);");
            statement.close();
            System.out.println("created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillPriorityTable() {
        open(true);
        Collections.sort(listParsers);

        for (String s : listParsers) {
            try {
                preparedStatement = connection
                        .prepareStatement("INSERT INTO '" + PRIORITY + "' ('" + STRING_NAME + "', '" + PRIORITY_INDICATOR + "') VALUES ( ?, ?); ",
                                Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, s);
                preparedStatement.setInt(2, 0);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("    FAIL: DateSET");
                e.printStackTrace();
            }
        }
        try {
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPriority(String stringName, boolean isTop) {
        open(false);
        try {
            if (isTop) {
                preparedStatement = connection
                        .prepareStatement("UPDATE " + PRIORITY + " SET " + PRIORITY_INDICATOR + "=" + PRIORITY_INDICATOR + "+ 1" + " WHERE " + STRING_NAME + "='" + stringName + "';",
                                Statement.RETURN_GENERATED_KEYS);
            } else {
                preparedStatement = connection
                        .prepareStatement("UPDATE " + PRIORITY + " SET " + PRIORITY_INDICATOR + "=" + PRIORITY_INDICATOR + "- 1" + " WHERE " + STRING_NAME + "='" + stringName + "';",
                                Statement.RETURN_GENERATED_KEYS);
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("    FAIL: DateSET");
            e.printStackTrace();
        }
    }

    public List<LabelSort> getLabelSortList(List<String> listParsers) {
        this.listParsers = listParsers;
        List<LabelSort> labelSortList = new ArrayList<>();
        open(false);
        try {
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement("SELECT * FROM " + PRIORITY);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                LabelSort labelSort = new LabelSort();
                labelSort.setName(rs.getString(STRING_NAME));
                labelSort.setTopPosition(rs.getInt(PRIORITY_INDICATOR));
                labelSortList.add(labelSort);
            }
            preparedStatement.close();
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("    FINISH getLabelSortList ");
        return labelSortList;
    }

}
