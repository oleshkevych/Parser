package com.parser.dbmanager;

import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by vov4ik on 12/11/2016.
 */
public class DbHelper {

    private final static String DB_NAME = "ParserDB.s3db";
    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private final static String DROP_TABLE = "DROP TABLE IF EXISTS ";

    // Database table names
    private final static String SITE = "Site";
    private final static String JOB_INFORM = "JobsInform";
    private final static String DESCRIPTION = "Description";
    private final static String LIST = "List";

    // Database column names
    private final static String ID = "Id";
    private final static String SITE_NAME = "SiteName";
    private final static String SITE_ID = "SiteId";
    private final static String HEAD_PUBLICATION = "HeadPublication";
    private final static String PLACE = "Place";
    private final static String COMPANY_NAME = "CompanyName";
    private final static String PUBLICATION_LINK = "PublicationLink";
    private final static String DATE_PUBLICATION = "DatePublication";
    private final static String IS_SEEN = "isSeen";
    private final static String JOB_ID = "JobId";
    private final static String LIST_HEADER = "ListHeader";
    private final static String TEXT_FIELD = "TextField";
    private final static String IS_NULL = "IsDescriptionNull";
    private final static String DESCRIPTION_ID = "ListId";
    private final static String LIST_ITEM = "ListItem";


    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet rs = null;

