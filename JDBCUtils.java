import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUtils {
    private static final String URL = "jdbc:mysql://localhost:3306/work3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 执行增删改操作
    public static int executeUpdate(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
        return statement.executeUpdate();
    }

    // 执行查询操作
    public static ResultSet executeQuery(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
        return statement.executeQuery();
    }

    // 开启事务
    public static void beginTransaction(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
    }

    // 提交事务
    public static void commitTransaction(Connection connection) throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    // 回滚事务
    public static void rollbackTransaction(Connection connection) throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }
}