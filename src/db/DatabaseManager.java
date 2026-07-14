package db;

import java.sql.*;

/**
 * 数据库连接管理器
 * 管理 MySQL 数据库连接
 */
public class DatabaseManager {

    // MySQL 连接参数（根据你的环境修改）
    private static final String URL = "jdbc:mysql://localhost:3306/database1";
    private static final String USER = "root";
    private static final String PASSWORD = "#Jzx201875";

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

            // 如果表为空，插入示例数据
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM contacts");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO contacts (name, phone_number) VALUES " +
                    "('张三', '13800138001')," +
                    "('李四', '13900139002')," +
                    "('王五', '13700137003')," +
                    "('赵六', '13600136004')," +
                    "('钱七', '13500135005')," +
                    "('孙八', '13400134006')," +
                    "('周九', '13300133007')," +
                    "('吴十', '13200132008')"
                );
                System.out.println("数据库已初始化，插入了示例联系人。");
            }

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
