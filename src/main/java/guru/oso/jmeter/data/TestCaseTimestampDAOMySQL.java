package guru.oso.jmeter.data;

import guru.oso.jmeter.util.MySQLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BC on 1/7/17.
 */
public class TestCaseTimestampDAOMySQL implements TestCaseTimestampDAO {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseTimestampDAOMySQL.class);

    private static final String MESSAGE_NUMBER = "message_number";
    private static final String MESSAGE_TYPE = "message_type";
    private static final String MESSAGE_START_TIME = "message_start_time";
    private static final String MESSAGE_END_TIME = "message_end_time";

    private static final String DATABASE_NAME = "performance_test";
    private static final String TABLE_NAME = "test_case";

    private String connectionURI;

    public TestCaseTimestampDAOMySQL(final String host) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

            StringBuilder dbBuilder = new StringBuilder("jdbc:mysql://");
            dbBuilder.append(host + "/" + DATABASE_NAME);
            dbBuilder.append("?user=root");
//            dbBuilder.append("&password=");
            dbBuilder.append("&autoReconnect=true");
            dbBuilder.append("&useSSL=false");

            this.connectionURI = dbBuilder.toString();

        } catch (Exception e) {
            logger.error("Big time failure!", e);
        }
//        conn = DriverManager.getConnection("jdbc:mysql://localhost/test?" + "user=minty&password=greatsqldb");

        //jdbc:mysql://104.154.236.96/performance_test?user=root&useSSL=false&autoReconnect=true


    }

    public TestCaseTimestampDAOMySQL(final String host, final String usr, final String pwd) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

            StringBuilder dbBuilder = new StringBuilder("jdbc:mysql://");
            dbBuilder.append(host + "/" + DATABASE_NAME);
            dbBuilder.append("?user=" + usr);
            dbBuilder.append("&password=" + pwd);

            this.connectionURI = dbBuilder.toString();

        } catch (Exception e) {
            logger.error("Big time failure!", e);
        }


    }

    @Override
    public List<TestCaseTimestamp> getAllTestCases() {

        List<TestCaseTimestamp> timestamps = new ArrayList<>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {

            conn = DriverManager.getConnection(this.connectionURI);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + TABLE_NAME);

            while(rs.next()) {
                TestCaseTimestamp timestamp = new RealTestCaseTimestamp();
                timestamp.setMessageNumber(rs.getString(MESSAGE_NUMBER));
                timestamp.setMessageType(rs.getString(MESSAGE_TYPE));
                timestamp.setStartTime(rs.getLong(MESSAGE_START_TIME));
                timestamp.setEndTime(rs.getLong(MESSAGE_END_TIME));
                timestamps.add(timestamp);
            }

        } catch (SQLException ex) {

            logger.error("SQLException!", ex);
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());

        } finally {

            MySQLUtils.closeQuietly(rs);
            MySQLUtils.closeQuietly(stmt);
            MySQLUtils.closeQuietly(conn);

        }

        return timestamps;

    }

    @Override
    public void insertTestCase(TestCaseTimestamp ts) {

        Connection conn = null;
        Statement stmt = null;

        try {

            conn = DriverManager.getConnection(this.connectionURI);
            stmt = conn.createStatement();

            StringBuilder updateBuilder = new StringBuilder("INSERT INTO " + TABLE_NAME);
            updateBuilder.append(" (" + MESSAGE_NUMBER + "," + MESSAGE_TYPE + "," + MESSAGE_START_TIME + "," + MESSAGE_END_TIME + ")");
            updateBuilder.append(" VALUES ('" + ts.getMessageNumber() + "','" + ts.getMessageType() + "'," + ts.getStartTime() + "'," + ts.getEndTime() + ")");

            logger.info("Insert: " + updateBuilder.toString());

            stmt.executeUpdate(updateBuilder.toString());

        } catch (SQLException ex) {

            logger.error("SQLException!", ex);
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());

        } finally {

            MySQLUtils.closeQuietly(stmt);
            MySQLUtils.closeQuietly(conn);

        }

    }

    @Override
    public void dropAllTestCases() {

        Connection conn = null;
        Statement stmt = null;

        try {

            conn = DriverManager.getConnection(this.connectionURI);
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM " + TABLE_NAME);

        } catch (SQLException ex) {

            logger.error("SQLException!", ex);
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());

        } finally {

            MySQLUtils.closeQuietly(stmt);
            MySQLUtils.closeQuietly(conn);

        }

    }

    @Override
    public TestCaseTimestamp findTestCase(String messageNumber) {

        TestCaseTimestamp timestamp = null;

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {

            conn = DriverManager.getConnection(this.connectionURI);
            stmt = conn.createStatement();

            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM " + TABLE_NAME);
            queryBuilder.append(" WHERE " + MESSAGE_NUMBER + "=" + "'" + messageNumber + "'");

            logger.info("Select: " + queryBuilder.toString());

            rs = stmt.executeQuery(queryBuilder.toString());

            if (rs.isBeforeFirst() ) {

                while (rs.next()) {
                    timestamp = new RealTestCaseTimestamp();
                    timestamp.setMessageNumber(rs.getString(MESSAGE_NUMBER));
                    timestamp.setMessageType(rs.getString(MESSAGE_TYPE));
                    timestamp.setStartTime(rs.getLong(MESSAGE_START_TIME));
                    timestamp.setEndTime(rs.getLong(MESSAGE_END_TIME));
                }

            }

        } catch (SQLException ex) {

            logger.error("SQLException!", ex);
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());

        } finally {

            MySQLUtils.closeQuietly(rs);
            MySQLUtils.closeQuietly(stmt);
            MySQLUtils.closeQuietly(conn);

        }

        if (timestamp == null) {
            timestamp = new NullTestCaseTimeStamp();
        }

        return timestamp;

    }

}
