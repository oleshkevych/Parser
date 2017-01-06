package com.parser.dbmanager;

import com.parser.entity.JobsInform;
import com.parser.entity.JobsInformForSearch;
import com.parser.entity.ListImpl;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Syntax.Java;

/**
 * Created by vov4ik on 12/11/2016.
 */
public class DbHelper {

    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private final static String DROP_TABLE = "DROP TABLE IF EXISTS ";

    // Database table names
    private final static String DATEUPDATE = "DateUpdate";
    private final static String JOB_INFORM = "JobsInform";
    private final static String DESCRIPTION = "Description";
    private final static String LIST = "List";

    // Database column names
    private final static String ID = "Id";
    private final static String DATE_LAST_UPDATE = "LastUpdateDate";
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
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;
    private List<String> listParsers = new ArrayList<>();

    public DbHelper() {

        listParsers.add("wfh.io");
        listParsers.add("remoteok.io");
        listParsers.add("landing.jobs");
        listParsers.add("startus.cc");
        listParsers.add("virtualvocations.com");
        listParsers.add("simplyhired.com");
        listParsers.add("stackoverflow.com");
        listParsers.add("juju.com");
        listParsers.add("jobs.drupal.org");
        listParsers.add("dutchstartupjobs.com");
        listParsers.add("monster.de");
        listParsers.add("weloveangular.com");
        listParsers.add("weworkremotely.com");
        listParsers.add("startupjobs.se");
        listParsers.add("berlinstartupjobs.com");
        listParsers.add("flexjobs.com");
        listParsers.add("builtinnode.com");
        listParsers.add("weworkmeteor.com");
        listParsers.add("jobbank.dk");
        listParsers.add("workingnomads.co");
        listParsers.add("remote.co");
        listParsers.add("randstad.com");
        listParsers.add("workopolis.com");
        listParsers.add("eluta.ca");
        listParsers.add("webbjobb.io");
        listParsers.add("jobspresso.co");
        listParsers.add("jobs.ch");
        listParsers.add("jobs.smashingmagazine.com");
        listParsers.add("themuse.com");
        listParsers.add("techjobs.com");
        listParsers.add("careerbuilder.com");
        listParsers.add("webentwickler-jobs.de");
        listParsers.add("uberjobs.de");
        listParsers.add("guru.com");
        listParsers.add("authenticjobs.com");
        listParsers.add("eurojobs.com");
        listParsers.add("technojobs.co.uk");
        listParsers.add("learn4good.com");
        listParsers.add("canadajobs.com");
        listParsers.add("drupal.org.uk");
        listParsers.add("ziprecruiter.com");
        listParsers.add("drupalcenter.de");
        listParsers.add("indeed.com");
        listParsers.add("wowjobs.ca");
        listParsers.add("builtinaustin.com");
        listParsers.add("betalist.com");
        listParsers.add("unicornhunt.io");
        listParsers.add("jobs.justlanded.com");
        listParsers.add("jobs.remotive.io");
    }

