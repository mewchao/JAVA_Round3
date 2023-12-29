package test.java.com;

import src.main.com.example.OrderManagementSystem.OrderManagementSystem;
import src.main.com.example.db.JDBCUtils;
import src.main.com.example.model.Order;
import src.main.com.example.model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class Test {

    public static void main(String[] args) {

        OrderManagementSystem orderManagementSystem = new OrderManagementSystem();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 测试代码
        try {
            // 获取数据库连接
            Connection connection = JDBCUtils.getConnection();
            connection.setAutoCommit(false);

            // 添加商品
            Product product1 = new Product(1, "商品1", 10.0, new Date(), new Date());
            OrderManagementSystem.addProduct(connection, product1);

            // 添加订单
            Order order1 = new Order(1, 1, 10.0, 1, new Date(), new Date(), new Date());
            OrderManagementSystem.addOrder(connection, order1);

            // 要删除的商品ID
            int productId = 1;
            int orderId = 1;

            // 要删除的商品ID
//            OrderManagementSystem.deleteOrder(connection, orderId);
//            OrderManagementSystem.deleteProduct(connection, productId);

            // 查询商品
            Product retrievedProduct = OrderManagementSystem.getProduct(connection, 1);
            System.out.println(retrievedProduct);

            // 查询订单
            Order retrievedOrder = OrderManagementSystem.getOrder(connection, 1);
            System.out.println(retrievedOrder);

            // 要修改的订单ID
            int orderIdToUpdate = 1;
            // 新的订单价格
            double newPrices = 99.99;
            // 新的商品数量
            int newNums = 5;

            try {
                OrderManagementSystem.updateOrder(connection, orderIdToUpdate, newPrices, newNums);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // 要修改的商品ID 新的商品名称 新的商品价格
            int goodsIdToUpdate = 1;
            String newName = "新商品名称";
            double newPrice = 19.99;

            try {
                OrderManagementSystem.updateProduct(connection, goodsIdToUpdate, newName, newPrice);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // 关闭数据库连接
            JDBCUtils.close(connection, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
