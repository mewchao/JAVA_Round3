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
```
public class JDBCDemo {
public static void main(String[] args) throws Exception {   //下面方法有不同的异常，我直接抛出一个大的异常

        //1、导入驱动jar包
        //2、注册驱动
        Class.forName("com.mysql.jdbc.Driver");

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