    public Connection connect(String link) {
        try {
//            String home = System.getProperty("user.home");
//            File f = new File(home + File.separator + "Desktop" + File.separator + "Testing" + File.separator" + ".txt");
            Class.forName("org.sqlite.JDBC");
            String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);

            try {
                path = path.substring(0, path.lastIndexOf("/"));
            }catch (Exception e){
//                e.printStackTrace();
            }
            /*System.out.println(path+"  "+*/ new File("lib").mkdir()/*)*/;
            if (link != null) {
                connection = DriverManager.getConnection("jdbc:sqlite:" + "lib/" + "ParserDB" + listParsers.indexOf(link) + ".s3db");
            } else {
                connection = DriverManager.getConnection("jdbc:sqlite:" + "lib/" + "ParserDBDate.s3db");
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

    private void open(String link) {
        try {
            if (link != null) {
                statement = connect(link).createStatement();
            } else {
                statement = connect(null).createStatement();
            }
            createTables(link);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables(String link) {
        try {
            if (link != null) {
                statement.execute(CREATE_TABLE + "'" + JOB_INFORM + "' (" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PLACE + " STRING, " +
                        COMPANY_NAME + " STRING, " +
                        HEAD_PUBLICATION + " STRING, " +
                        PUBLICATION_LINK + " STRING, " +
                        DATE_PUBLICATION + " LONG , " +
                        IS_SEEN + " INTEGER);");
                statement.execute(CREATE_TABLE + "'" + DESCRIPTION + "' (" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        JOB_ID + " INTEGER, " +
                        IS_NULL + " INTEGER, " +
                        TEXT_FIELD + " STRING, " +
                        LIST_HEADER + " STRING);");
                statement.execute(CREATE_TABLE + "'" + LIST + "' (" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DESCRIPTION_ID + " INTEGER, " +
                        LIST_ITEM + " STRING);");
                statement.close();
                System.out.println("created");
            } else {
                statement.execute(CREATE_TABLE + "'" + DATEUPDATE + "' (" +
                        DATE_LAST_UPDATE + " LONG);");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDateLastUpdate() {
        open(null);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DROP_TABLE + DATEUPDATE);
            preparedStatement.executeUpdate();
            createTables(null);
            preparedStatement = connection
                    .prepareStatement("INSERT INTO '" + DATEUPDATE + "' ('" + DATE_LAST_UPDATE + "') VALUES ('" + new Date().getTime() + "'); ",
                            Statement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("    FAIL: DateSET");
            e.printStackTrace();
        }
    }

    public long getDateLastUpdate() {
        open(null);
        try {
            long date = 0;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + DATEUPDATE);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                date = rs.getLong(DATE_LAST_UPDATE);
            }
            rs.close();
            preparedStatement.close();
            connection.close();
            return date;
        } catch (SQLException e) {
            System.out.println("    FAIL: DateGET");
            e.printStackTrace();
            return 0;
        }
    }

    public int writeDB(String siteName, final List<JobsInform> jobsInforms) {
        open(siteName);

        try {
            // counting new results
            int newResults = jobsInforms.size();

            preparedStatement = connection.prepareStatement("SELECT * FROM " + JOB_INFORM);
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
            for (JobsInform jobsInform : oldJobsInforms) {
                if (jobsInforms.contains(jobsInform)) {
                    if (jobsInform.isSeen()) {
                        newResults--;
                    }
                    jobsInforms.get(jobsInforms.indexOf(jobsInform)).setSeen(jobsInform.isSeen());
                }
            }

            System.out.println("    SITE: " + siteName);
            System.out.println("    NEW RESULTS: " + newResults);

            if (jobsInforms.size() != 0 && newResults != 0) {
                //dropping DB
                preparedStatement = connection.prepareStatement(DROP_TABLE + JOB_INFORM);
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement(DROP_TABLE + DESCRIPTION);
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement(DROP_TABLE + LIST);
                preparedStatement.executeUpdate();
                createTables(siteName);

                //writing Jobs to JobInform Table
                writeJobsInform(jobsInforms);
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

    private void writeJobsInform(List<JobsInform> jobsInforms) {
        for (JobsInform jobsInform : jobsInforms) {
            try {
                preparedStatement = connection.prepareStatement("INSERT INTO '" + JOB_INFORM + "' ('" + PLACE + "', '"
                                + COMPANY_NAME + "', '" + HEAD_PUBLICATION + "','" + PUBLICATION_LINK + "', '" + DATE_PUBLICATION + "', '" + IS_SEEN + "') VALUES ( ?, ?, ?, ?, ?, ?); ",
                        Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, jobsInform.getPlace() != null ? jobsInform.getPlace() : "");
                preparedStatement.setString(2, jobsInform.getCompanyName() != null ? jobsInform.getCompanyName() : "");
                preparedStatement.setString(3, jobsInform.getHeadPublication());
                preparedStatement.setString(4, jobsInform.getPublicationLink());
                preparedStatement.setLong(5, (jobsInform.getPublishedDate() != null && jobsInform.getPublishedDate().getTime() != new Date(0).getTime()) ? jobsInform.getPublishedDate().getTime() : 0);
                preparedStatement.setInt(6, (jobsInform.isSeen() ? 1 : 0));
                preparedStatement.executeUpdate();

                int jobInformId = jobsInforms.indexOf(jobsInform) + 1;

                // writing OrderList to Description table
                if (jobsInform.getOrder() != null) {
                    writingOrderList(jobsInform.getOrder(), jobInformId);
                } else {
                    List<ListImpl> list = new ArrayList<>();
                    ListImpl l = new ListImpl();
                    l.setListHeader(jobsInform.getHeadPublication());
                    list.add(l);
                    writingOrderList(list, jobInformId);
                }
            } catch (SQLException e) {
                System.out.println("    FAIL: JobsInformWriting");
                e.printStackTrace();
            }


        }

    }

    private void writingOrderList(List<ListImpl> jobsInformList, int jobInformId) {
        for (ListImpl list : jobsInformList) {
            try {
                if (list != null) {
                    preparedStatement = connection.prepareStatement("INSERT INTO '" + DESCRIPTION + "' ('" + JOB_ID + "', '"
                                    + TEXT_FIELD + "', '" + LIST_HEADER + "', '" + IS_NULL + "') VALUES ("
                                    + jobInformId + ", '" + (list.getTextFieldImpl() != null ? list.getTextFieldImpl().replaceAll("'", "*") : list.getTextFieldImpl()) + "', '"
                                    + (list.getListHeader() != null ? list.getListHeader().replaceAll("'", "*") : list.getListHeader()) + "', " + (list != null ? 1 : 0) + "); ",
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.executeUpdate();
                    int listImplId = jobsInformList.indexOf(list) + 1;
                    if (list.getListItem() != null && list.getListItem().size() > 0) {
                        //writing ListImplementation to List table
                        for (String s : list.getListItem()) {
                            preparedStatement = connection.prepareStatement("INSERT INTO '" + LIST + "' ('" + DESCRIPTION_ID + "', '"
                                    + LIST_ITEM + "') VALUES ("
                                    + listImplId + ", '" + s.replaceAll("'", "*") + "'); ");
                            preparedStatement.executeUpdate();
                        }
                    }
                } else {
                    preparedStatement = connection.prepareStatement("INSERT INTO '" + DESCRIPTION + "' ('" + JOB_ID + "', '"
                                    + IS_NULL + "') VALUES ("
                                    + jobInformId + ", '" + (list != null ? 1 : 0) + "'); ",
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                System.out.println("    FAIL: OrderListWriting");
                e.printStackTrace();
            }
        }
    }

    public List<JobsInform> getJobsInformFromDb(String siteName) {
        List<JobsInform> jobsInforms = new ArrayList<>();
        System.out.println("    FINISH: " + siteName);
        try {
            connect(siteName);
            PreparedStatement preparedStatement = null;
            PreparedStatement preparedStatementDescription = null;
            ResultSet resultSetDescription = null;
            preparedStatement = connection.prepareStatement("SELECT * FROM " + JOB_INFORM);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                JobsInform jobsInform = new JobsInform();
                preparedStatementDescription = connection.prepareStatement("SELECT * FROM " + DESCRIPTION + " WHERE " + JOB_ID + " = ? ;");
                preparedStatementDescription.setInt(1, rs.getInt(ID));
                resultSetDescription = preparedStatementDescription.executeQuery();
//                while (resultSetDescription.next()) {
//                    ListImpl list = new ListImpl();
//                    if (resultSetDescription.getInt(IS_NULL) == 1) {
//                        preparedStatementList = connection.prepareStatement("SELECT * FROM " + LIST + " WHERE " + DESCRIPTION_ID + " = ? ;");
//                        preparedStatementList.setInt(1, resultSetDescription.getInt(ID));
//                        resultSetList = preparedStatementList.executeQuery();
//                        List<String> listString = new ArrayList<>();
//                        while (resultSetList.next()) {
//                            listString.add(resultSetList.getString(LIST_ITEM));
//                        }
//                        if (listString.size() > 0) {
//                            list.setListItem(listString);
//                        } else {
//                            list.setListItem(null);
//                        }
//                        list.setListHeader(resultSetDescription.getString(LIST_HEADER).equals("null") ? null : resultSetDescription.getString(LIST_HEADER));
//                        list.setTextFieldImpl(resultSetDescription.getString(TEXT_FIELD).equals("null") ? null : resultSetDescription.getString(TEXT_FIELD));
//                        resultSetList.close();
//                        preparedStatementList.close();
//                    } else {
//                        list = null;
//                    }
//                    orderList.add(list);
//                }
                jobsInform.setOrder(getOrderList(resultSetDescription));
                jobsInform.setId(rs.getInt(ID));
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

    private List<ListImpl> getOrderList(ResultSet resultSetDescription) throws SQLException{
        PreparedStatement preparedStatementList = null;
        ResultSet resultSetList = null;
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
        if(resultSetList!=null)
        resultSetList.close();
        if(preparedStatementList != null)
        preparedStatementList.close();
        return orderList;
    }

    public void setIsSeen(JobsInformForSearch j) {
        open(j.getSiteName());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + JOB_INFORM + " SET " + IS_SEEN + "=" + 1 + " WHERE " + ID + "=" + j.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNewResults(String siteName) {
        open(siteName);

        try {
            int newResults = 0;
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement("SELECT * FROM " + JOB_INFORM + " WHERE " + IS_SEEN + "=" + 0);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                newResults++;
            }
            preparedStatement.close();
            rs.close();
            connection.close();
            return newResults;
        } catch (SQLException e) {
            e.printStackTrace();
            return 99999;
        }
    }
}
