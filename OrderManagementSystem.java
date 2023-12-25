import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderManagementSystem {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        /**
         * 修改框架线程池的核心参数
         *
         * @param threadPoolBaseInfo 修改线程池的基础参数
         * @return 线程池核心参数修改结果
         */
        // 测试代码
        try {
            // 获取数据库连接
            Connection connection = JDBCUtils.getConnection();

            // 添加商品
            Product product1 = new Product(1, "商品1", 10.0, new Date(), new Date());
            addProduct(connection, product1);

            // 添加订单
            List<Product> products = new ArrayList<>();
            products.add(product1);
            Order order1 = new Order(1, 1, 10.0, 1, new Date(), new Date(), new Date());
            addOrder(connection, order1);

            // 查询订单
            Order retrievedOrder = getOrder(connection, 1);
            System.out.println(retrievedOrder);

            // 更新订单价格
            updateOrderPrice(connection, 1, 15.0);
            retrievedOrder = getOrder(connection, 1);
            System.out.println(retrievedOrder);

            // 删除订单中的商品
            removeProductFromOrder(connection, 1, 1);
            retrievedOrder = getOrder(connection, 1);
            System.out.println(retrievedOrder);

            // 关闭数据库连接
            JDBCUtils.close(connection, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 添加商品
     *
     * @param
     * @return
     */
    public static void addProduct(Connection connection, Product product) throws SQLException {
        String sql = "INSERT INTO products (id, name, price, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, product.getUk_id_goods());
            statement.setString(2, product.getName_goods());
            statement.setDouble(3, product.getPrice_goods());
            statement.setDate(4, new java.sql.Date(product.getGmt_modified().getTime()));
            statement.setDate(5, new java.sql.Date(product.getGmt_create().getTime()));
            statement.executeUpdate();
        }
    }
    /**
     *
     *
     * @param
     * @return
     */
    // 添加订单
    public static void addOrder(Connection connection, Order order) throws SQLException {
        // 检查商品是否存在
        if (!isProductExists(connection, order.getId_good())) {
            throw new IllegalArgumentException("商品不存在");
        }

        // 检查价格是否合法
        if (order.getPrices_order() <= 0) {
            throw new IllegalArgumentException("价格必须大于零");
        }

        // 检查数量是否合法
        if (order.getNums_good() <= 0) {
            throw new IllegalArgumentException("数量必须大于零");
        }

        String sql = "INSERT INTO orders (id, id_good, prices_order, nums_good, time_order, gmt_create, gmt_modified) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
        String sql = "SELECT COUNT(*) FROM good_table WHERE uk_id_goods = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ukIdGoods);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    /**
     * 查询订单
     *
     * @param
     * @return
     */
    public static Order getOrder(Connection connection, int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id_order = resultSet.getInt("id");
                    int id_good = resultSet.getInt("id_good");
                    double prices_order = resultSet.getDouble("prices_order");
                    int nums_good = resultSet.getInt("nums_good");
                    Date time_order = resultSet.getDate("time_order");
                    Date gmt_create = resultSet.getDate("gmt_create");
                    Date gmt_modified = resultSet.getDate("gmt_modified");
                    return new Order(id_order, id_good, prices_order, nums_good, time_order, gmt_create, gmt_modified);
                }
            }
        }
        return null;
    }
    /**
     * 更新订单价格
     *
     * @param
     * @return
     */
    public static void updateOrderPrice(Connection connection, int orderId, double newPrice) throws SQLException {
        String sql = "UPDATE orders SET prices_order = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, newPrice);
            statement.setInt(2, orderId);
            statement.executeUpdate();
        }
    }
    /**
     * 从订单中删除商品
     *
     * @param
     * @return
     */
    public static void removeProductFromOrder(Connection connection, int orderId, int productId) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ? AND id_good = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        }
    }
}