    public Connection connect(){
        try {
            Class.forName("org.sqlite.JDBC");
//            String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
//            new File(path+"lib").mkdir();
//            File f = new File(path + "lib/" + DB_NAME);
//            System.out.println(path+"lib/" + DB_NAME+"  "+ new File(".").getCanonicalPath());
//            if (!f.exists()) {
//                f.createNewFile();
//            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);


            return connection;
        }catch (SQLException e) {
            e.printStackTrace();
                return  null;
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            return  null;
        }
    }
    public void open(){
        try {
            statement = connect().createStatement();
            statement.execute(CREATE_TABLE + "'" + SITE + "' (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SITE_NAME + " TEXT);");
            statement.execute(CREATE_TABLE + "'" + JOB_INFORM + "' (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SITE_ID + " INTEGER, " +
                    PLACE + " STRING, " +
                    COMPANY_NAME + " STRING, " +
                    HEAD_PUBLICATION + " STRING, " +
                    PUBLICATION_LINK + " STRING, " +
                    DATE_PUBLICATION + " LONG , " +
                    IS_SEEN + " INTEGER);");
            statement.execute(CREATE_TABLE + "'" + DESCRIPTION + "' (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SITE_ID + " INTEGER, " +
                    JOB_ID + " INTEGER, " +
                    IS_NULL + " INTEGER, " +
                    TEXT_FIELD + " STRING, " +
                    LIST_HEADER + " STRING);");
            statement.execute(CREATE_TABLE + "'" + LIST + "' (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SITE_ID + " INTEGER, " +
                    DESCRIPTION_ID + " INTEGER, " +
                    LIST_ITEM + " STRING);");
            statement.close();
            System.out.println("created");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void writeDB(String siteName, List<JobsInform> jobsInforms) {
        open();
        PreparedStatement preparedStatement = null;
        try {

            int siteID = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM " + SITE);
            rs = preparedStatement.executeQuery();
            boolean isExists = false;
            while (rs.next()) {
                siteID = rs.getInt(ID);
                if (rs.getString(SITE_NAME).equals(siteName)) {
                    isExists = true;
                }
            }
            if (!isExists) {
                preparedStatement = connection
                        .prepareStatement("INSERT INTO '" + SITE + "' ('" + SITE_NAME + "') VALUES ('" + siteName + "'); ",
                                Statement.RETURN_GENERATED_KEYS);
                preparedStatement.executeUpdate();
                siteID++;
            }
            System.out.println("    IS siteID: " + siteID);
            preparedStatement = connection.prepareStatement("SELECT * FROM '" + JOB_INFORM + "'");
            rs = preparedStatement.executeQuery();
            int count = 0;
            System.out.println("    start running jobs table : ");
            while (rs.next()) {
                System.out.println("    IS Header before: " + count + " " + rs.getString(SITE_ID));
                count++;
            }
            System.out.println("    finish running before jobs table : " + count);

            preparedStatement = connection.prepareStatement("DELETE FROM " + JOB_INFORM + " WHERE " + SITE_ID + " = ? ;");
            preparedStatement.setInt(1, siteID);
            preparedStatement.executeUpdate();
//            preparedStatement = connection.prepareStatement("SELECT * FROM '" + JOB_INFORM + "'");
//            rs = preparedStatement.executeQuery();
//            int count1 = 0;
//            while (rs.next()) {
//                System.out.println("    IS Header after: " + count1 + " " + rs.getString(SITE_ID));
//                count1++;
//            }
//            System.out.println("    finish running after jobs table : " + count1);
            preparedStatement = connection.prepareStatement("DELETE FROM " + DESCRIPTION + " WHERE " + SITE_ID + " = ? ;");
            preparedStatement.setInt(1, siteID);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM " + LIST + " WHERE " + SITE_ID + " = ? ;");
            preparedStatement.setInt(1, siteID);
            preparedStatement.executeUpdate();
            for (JobsInform jobsInform : jobsInforms) {

                preparedStatement = connection.prepareStatement("INSERT INTO '" + JOB_INFORM + "' ('" + SITE_ID + "', '" + PLACE + "', '"
                                + COMPANY_NAME + "', '" + HEAD_PUBLICATION + "','" + PUBLICATION_LINK + "', '" + DATE_PUBLICATION + "', '" + IS_SEEN + "') VALUES (?, ?, ?, ?, ?, ?, ?); ",
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, siteID);
                preparedStatement.setString(2, jobsInform.getPlace());
                preparedStatement.setString(3, jobsInform.getCompanyName());
                preparedStatement.setString(4, jobsInform.getHeadPublication());
                preparedStatement.setString(5, jobsInform.getPublicationLink());
                preparedStatement.setLong(6, jobsInform.getPublishedDate().getTime());
                preparedStatement.setInt(7, (jobsInform.isSeen() ? 1 : 0));
                preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement("SELECT * FROM " + JOB_INFORM + " WHERE " + SITE_ID + " = ? ;");
                preparedStatement.setInt(1, siteID);
                rs = preparedStatement.executeQuery();
                count = 0;
//            System.out.println("    IS counting jobs : " + jobInformId);
                while (rs.next()) {
                    count = rs.getInt(ID);
//                System.out.println("    IS Header jobs: "+count+" " + rs.getString(HEAD_PUBLICATION));
                }
                int jobInformId = count;
//            System.out.println("    IS jobInformId: " + jobInformId);

                for (ListImpl list : jobsInform.getOrder()) {
                    if (list != null) {
                        preparedStatement = connection.prepareStatement("INSERT INTO '" + DESCRIPTION + "' ('" + JOB_ID + "', '"
                                        + TEXT_FIELD + "', '" + LIST_HEADER + "', '" + IS_NULL + "', '" + SITE_ID + "') VALUES ("
                                        + jobInformId + ", '" + (list.getTextFieldImpl() != null ? list.getTextFieldImpl().replaceAll("'", "*") : list.getTextFieldImpl()) + "', '"
                                        + (list.getListHeader() != null ? list.getListHeader().replaceAll("'", "*") : list.getListHeader()) + "', " + (list != null? 1 : 0) + ", " + siteID + "); ",
                                Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.executeUpdate();
                        preparedStatement = connection.prepareStatement("SELECT * FROM " + DESCRIPTION + " WHERE " + JOB_ID + " = ? ;");
                        preparedStatement.setInt(1, jobInformId);
                        rs = preparedStatement.executeQuery();
                        count = 0;
//            System.out.println("    IS counting jobs : " + jobInformId);
                        while (rs.next()) {
                            count = rs.getInt(ID);
//                System.out.println("    IS Header jobs: "+count+" " + rs.getString(HEAD_PUBLICATION));
                        }
                        int listImplId = count;
//            System.out.println("    IS jobInformId: " + jobInformId);
                        if (list.getListItem() != null && list.getListItem().size() > 0) {
                            for (String s : list.getListItem()) {
                                preparedStatement = connection.prepareStatement("INSERT INTO '" + LIST + "' ('" + DESCRIPTION_ID + "', '"
                                        + LIST_ITEM + "', '" + SITE_ID + "') VALUES ("
                                        + listImplId + ", '" + s.replaceAll("'", "*") + "', " + siteID + "); ");
                                preparedStatement.executeUpdate();
//                            System.out.println("    IS DONE: " + listImplId + " " + p);
                            }
//                        preparedStatement = connection.prepareStatement("SELECT * FROM '" + LIST + "'");
//                        rs = preparedStatement.executeQuery();
//                        int count2 = 0;
//                        System.out.println("    IS Header0 : "+count+" " + rs.getFetchSize());
//                        while (rs.next()){
//                            System.out.println("    IS Header after: "+count2+" " + rs.getString(LIST_ITEM));
//                            count2++;
//                        }
                        }
                    }else{
                        preparedStatement = connection.prepareStatement("INSERT INTO '" + DESCRIPTION + "' ('" + JOB_ID + "', '"
                                        + IS_NULL + "', '" + SITE_ID + "') VALUES ("
                                        + jobInformId + ", '" + (list != null? 1 : 0) + "', " + siteID + "); ",
                                Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.executeUpdate();
                    }
                }

            }
            rs.close();
            preparedStatement.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public List<JobsInform> getJobsInformFromDb(String siteName){
        List<JobsInform> jobsInforms = new ArrayList<>();
        try {
            connect();
            PreparedStatement preparedStatement = null;
            PreparedStatement preparedStatementDescription = null;
            PreparedStatement preparedStatementList = null;
            ResultSet resultSetDescription = null;
            ResultSet resultSetList = null;
            int siteID = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM " + SITE);
            rs = preparedStatement.executeQuery();
            boolean isExists = false;
            while (rs.next()) {
                if (rs.getString(SITE_NAME).equals(siteName)) {
                    siteID = rs.getInt(ID);
                    isExists = true;
                }
            }
            if (!isExists) {
                System.out.println("  Wrong site name!  ");
                return jobsInforms;
            }


            preparedStatement = connection.prepareStatement("SELECT * FROM " + JOB_INFORM + " WHERE " + SITE_ID + " = ? ;");
            preparedStatement.setInt(1, siteID);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                JobsInform jobsInform = new JobsInform();
                preparedStatementDescription = connection.prepareStatement("SELECT * FROM " + DESCRIPTION + " WHERE " + JOB_ID + " = ? ;");
                preparedStatementDescription.setInt(1, rs.getInt(ID));
                resultSetDescription = preparedStatementDescription.executeQuery();
                List<ListImpl> orderList = new ArrayList<>();
                while(resultSetDescription.next()){
                    ListImpl list = new ListImpl();
                    if(resultSetDescription.getInt(IS_NULL) == 1) {
                        preparedStatementList = connection.prepareStatement("SELECT * FROM " + LIST + " WHERE " + DESCRIPTION_ID + " = ? ;");
                        preparedStatementList.setInt(1, resultSetDescription.getInt(ID));
                        resultSetList = preparedStatementList.executeQuery();
                        List<String> listString = new ArrayList<>();
                        while (resultSetList.next()) {
                            listString.add(resultSetList.getString(LIST_ITEM));
                        }
                        if (listString.size() > 0) {
                            list.setListItem(listString);
                        } else {
                            list.setListItem(null);
                        }
                        list.setListHeader(resultSetDescription.getString(LIST_HEADER).equals("null")? null : resultSetDescription.getString(LIST_HEADER));
                        list.setTextFieldImpl(resultSetDescription.getString(TEXT_FIELD).equals("null")? null : resultSetDescription.getString(TEXT_FIELD));
                        resultSetList.close();
                        preparedStatementList.close();
                    }else{
                        list = null;
                    }
                    orderList.add(list);
                }
                jobsInform.setOrder(orderList);
                jobsInform.setPublishedDate(new Date(rs.getLong(DATE_PUBLICATION)));
                jobsInform.setCompanyName(rs.getString(COMPANY_NAME));
                jobsInform.setHeadPublication(rs.getString(HEAD_PUBLICATION));
                jobsInform.setPlace(rs.getString(PLACE));
                jobsInform.setSeen(rs.getInt(IS_SEEN) == 1);
                jobsInform.setPublicationLink(rs.getString(PUBLICATION_LINK));
                jobsInforms.add(jobsInform);
                preparedStatementDescription.close();
                resultSetDescription.close();
            }
            preparedStatement.close();


            rs.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return jobsInforms;
    }


}
