package guru.oso.jmeter.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by BC on 1/8/17.
 */
public class MySQLUtils {

    public static void closeQuietly(ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqlEx) {
            } // ignore

            rs = null;
        }

    }

    public static void closeQuietly(Statement stmt) {

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqlEx) {
            } // ignore

            stmt = null;
        }

    }

    public static void closeQuietly(Connection conn) {

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException sqlEx) {
            } // ignore

            conn = null;
        }

    }

}
