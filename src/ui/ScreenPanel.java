package ui;

import pages.HomePage;
import pages.LockScreenPage;
import pages.MenuPage;
import pages.ScreenOffPage;

import javax.swing.*;
import java.awt.*;

/**
 * 屏幕面板 —— 手机状态机
 *
 * 状态流转：
 *   OFF ──(任意键)──> LOCK ──(#→确认)──> MENU ──(选功能+确认)──> CONTACTS
 *                     │ 按错提示              │ 取消回 OFF
 *                     └── 显示正确操作提示     │ 左右循环选功能
 *
 * 使用 CardLayout 管理 4 个页面
 */
public class ScreenPanel extends JPanel {

    private CardLayout cardLayout;

    // 四个页面
    private ScreenOffPage offPage;
    private LockScreenPage lockPage;
    private MenuPage menuPage;
    private HomePage contactsPage;

    // 页面标识
    public static final String PAGE_OFF = "off";
    public static final String PAGE_LOCK = "lock";
    public static final String PAGE_MENU = "menu";
    public static final String PAGE_CONTACTS = "contacts";

    // 当前状态
    private String currentState;

    public ScreenPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(400, 380));
        setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 3));

        // 创建四个页面
        offPage = new ScreenOffPage();
        lockPage = new LockScreenPage();
        menuPage = new MenuPage();
        contactsPage = new HomePage(this);  // 传入 ScreenPanel 引用，用于返回导航

        // 添加到 CardLayout
        add(offPage, PAGE_OFF);
        add(lockPage, PAGE_LOCK);
        add(menuPage, PAGE_MENU);
        add(contactsPage, PAGE_CONTACTS);

        // 初始状态：黑屏
        currentState = PAGE_OFF;
        cardLayout.show(this, PAGE_OFF);
    }

    /**
     * 接收按键事件 —— 状态机核心
     */
    public void dispatchKeyPress(String keyCode) {
        switch (currentState) {
            case PAGE_OFF:
                // 任意键 → 亮屏锁定
                showPage(PAGE_LOCK);
                currentState = PAGE_LOCK;
                lockPage.reset();
                break;

            case PAGE_LOCK:
                // 转发给解锁页处理
                boolean unlocked = lockPage.handleInput(keyCode);
                if (unlocked) {
                    menuPage.reset();
                    showPage(PAGE_MENU);
                    currentState = PAGE_MENU;
                }
                break;

            case PAGE_MENU:
                // 转发给菜单页处理
                if ("CANCEL".equals(keyCode)) {
                    // 取消 → 回黑屏
                    goToOff();
                    return;
                }
                menuPage.handleInput(keyCode);
                if (menuPage.isConfirmed()) {
                    String func = menuPage.getSelectedFunction();
                    if ("联系人".equals(func)) {
                        showPage(PAGE_CONTACTS);
                        currentState = PAGE_CONTACTS;
                    }
                    // 其他功能暂未实现，留在菜单页
                    menuPage.reset();
                }
                break;

            case PAGE_CONTACTS:
                // 转发给联系人页处理
                contactsPage.handleKeyPress(keyCode);
                break;
        }
    }

    /**
     * 切换到指定页面
     */
    private void showPage(String pageName) {
        cardLayout.show(this, pageName);
    }

    /**
     * 返回菜单页（从联系人页调用）
     */
    public void goToMenu() {
        menuPage.reset();
        showPage(PAGE_MENU);
        currentState = PAGE_MENU;
    }

    /**
     * 返回黑屏（从菜单页或联系人页调用）
     */
    public void goToOff() {
        showPage(PAGE_OFF);
        currentState = PAGE_OFF;
    }

    /**
     * 添加新页面（预留扩展）
     */
    public void addPage(JPanel panel, String name) {
        add(panel, name);
    }

    public String getCurrentState() {
        return currentState;
    }
}
