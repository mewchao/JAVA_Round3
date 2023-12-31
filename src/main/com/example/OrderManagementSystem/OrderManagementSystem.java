package src.main.com.example.OrderManagementSystem;

import src.main.com.example.model.Order;
import src.main.com.example.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class OrderManagementSystem {
    /**
     * 添加商品
     *
     * @param connection 数据库连接对象
     * @param product    要添加的商品对象
     * @throws SQLException 如果在执行数据库操作时发生错误
     */
    public static void addProduct(Connection connection, Product product) throws SQLException {
        //关闭自动提交
        connection.setAutoCommit(false);
        //try 块中执行插入操作，将商品数据插入到数据库中。 connection.commit(); 提交事务，将之前的操作作为一个事务进行提交
        try {
            String sql = "INSERT INTO good_table (uk_id_goods, name_goods, price_goods, gmt_modified, gmt_create) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // 设置商品的参数
                statement.setInt(1, product.getUk_id_goods());
                statement.setString(2, product.getName_goods());
                statement.setDouble(3, product.getPrice_goods());
                statement.setDate(4, new java.sql.Date(product.getGmt_modified().getTime()));
                statement.setDate(5, new java.sql.Date(product.getGmt_create().getTime()));
                //执行插入操作
                statement.executeUpdate();
            }
            // 提交事务
            connection.commit();
        } catch (SQLException ex) {
            // 回滚事务
            connection.rollback();
            throw ex;
        }
    }


    /**
     * 添加订单
     *
     * @param connection 数据库连接对象
     * @param order      要添加的订单对象
     * @throws SQLException             如果在执行数据库操作时发生错误
     * @throws IllegalArgumentException 如果商品不存在、价格小于等于零或数量小于等于零
     */
    public static void addOrder(Connection connection, Order order) throws SQLException {
        // 关闭自动提交
        connection.setAutoCommit(false);
        try {
            if (!isProductExists(connection, order.getId_good())) {
                System.out.println("商品不存在");
                throw new IllegalArgumentException("商品不存在");
            }

            if (order.getPrices_order() <= 0) {
                System.out.println("价格必须大于零");
                throw new IllegalArgumentException("价格必须大于零");
            }

            if (order.getNums_good() <= 0) {
                System.out.println("数量必须大于零");
                throw new IllegalArgumentException("数量必须大于零");
            }

            String sql = "INSERT INTO order_project  (id_order, id_good, prices_order, nums_good, time_order, gmt_create, gmt_modified) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, order.getId_order());
                statement.setInt(2, order.getId_good());
                statement.setDouble(3, order.getPrices_order());
                statement.setInt(4, order.getNums_good());
                statement.setDate(5, new java.sql.Date(order.getTime_order().getTime()));
                statement.setDate(6, new java.sql.Date(order.getGmt_create().getTime()));
                statement.setDate(7, new java.sql.Date(order.getGmt_modified().getTime()));
                statement.executeUpdate();
            }
            // 提交事务
            connection.commit();
        } catch (SQLException ex) {
            // 回滚事务
            connection.rollback();
            throw ex;
        } finally {
            // 恢复自动提交状态
            connection.setAutoCommit(true);
        }
    }


    /**
     * 删除商品
     *
     * @param connection 数据库连接对象
     * @param productId  要删除的商品id
     * @throws SQLException 如果在执行数据库操作时发生错误
     */
    public static void deleteProduct(Connection connection, int productId) throws SQLException {
        // 关闭自动提交
        connection.setAutoCommit(false);

        try {
            // 检查商品是否存在
            if (!isProductExists(connection, productId)) {
                throw new IllegalArgumentException("商品不存在");
            }

            String sql = "DELETE FROM good_table WHERE uk_id_goods = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, productId);
                // 执行删除操作
                statement.executeUpdate();
            }

            connection.commit(); // 提交事务
        } catch (SQLException ex) {
            connection.rollback(); // 回滚事务
            throw ex;
        }
    }


    /**
     * 删除订单
     *
     * @param connection 数据库连接对象
     * @param orderId    要删除的订单对象的id
     * @throws SQLException             如果在执行数据库操作时发生错误
     * @throws IllegalArgumentException 订单不存在
     */
    public static void deleteOrder(Connection connection, int orderId) throws SQLException {
        // 关闭自动提交
        connection.setAutoCommit(false);
        try {
            // 检查订单是否存在
            if (!isOrderExists(connection, orderId)) {
                throw new IllegalArgumentException("订单不存在");
            }

            String sql = "DELETE FROM order_project WHERE id_order = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, orderId);
                // 执行删除操作
                statement.executeUpdate();
            }

            connection.commit(); // 提交事务
        } catch (SQLException ex) {
            connection.rollback(); // 回滚事务
            throw ex;
        }
    }

    /**
     * 查询订单是否存在
     *
     * @param connection 数据库连接对象
     * @param orderId    订单id
     * @return boolean 是否存在该订单
     * @throws SQLException 如果在执行数据库操作时发生错误
     */
    public static boolean isOrderExists(Connection connection, int orderId) throws SQLException {
        // 关闭自动提交
        connection.setAutoCommit(false);

        String sql = "SELECT COUNT(*) FROM order_project WHERE id_order = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // 设置查询参数
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // 获取结果集中的计数值，如果计数大于零，表示订单存在
                    int count = resultSet.getInt(1);
                    // 提交事务
                    connection.commit();
                    return count > 0;
                }
            }
        } catch (SQLException ex) {
            // 回滚事务
            connection.rollback();
            throw ex;
        }
        // 默认情况下，订单不存在
        return false;
    }

    /**
     * 查询商品是否存在
     *
     * @param connection 数据库连接对象
     * @param ukIdGoods  商品uk_id_goods
     * @return boolean 是否存在该商品
     * @throws SQLException 如果在执行数据库操作时发生错误
     */
    public static boolean isProductExists(Connection connection, int ukIdGoods) throws SQLException {
        // 关闭自动提交
        connection.setAutoCommit(false);
        String sql = "SELECT COUNT(*) FROM good_table WHERE uk_id_goods = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // 设置查询参数
            statement.setInt(1, ukIdGoods);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // 获取结果集中的计数值  如果计数大于零，表示商品存在
                    int count = resultSet.getInt(1);
                    // 提交事务
                    connection.commit();
                    return count > 0;
                }
            }
        } catch (SQLException ex) {
            // 回滚事务
            connection.rollback();
            throw ex;
        }
        // 默认情况下，商品不存在
        return false;
    }

    /**
     * 根据订单ID查询订单
     *
     * @param connection 数据库连接
     * @param orderId    订单ID
     * @return 订单对象，如果不存在则返回null
     * @throws SQLException 如果查询过程中发生数据库异常
     */
    public static Order getOrder(Connection connection, int orderId) throws SQLException {
        // 关闭自动提交
        connection.setAutoCommit(false);
        String sql = "SELECT id_order, id_good, prices_order, nums_good, time_order, gmt_create, gmt_modified FROM order_project WHERE id_order = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id_order = resultSet.getInt("id_order");
                    int id_good = resultSet.getInt("id_good");
                    double prices_order = resultSet.getDouble("prices_order");
                    int nums_good = resultSet.getInt("nums_good");
                    Date time_order = resultSet.getDate("time_order");
                    Date gmt_create = resultSet.getDate("gmt_create");
                    Date gmt_modified = resultSet.getDate("gmt_modified");
                    Order order = new Order(id_order, id_good, prices_order, nums_good, time_order, gmt_create, gmt_modified);
                    // 提交事务
                    connection.commit();
                    return order;
                }
            }
        } catch (SQLException ex) {
            // 回滚事务
            connection.rollback();
            throw ex;
        }
        return null;
    }


    /**
     * 根据商品ID查询商品
     *
     * @param connection 数据库连接
     * @param goodsId    商品ID
     * @return 商品对象，如果不存在则返回null
     * @throws SQLException 如果查询过程中发生数据库异常
     */
    public static Product getProduct(Connection connection, int goodsId) throws SQLException {
        // 关闭自动提交
        connection.setAutoCommit(false);
        String sql = "SELECT uk_id_goods, name_goods, price_goods, gmt_modified, gmt_create FROM good_table WHERE uk_id_goods = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, goodsId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int uk_id_goods = resultSet.getInt("uk_id_goods");
                    String name_goods = resultSet.getString("name_goods");
                    double price_goods = resultSet.getDouble("price_goods");
                    Date gmt_modified = resultSet.getDate("gmt_modified");
                    Date gmt_create = resultSet.getDate("gmt_create");
                    Product product = new Product(uk_id_goods, name_goods, price_goods, gmt_modified, gmt_create);
                    // 提交事务
                    connection.commit();
                    return product;
                }
            }
        } catch (SQLException ex) {
            // 回滚事务
            connection.rollback();
            throw ex;
        }
        return null;
    }

    /**
     * 根据商品ID修改商品信息
     *
     * @param connection 数据库连接
     * @param goodsId    商品ID
     * @param newName    新的商品名称
     * @param newPrice   新的商品价格
     * @throws SQLException 如果修改过程中发生数据库异常
     */
    public static void updateProduct(Connection connection, int goodsId, String newName, double newPrice) throws SQLException {
        // 关闭自动提交
        connection.setAutoCommit(false);
        String sql = "UPDATE good_table SET name_goods = ?, price_goods = ? WHERE uk_id_goods = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newName);
            statement.setDouble(2, newPrice);
            statement.setInt(3, goodsId);
            int rowsAffected = statement.executeUpdate();

            // 根据受影响的行数判断修改是否成功
            if (rowsAffected > 0) {
                // 提交事务
                connection.commit();
                System.out.println("商品信息修改成功！");
            } else {
                System.out.println("未找到对应的商品，修改失败！");
            }
        } catch (SQLException ex) {
            // 回滚事务
            connection.rollback();
            throw ex;
        }
    }

    /**
     * 根据订单ID修改订单信息
     *
     * @param connection 数据库连接
     * @param orderId    订单ID
     * @param newPrices  新的订单价格
     * @param newNums    新的商品数量
     * @throws SQLException 如果修改过程中发生数据库异常
     */
    public static void updateOrder(Connection connection, int orderId, double newPrices, int newNums) throws SQLException {
        // 关闭自动提交
        connection.setAutoCommit(false);
        String sql = "UPDATE order_project SET prices_order = ?, nums_good = ? WHERE id_order = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, newPrices);
            statement.setInt(2, newNums);
            statement.setInt(3, orderId);
            int rowsAffected = statement.executeUpdate();

            // 根据受影响的行数判断修改是否成功
            if (rowsAffected > 0) {
                // 提交事务
                connection.commit();
                System.out.println("订单信息修改成功！");
            } else {
                System.out.println("未找到对应的订单，修改失败！");
            }
        } catch (SQLException ex) {
            // 回滚事务
            connection.rollback();
            throw ex;
        }
    }


}