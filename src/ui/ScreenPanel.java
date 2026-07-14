package ui;

import model.Contact;
import pages.*;
import repository.ContactRepository;
import repository.ContactRepositoryImpl;

import javax.swing.*;
import java.awt.*;

/**
 * 屏幕面板 —— 手机状态机
 *
 * 状态流转：
 *   OFF → LOCK → MENU → CONTACTS ↔ DETAIL ↔ ADD_EDIT
 */
public class ScreenPanel extends JPanel {

    private CardLayout cardLayout;

    // 页面
    private ScreenOffPage offPage;
    private LockScreenPage lockPage;
    private MenuPage menuPage;
    private HomePage contactsPage;
    private ContactDetailPage detailPage;
    private AddEditContactPage addEditPage;

    // 页面标识
    public static final String PAGE_OFF = "off";
    public static final String PAGE_LOCK = "lock";
    public static final String PAGE_MENU = "menu";
    public static final String PAGE_CONTACTS = "contacts";
    public static final String PAGE_DETAIL = "detail";
    public static final String PAGE_ADD_EDIT = "addEdit";

    // 当前状态
    private String currentState;
    private ContactRepository repository;

    public ScreenPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(400, 380));
        setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 3));
        this.repository = new ContactRepositoryImpl();

        // 创建所有页面
        offPage = new ScreenOffPage();
        lockPage = new LockScreenPage();
        menuPage = new MenuPage();
        contactsPage = new HomePage(this);
        detailPage = new ContactDetailPage();
        addEditPage = new AddEditContactPage();

        // 添加到 CardLayout
        add(offPage, PAGE_OFF);
        add(lockPage, PAGE_LOCK);
        add(menuPage, PAGE_MENU);
        add(contactsPage, PAGE_CONTACTS);
        add(detailPage, PAGE_DETAIL);
        add(addEditPage, PAGE_ADD_EDIT);

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
                if (lockPage != null) lockPage.reset();
                showPage(PAGE_LOCK);
                currentState = PAGE_LOCK;
                break;

            case PAGE_LOCK:
                if (lockPage.handleInput(keyCode)) {
                    menuPage.reset();
                    showPage(PAGE_MENU);
                    currentState = PAGE_MENU;
                }
                break;

            case PAGE_MENU:
                if ("CANCEL".equals(keyCode)) {
                    goToOff();
                    return;
                }
                menuPage.handleInput(keyCode);
                if (menuPage.isConfirmed()) {
                    String func = menuPage.getSelectedFunction();
                    if ("联系人".equals(func)) {
                        contactsPage.reloadContacts();
                        showPage(PAGE_CONTACTS);
                        currentState = PAGE_CONTACTS;
                    }
                    menuPage.reset();
                }
                break;

            case PAGE_CONTACTS:
                if ("CANCEL".equals(keyCode)) {
                    goToMenu();
                    return;
                }
                if ("HANGUP".equals(keyCode)) {
                    goToOff();
                    return;
                }
                // 先让 HomePage 处理
                contactsPage.handleKeyPress(keyCode);

                // 检查导航请求
                if (contactsPage.isGoToDetail()) {
                    Contact selected = contactsPage.getSelectedContact();
                    if (selected != null) {
                        detailPage.setContact(selected);
                        detailPage.reset();
                        showPage(PAGE_DETAIL);
                        currentState = PAGE_DETAIL;
                    }
                    contactsPage.resetNavFlags();
                } else if (contactsPage.isGoToAdd()) {
                    addEditPage.setContact(null);  // 新增模式
                    showPage(PAGE_ADD_EDIT);
                    currentState = PAGE_ADD_EDIT;
                    contactsPage.resetNavFlags();
                }
                break;

            case PAGE_DETAIL:
                detailPage.handleInput(keyCode);
                if (detailPage.isBackRequested()) {
                    // 返回列表
                    contactsPage.reloadContacts();
                    showPage(PAGE_CONTACTS);
                    currentState = PAGE_CONTACTS;
                } else if (detailPage.isDeleted()) {
                    // 删除成功，返回列表
                    contactsPage.reloadContacts();
                    showPage(PAGE_CONTACTS);
                    currentState = PAGE_CONTACTS;
                } else if (detailPage.isEditRequested()) {
                    // 进入编辑
                    addEditPage.setContact(detailPage.getContact());
                    showPage(PAGE_ADD_EDIT);
                    currentState = PAGE_ADD_EDIT;
                }
                break;

            case PAGE_ADD_EDIT:
                if ("CANCEL".equals(keyCode)) {
                    addEditPage.handleInput(keyCode);
                } else {
                    addEditPage.handleInput(keyCode);
                }

                if (addEditPage.isCancelled()) {
                    // 放弃 → 返回来源
                    contactsPage.reloadContacts();
                    showPage(PAGE_CONTACTS);
                    currentState = PAGE_CONTACTS;
                } else if (addEditPage.isSaved()) {
                    // 保存到数据库
                    Contact result = addEditPage.getResult();
                    if (result.getName().isEmpty() || result.getPhoneNumber().isEmpty()) {
                        // 字段为空，不保存
                        break;
                    }
                    if (addEditPage.isEditMode()) {
                        repository.update(result);
                        System.out.println("更新联系人: " + result.getName());
                    } else {
                        repository.insert(result);
                        System.out.println("新增联系人: " + result.getName());
                    }
                    contactsPage.reloadContacts();
                    showPage(PAGE_CONTACTS);
                    currentState = PAGE_CONTACTS;
                }
                break;
        }
    }

    private void showPage(String pageName) {
        cardLayout.show(this, pageName);
    }

    public void goToMenu() {
        menuPage.reset();
        showPage(PAGE_MENU);
        currentState = PAGE_MENU;
    }

    public void goToOff() {
        showPage(PAGE_OFF);
        currentState = PAGE_OFF;
    }

    public void addPage(JPanel panel, String name) {
        add(panel, name);
    }

    public String getCurrentState() {
        return currentState;
    }
}
