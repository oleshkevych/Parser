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

    private final static String DB_NAME_1 = "ParserDB1.s3db";
    private final static String DB_NAME_2 = "ParserDB2.s3db";
    private final static String DB_NAME_3 = "ParserDB3.s3db";
    private final static String DB_NAME_4 = "ParserDB4.s3db";
    private final static String DB_NAME_5 = "ParserDB5.s3db";
    private final static String DB_NAME_6 = "ParserDB6.s3db";
    private final static String DB_NAME_7 = "ParserDB7.s3db";
    private final static String DB_NAME_8 = "ParserDB8.s3db";
    private final static String DB_NAME_9 = "ParserDB9.s3db";
    private final static String DB_NAME_10 = "ParserDB10.s3db";
    private final static String DB_NAME_11 = "ParserDB11.s3db";
    private final static String DB_NAME_12 = "ParserDB12.s3db";
    private final static String DB_NAME_13 = "ParserDB13.s3db";
    private final static String DB_NAME_14 = "ParserDB14.s3db";
    private final static String DB_NAME_15 = "ParserDB15.s3db";
    private final static String DB_NAME_16 = "ParserDB16.s3db";
    private final static String DB_NAME_17 = "ParserDB17.s3db";
    private final static String DB_NAME_18 = "ParserDB18.s3db";
    private final static String DB_NAME_19 = "ParserDB19.s3db";
    private final static String DB_NAME_20 = "ParserDB20.s3db";
    private final static String DB_NAME_21 = "ParserDB21.s3db";
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


    private Connection connection = null;
    private Statement statement = null;
    private ResultSet rs = null;
    private Map<String, String> mapParsers = new HashMap<>();

    public DbHelper(){

        mapParsers.put("wfh.io", DB_NAME_1);
        mapParsers.put("remoteok.io", DB_NAME_1);
        mapParsers.put("landing.jobs", DB_NAME_1);
             mapParsers.put("startus.cc", DB_NAME_18);
        mapParsers.put("virtualvocations.com", DB_NAME_2);
        mapParsers.put("simplyhired.com", DB_NAME_2);
        mapParsers.put("stackoverflow.com", DB_NAME_3);
        mapParsers.put("juju.com", DB_NAME_3);
        mapParsers.put("jobs.drupal.org", DB_NAME_3);
        mapParsers.put("dutchstartupjobs.com", DB_NAME_20);
        mapParsers.put("monster.de", DB_NAME_4);
        mapParsers.put("weloveangular.com", DB_NAME_4);
        mapParsers.put("weworkremotely.com", DB_NAME_5);
        mapParsers.put("startupjobs.se", DB_NAME_5);
        mapParsers.put("berlinstartupjobs.com", DB_NAME_5);
        mapParsers.put("flexjobs.com", DB_NAME_6);
        mapParsers.put("builtinnode.com", DB_NAME_6);
        mapParsers.put("weworkmeteor.com", DB_NAME_7);
        mapParsers.put("jobbank.dk", DB_NAME_7);
        mapParsers.put("workingnomads.co", DB_NAME_7);
        mapParsers.put("remote.co", DB_NAME_8);
        mapParsers.put("randstad.com", DB_NAME_8);
        mapParsers.put("workopolis.com", DB_NAME_8);
        mapParsers.put("eluta.ca", DB_NAME_9);
        mapParsers.put("webbjobb.io", DB_NAME_9);
        mapParsers.put("jobspresso.co", DB_NAME_10);
        mapParsers.put("jobs.ch", DB_NAME_10);
        mapParsers.put("jobs.smashingmagazine.com", DB_NAME_11);
        mapParsers.put("themuse.com", DB_NAME_11);
        mapParsers.put("techjobs.com", DB_NAME_12);
        mapParsers.put("careerbuilder.com", DB_NAME_12);
        mapParsers.put("webentwickler-jobs.de", DB_NAME_13);
        mapParsers.put("uberjobs.de", DB_NAME_14);
        mapParsers.put("guru.com", DB_NAME_14);
        mapParsers.put("authenticjobs.com", DB_NAME_15);
        mapParsers.put("eurojobs.com", DB_NAME_15);
        mapParsers.put("technojobs.co.uk", DB_NAME_16);
        mapParsers.put("learn4good.com", DB_NAME_16);
        mapParsers.put("canadajobs.com", DB_NAME_16);
        mapParsers.put("drupal.org.uk", DB_NAME_17);
        mapParsers.put("ziprecruiter.com", DB_NAME_19);
    }

    public Connection connect(String link) {
        try {
            Class.forName("org.sqlite.JDBC");
//            String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
//            new File(path+"lib").mkdir();
//            File f = new File(path + "lib/" + DB_NAME);
//            System.out.println(path+"lib/" + DB_NAME+"  "+ new File(".").getCanonicalPath());
//            if (!f.exists()) {
//                f.createNewFile();
//            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + mapParsers.get(link));


            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void open(String link) {
        try {
            statement = connect(link).createStatement();
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int writeDB(String siteName, final List<JobsInform> jobsInforms) {
        open(siteName);

        try {


            PreparedStatement preparedStatement = null;
            int siteID = 0;
            int newResults = jobsInforms.size();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + SITE);
            rs = preparedStatement.executeQuery();
            boolean isExists = false;
            while (rs.next()) {
                if (rs.getString(SITE_NAME).equals(siteName)) {
                    siteID = rs.getInt(ID);
                    isExists = true;
                }
            }
            if (isExists) {
                preparedStatement = connection.prepareStatement("SELECT * FROM " + JOB_INFORM + " WHERE " + SITE_ID + " = ? ;");
                preparedStatement.setInt(1, siteID);
                rs = preparedStatement.executeQuery();
                List<JobsInform> oldJobsInforms = new ArrayList<>();
                while (rs.next()) {
                    JobsInform jobsInform = new JobsInform();
                    jobsInform.setHeadPublication(rs.getString(HEAD_PUBLICATION));
                    jobsInform.setSeen(rs.getInt(IS_SEEN) == 1);
                    jobsInform.setPublicationLink(rs.getString(PUBLICATION_LINK));
                    jobsInform.setCompanyName(rs.getString(COMPANY_NAME));
                    jobsInform.setPlace(rs.getString(PLACE));
                    oldJobsInforms.add(jobsInform);
                }
                boolean writeTrigger = false;
                for (JobsInform jobsInform : oldJobsInforms) {
                    if (jobsInforms.contains(jobsInform)) {
                        newResults--;
                        if(!jobsInforms.get(jobsInforms.indexOf(jobsInform)).isSeen()) {
                            jobsInforms.get(jobsInforms.indexOf(jobsInform)).setSeen(true);
                            writeTrigger = true;
                        }
                    }
                }
//                if(newResults == 0 && writeTrigger){
//                    return 0;
//                }
            }
            System.out.println("    SITE: " + siteName);

            System.out.println("    NEW RESULTS: " + newResults);

            if (!isExists) {
                preparedStatement = connection
                        .prepareStatement("INSERT INTO '" + SITE + "' ('" + SITE_NAME + "') VALUES ('" + siteName + "'); ",
                                Statement.RETURN_GENERATED_KEYS);
                preparedStatement.executeUpdate();
                siteID++;
            }
            preparedStatement = connection.prepareStatement("SELECT * FROM '" + JOB_INFORM + "'");
            rs = preparedStatement.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
            }

            preparedStatement = connection.prepareStatement("DELETE FROM " + JOB_INFORM + " WHERE " + SITE_ID + " = ? ;");
            preparedStatement.setInt(1, siteID);
            preparedStatement.executeUpdate();
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
                preparedStatement.setLong(6,  jobsInform.getPublishedDate()!=null ? jobsInform.getPublishedDate().getTime() : 0);
                preparedStatement.setInt(7, (jobsInform.isSeen() ? 1 : 0));
                preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement("SELECT * FROM " + JOB_INFORM + " WHERE " + SITE_ID + " = ? ;");
                preparedStatement.setInt(1, siteID);
                rs = preparedStatement.executeQuery();
                count = 0;
                while (rs.next()) {
                    count = rs.getInt(ID);
                }
                int jobInformId = count;

                if (jobsInform.getOrder() != null)
                    for (ListImpl list : jobsInform.getOrder()) {
                        if (list != null) {
                            preparedStatement = connection.prepareStatement("INSERT INTO '" + DESCRIPTION + "' ('" + JOB_ID + "', '"
                                            + TEXT_FIELD + "', '" + LIST_HEADER + "', '" + IS_NULL + "', '" + SITE_ID + "') VALUES ("
                                            + jobInformId + ", '" + (list.getTextFieldImpl() != null ? list.getTextFieldImpl().replaceAll("'", "*") : list.getTextFieldImpl()) + "', '"
                                            + (list.getListHeader() != null ? list.getListHeader().replaceAll("'", "*") : list.getListHeader()) + "', " + (list != null ? 1 : 0) + ", " + siteID + "); ",
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
                        } else {
                            preparedStatement = connection.prepareStatement("INSERT INTO '" + DESCRIPTION + "' ('" + JOB_ID + "', '"
                                            + IS_NULL + "', '" + SITE_ID + "') VALUES ("
                                            + jobInformId + ", '" + (list != null ? 1 : 0) + "', " + siteID + "); ",
                                    Statement.RETURN_GENERATED_KEYS);
                            preparedStatement.executeUpdate();
                        }
                    }

            }
            rs.close();
            preparedStatement.close();
            connection.close();
            System.out.println("    FINISH: " + siteName);
            return newResults;
        } catch (SQLException e) {
            System.out.println("    FAIL: " + siteName);
            e.printStackTrace();
            return 0;
        }


    }

    public List<JobsInform> getJobsInformFromDb(String siteName) {
        List<JobsInform> jobsInforms = new ArrayList<>();
        try {
            connect(siteName);
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
                while (resultSetDescription.next()) {
                    ListImpl list = new ListImpl();
                    if (resultSetDescription.getInt(IS_NULL) == 1) {
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
                        list.setListHeader(resultSetDescription.getString(LIST_HEADER).equals("null") ? null : resultSetDescription.getString(LIST_HEADER));
                        list.setTextFieldImpl(resultSetDescription.getString(TEXT_FIELD).equals("null") ? null : resultSetDescription.getString(TEXT_FIELD));
                        resultSetList.close();
                        preparedStatementList.close();
                    } else {
                        list = null;
                    }
                    orderList.add(list);
                }
                jobsInform.setOrder(orderList);
                jobsInform.setPublishedDate(rs.getLong(DATE_PUBLICATION) != 0 ? new Date(rs.getLong(DATE_PUBLICATION)) : null);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobsInforms;
    }


}
