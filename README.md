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



### [MEANS]
* 记录以下信息:
  * 商品: 商品编号、商品名、商品价格
  * 订单: 订单编号、商品信息(考虑如何合理存储关联信息)、下单时间、订单价格
* 将信息保存至Mysql数据库
* JDBC工具类实现商品和订单信息的增删改查、更新，商品和订单排序（价格、下单时间）等功能
  * 在创建订单时，实施数据验证，确保订单信息的完整性和准确性。例如，检查商品是否存在，价格是否合法等等。
  * 如果想要删除已经存在在订单中的商品，你要怎么处理？
  * 避免使用SELECT *
* JDBC工具类，功能包括：
  * 处理数据库连接
  * 执行增删查改操作
  * 解决SQL注入问题
    * 参数化查询，正确地处理和转义输入值，从而防止 SQL 注入攻击。每个占位符都使用了相应的 set 方法来设置参数的值
  * 添加事务管理
    * 调用 connection.setAutoCommit(false) 来关闭自动提交模式。默认情况下，每个 SQL 语句都会自动提交到数据库，但在事务管理中，我们希望手动控制提交和回滚。 
    * 在 try 块中执行插入操作。插入操作通过 PreparedStatement 对象执行，参数化查询的方式防止 SQL 注入攻击。 
    * 在插入操作的 try 块中，如果一切正常，调用 connection.commit() 提交事务。这将确保之前的插入操作作为一个事务进行提交。 如果在插入操作期间发生了异常（例如，SQLException 异常），则会进入 catch 块。在 catch 块中，调用 connection.rollback() 进行事务回滚，将之前的操作撤销
  * 包含异常处理和资源释放
      * 异常处理：
        在方法签名中声明了 throws SQLException，表示该方法可能会抛出 SQLException 异常。
        在 try 块中执行数据库操作，如果在执行期间发生了 SQLException 异常，则会进入 catch 块。
        在 catch 块中，调用 connection.rollback() 进行事务回滚，将之前的操作撤销。
        最后，通过 throw ex 将捕获的异常重新抛出，将异常传递给调用方处理。
      * 资源释放：
```
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



### [NOTES]
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