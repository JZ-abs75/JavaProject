import db.DatabaseManager;
import ui.PhoneFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 初始化数据库
        DatabaseManager.initDatabase();

        // 使用 Swing 事件分发线程启动 GUI
        SwingUtilities.invokeLater(() -> {
            PhoneFrame frame = new PhoneFrame();
            frame.setVisible(true);
        });

        // 程序退出时关闭数据库连接
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseManager.closeConnection();
        }));
    }
}
