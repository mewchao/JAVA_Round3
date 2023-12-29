### 项目框架
```
src
├── main
│   ├── java
│       └── com
│           └── example                         
│               ├── db                     // 存放与数据库相关的类或工具
│               │   └── JDBCUtils.java     // JDBC操作工具类
│               ├── model                  // 存放实体类
│               │    ├── Order.java         // 订单实体类
│               │    ├── OrderManager.java   // 订单管理类
│               │    └── Product.java       // 商品实体类
│               ├── OrderManagementSystem.java   // OrderManagementSystem模块
│ 
└── test
    ├── java
        └── com
            └── example
                └── Test.java               // 测试类
```
### [DEMOS]
```
public class JDBCDemo {
public static void main(String[] args) throws Exception {   

        //1、导入驱动jar包
        //2、注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        //3、获取数据库的连接对象
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "123456789");

        //4、定义sql语句
        String sql = "update Student set score = 99 where id = '10003' ";

        //5、获取执行sql语句的对象
        Statement stat = con.createStatement();

        //6、执行sql并接收返回结果
        int count = stat.executeUpdate(sql);

        //7、处理结果
        System.out.println(count);

        //8、释放资源
        stat.close();
        con.close();
    }
}
```
### [KNOWS]
##### 事务的概念
* 事务可以确保一系列的数据库操作要么全部成功执行，要么全部失败回滚，以保持数据库的一致性和完整性。    

#####  ACID 特性：
* 原子性（Atomicity）：事务是原子的，它要么全部成功执行，要么全部失败回滚。如果其中一个操作失败，整个事务都将失败，不会留下部分修改。
* 一致性（Consistency）：事务在执行前后，数据库从一个一致的状态转移到另一个一致的状态。这意味着事务必须遵循数据库的完整性约束，如主键、唯一性约束等。
* 隔离性（Isolation）：多个事务可以并发执行，但彼此之间不能互相干扰。一个事务的修改在提交之前对其他事务是不可见的。
* 持久性（Durability）：一旦事务提交成功，它的结果将永久保存在数据库中，即使系统发生故障也不会丢失。


##### 提交事务和回滚事务
* 在 JDBC 中，要提交事务，可以使用 commit() 方法，如上面的示例所示。提交事务后，其中的所有操作将成为数据库的一部分。
* 如果在事务过程中出现了问题，您可以使用 rollback() 方法来回滚事务，撤销所有未提交的更改，将数据库恢复到事务开始之前的状态


### [BUGS]
报错java.sql.SQLNonTransientConnectionException: Can't call rollback when autocommit=true
```
try {
// 设置自动提交为false
connection.setAutoCommit(false);
} catch (SQLException ex) {
// 回滚事务
connection.rollback();
throw ex;
} finally {
// 不再恢复自动提交状态
// connection.setAutoCommit(true);
}
```
因为在没有异常的情况下，connection.commit()已经提交了事务，而手动设置自动提交为true可能导致冲突