package db;

import java.sql.*;

/**
 * 数据库连接管理器
 * 管理 MySQL 数据库连接
 */
public class DatabaseManager {

    // 连接mySQL
    private static final String URL = "jdbc:mysql://localhost:3306/database1";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    private static Connection connection;

    /**
     * 获取数据库连接（单例）
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // 加载 MySQL 驱动
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL 驱动未找到，请检查 lib/mysql-connector-j-8.3.0.jar", e);
            }
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    /**
     * 初始化数据库表（首次运行时调用）
     */
    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // 创建 contacts 表
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS contacts (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  name VARCHAR(50) NOT NULL," +
                "  phone_number VARCHAR(20) NOT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            );

            // 每次启动清空并重新插入示例数据
            stmt.execute("DELETE FROM contacts");
            stmt.execute("INSERT INTO contacts (name, phone_number) VALUES " +
                "('中分蔡徐坤', '12345678900')," +
                "('俄罗斯米塔', '12345678901')," +
                "('香港刘德华', '12345678902')," +
                "('常山赵子龙', '12345678903')," +
                "('美国马斯克', '12345678904')," +
                "('花果山猴王', '12345678905')," +
                "('狗熊岭熊大', '12345678906')," +
                "('河北刘华强', '12345678907')," +
                "('橄榄球僵尸', '12345678908')"
            );
            System.out.println("数据库初始化成功！");

        } catch (SQLException e) {
            System.err.println("数据库初始化失败: " + e.getMessage());
        }
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("数据库连接已关闭。");
            }
        } catch (SQLException e) {
            System.err.println("关闭连接失败: " + e.getMessage());
        }
    }
}
