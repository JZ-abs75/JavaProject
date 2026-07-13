package ui;

import javax.swing.*;
import java.awt.*;

/**
 * 主窗口 —— 模拟老年机外形
 * 布局：上方 ScreenPanel（屏幕）+ 下方 KeypadPanel（按键）
 * 屏幕只能通过下方按键操作
 */
public class PhoneFrame extends JFrame {

    private ScreenPanel screenPanel;
    private KeypadPanel keypadPanel;

    public PhoneFrame() {
        // 窗口基本设置
        setTitle("老年机 - 联系人");
        setSize(400, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // 整体布局：上屏幕 + 下按键
        setLayout(new BorderLayout());

        // 先创建屏幕
        screenPanel = new ScreenPanel();
        add(screenPanel, BorderLayout.CENTER);

        // 再创建按键，传入屏幕引用以实现按键操控屏幕
        keypadPanel = new KeypadPanel(screenPanel);
        add(keypadPanel, BorderLayout.SOUTH);

        getContentPane().setBackground(new Color(40, 40, 40));
    }

    public ScreenPanel getScreenPanel() {
        return screenPanel;
    }

    public KeypadPanel getKeypadPanel() {
        return keypadPanel;
    }
}
