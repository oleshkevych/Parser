package com.parser.dbmanager;

import com.parser.entity.JobsInform;
import com.parser.entity.ListImpl;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Created by vov4ik on 12/11/2016.
 */
public class DbHelper {

    private final static String DB_NAME = "ParserNEW.s3db";
    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private final static String DROP_TABLE = "DROP TABLE IF EXISTS ";

    // Database table names
    private final static String SITE = "Site";
    private final static String JOB_INFORM = "JobsInform ";
    private final static String DESCRIPTION = "Description";
    private final static String LIST = "List";

    // Database column names
    private final static String ID = "Id";
    private final static String SITE_NAME = "SiteName";
    private final static String SITE_ID = "SiteId";
    private final static String HEAD_PUBLICATION = "HeadPublication";
    private final static String PLACE = "Place";
    private final static String COMPANY_NAME = "CompanyName";
    private final static String DATE_PUBLICATION = "DatePublication";
    private final static String IS_SEEN = "isSeen";
    private final static String JOB_ID = "JobId";
    private final static String LIST_HEADER = "ListHeader";
    private final static String TEXT_FIELD = "TextField";
    private final static String DESCRIPTION_ID = "ListId";
    private final static String LIST_ITEM = "ListItem";


    private static Connection connection = null;
    private static Statement statement = null;

    public Connection connect() throws ClassNotFoundException, SQLException{
//        try {
            Class.forName("org.sqlite.JDBC");
//            String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
//            new File(path+"lib").mkdir();
//            File f = new File(path + "lib/" + DB_NAME);
//            System.out.println(path+"lib/" + DB_NAME+"  "+ new File(".").getCanonicalPath());
//            if (!f.exists()) {
//                f.createNewFile();
//            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);

            System.out.println("Connected");
            return connection;
//        }catch (IOException e) {
//            e.printStackTrace();
//                return  null;
//        }
    }
    public void open() throws ClassNotFoundException, SQLException{
        statement = connect().createStatement();
        statement.execute(CREATE_TABLE +"'"+ SITE+"' ("+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SITE_NAME + " TEXT);");
        statement.execute(CREATE_TABLE +"'" + JOB_INFORM + "' ("+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SITE_ID + " INTEGER , " +
                PLACE + " STRING, " +
                COMPANY_NAME + " STRING, " +
                HEAD_PUBLICATION + " STRING, " +
                DATE_PUBLICATION + " TEXT , " +
                IS_SEEN + " INTEGER);");
        statement.execute(CREATE_TABLE + "'"+ DESCRIPTION +"' ("+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                JOB_ID + " INTEGER, " +
                TEXT_FIELD + " STRING, " +
                LIST_HEADER + " STRING);");
        statement.execute(CREATE_TABLE + "'"+ LIST +"' ("+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DESCRIPTION_ID + " INTEGER, " +
                LIST_ITEM + " STRING);");
//        statement.close();
        System.out.println("created");

    }

    public void writeDB(String siteName1, JobsInform jobsInform) throws SQLException, ClassNotFoundException {
//        Statement statement = open().Statement.RETURN_GENERATED_KEYS);
        open();
        Statement preparedStatement = statement;
        System.out.println("start adding");

//        preparedStatement = connection
//                .prepareStatement("INSERT INTO '"+ SITE +"' ('"+SITE_NAME+"') VALUES ('" + siteName1 + "'); ",
//                Statement.RETURN_GENERATED_KEYS);
        System.out.println("Inserted ID -" + preparedStatement.execute("INSERT INTO '"+ SITE +"' ('"+SITE_NAME+"') VALUES ('" + siteName1 + "'); "));
//        ResultSet rs = preparedStatement.executeQuery();
//        if (rs.next()) {
//            long id = rs.getLong(1);
//            System.out.println("Inserted ID -" + id); //display inserted record
//        }
        int siteID = preparedStatement.executeUpdate("INSERT INTO '"+ SITE +"' ('"+SITE_NAME+"') VALUES ('" + siteName1 + "'); ");
        System.out.println("    IS siteID: " + siteID);

//        preparedStatement = connection.prepareStatement("INSERT INTO '"+ JOB_INFORM +"' ('"+SITE_ID + "', '"+ PLACE + "', '"
//                        +COMPANY_NAME + "', '"+HEAD_PUBLICATION + "', '"+DATE_PUBLICATION + "', '"+IS_SEEN + "') VALUES ("
//                        + siteID +", '" +jobsInform.getPlace() + "', '"+jobsInform.getCompanyName() + "', '" +jobsInform.getHeadPublication() + "', '"
//                        +jobsInform.getPublishedDate() + "', " +(jobsInform.isSeen()? 1 : 0) + "); ",
//                Statement.RETURN_GENERATED_KEYS);
        int jobInformId = preparedStatement.executeUpdate("INSERT INTO '"+ JOB_INFORM +"' ('"+SITE_ID + "', '"+ PLACE + "', '"
                +COMPANY_NAME + "', '"+HEAD_PUBLICATION + "', '"+DATE_PUBLICATION + "', '"+IS_SEEN + "') VALUES ("
                + siteID +", '" +jobsInform.getPlace() + "', '"+jobsInform.getCompanyName() + "', '" +jobsInform.getHeadPublication() + "', '"
                +jobsInform.getPublishedDate() + "', " +(jobsInform.isSeen()? 1 : 0) + "); ");
        System.out.println("    IS jobInformId: " + jobInformId);

        for(ListImpl list: jobsInform.getOrder()) {
            if(list!=null) {
//                preparedStatement = connection.prepareStatement("INSERT INTO '" + DESCRIPTION + "' ('" + JOB_ID + "', '"
//                                + TEXT_FIELD + "', '" + LIST_HEADER + "') VALUES ("
//                                + jobInformId + ", '" + list.getTextFieldImpl() + "', '"
//                                + list.getListHeader() + "'); ",
//                        Statement.RETURN_GENERATED_KEYS);
                int listImplId = preparedStatement.executeUpdate("INSERT INTO '" + DESCRIPTION + "' ('" + JOB_ID + "', '"
                        + TEXT_FIELD + "', '" + LIST_HEADER + "') VALUES ("
                        + jobInformId + ", '" + list.getTextFieldImpl() + "', '"
                        + list.getListHeader() + "'); ");
                if(list.getListItem() != null && list.getListItem().size()>0) {
                    for (String s : list.getListItem()) {
                        boolean p = preparedStatement.execute("INSERT INTO '" + LIST + "' ('" + DESCRIPTION_ID + "', '"
                                + LIST_ITEM + "') VALUES ("
                                + listImplId + ", '" + s + "'); ");
                        System.out.println("    IS DONE: "+ listImplId + " " + p);
                    }
                }
            }
        }
        preparedStatement.close();
        connection.close();


//        System.out.println("Таблица заполнена");
    }

}
