package ui;

import pages.HomePage;

import javax.swing.*;
import java.awt.*;

/**
 * 屏幕面板 —— 手机上方显示区域
 * 使用 CardLayout 管理多个页面切换
 * 接收来自 KeypadPanel 的按键事件并转发到当前页面
 */
public class ScreenPanel extends JPanel {

    private CardLayout cardLayout;
    private HomePage homePage;

    public static final String HOME_PAGE = "home";

    public ScreenPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setBackground(new Color(30, 30, 50));
        setPreferredSize(new Dimension(400, 380));
        setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 3));

        // 添加主页
        homePage = new HomePage();
        add(homePage, HOME_PAGE);

        // 默认显示主页
        cardLayout.show(this, HOME_PAGE);
    }

    /**
     * 接收按键事件并转发到当前页面
     */
    public void dispatchKeyPress(String keyCode) {
        // 当前只有主页，直接转发
        homePage.handleKeyPress(keyCode);
    }

    /**
     * 切换到指定页面
     */
    public void showPage(String pageName) {
        cardLayout.show(this, pageName);
    }

    /**
     * 添加新页面
     */
    public void addPage(JPanel panel, String name) {
        add(panel, name);
    }

    public HomePage getHomePage() {
        return homePage;
    }
}
