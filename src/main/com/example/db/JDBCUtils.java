package src.main.com.example.db;

import java.sql.*;


public class JDBCUtils {
    private static final String URL = "jdbc:mysql://localhost:3306/work3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    /**
     * 获取数据库连接
     *
     * @return 数据库连接对象
     * @throws SQLException 如果获取连接失败
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * 关闭数据库连接、PreparedStatement 和 ResultSet
     *
     * @param connection 数据库连接对象
     * @param statement  PreparedStatement 对象
     * @param resultSet  ResultSet 对象
     */
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

    /**
     * 执行增删改操作
     *
     * @param connection 数据库连接对象
     * @param sql        SQL语句
     * @param params     SQL参数
     * @return 受影响的行数
     * @throws SQLException 如果执行操作失败
     */
    public static int executeUpdate(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
        return statement.executeUpdate();
    }

    /**
     * 执行查询操作
     *
     * @param connection 数据库连接对象
     * @param sql        SQL语句
     * @param params     SQL参数
     * @return 查询结果集
     * @throws SQLException 如果执行查询失败
     */
    public static ResultSet executeQuery(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
        return statement.executeQuery();
    }

    /**
     * 开启事务
     *
     * @param connection 数据库连接对象
     * @throws SQLException 如果开启事务失败
     */
    public static void beginTransaction(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
    }

    /**
     * 提交事务
     *
     * @param connection 数据库连接对象
     * @throws SQLException 如果提交事务失败
     */
    public static void commitTransaction(Connection connection) throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    /**
     * 回滚事务
     *
     * @param connection 数据库连接对象
     * @throws SQLException 如果回滚事务失败
     */
    public static void rollbackTransaction(Connection connection) throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }
}