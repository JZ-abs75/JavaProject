package pages;

import model.Contact;
import ui.ScreenPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * 联系人列表页面
 * 只能通过下方按键操作，不支持鼠标点击
 * 上/下：循环翻页（首尾衔接）
 * 取消：返回菜单
 * 挂断：返回黑屏
 */
public class HomePage extends JPanel {

    private DefaultListModel<String> listModel;
    private JList<String> contactList;
    private List<Contact> contacts;
    private JLabel statusLabel;
    private ScreenPanel screenPanel;

    public HomePage(ScreenPanel screenPanel) {
        this.screenPanel = screenPanel;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 50));
        setBorder(new EmptyBorder(10, 15, 10, 15));

        // ---- 顶部标题栏 ----
        JPanel titleBar = createTitleBar();
        add(titleBar, BorderLayout.NORTH);

        // ---- 中间联系人列表 ----
        contacts = Contact.getSampleContacts();
        listModel = new DefaultListModel<>();
        for (Contact c : contacts) {
            listModel.addElement(formatContact(c));
        }

        contactList = new JList<>(listModel);
        contactList.setBackground(new Color(30, 30, 50));
        contactList.setForeground(Color.WHITE);
        contactList.setFont(new Font("Microsoft YaHei", Font.PLAIN, 18));
        contactList.setSelectionBackground(new Color(50, 100, 180));
        contactList.setSelectionForeground(Color.WHITE);
        contactList.setFixedCellHeight(45);
        contactList.setBorder(new EmptyBorder(5, 5, 5, 5));

        // 禁用鼠标交互 — 只能用按键操作
        contactList.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(contactList);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(30, 30, 50));
        add(scrollPane, BorderLayout.CENTER);

        // ---- 底部状态栏 ----
        statusLabel = new JLabel("共 " + contacts.size() + " 个联系人  |  上下浏览  确认查看  取消返回", SwingConstants.CENTER);
        statusLabel.setForeground(new Color(150, 150, 150));
        statusLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        statusLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        add(statusLabel, BorderLayout.SOUTH);

        // 默认选中第一个
        contactList.setSelectedIndex(0);
    }

    /**
     * 处理来自下方按键的输入（唯一操作入口）
     */
    public void handleKeyPress(String keyCode) {
        int currentIndex = contactList.getSelectedIndex();
        int maxIndex = listModel.size() - 1;

        switch (keyCode) {
            case "UP":
                // 循环：第一个往上 → 跳到最后一个
                if (currentIndex > 0) {
                    contactList.setSelectedIndex(currentIndex - 1);
                    contactList.ensureIndexIsVisible(currentIndex - 1);
                } else {
                    contactList.setSelectedIndex(maxIndex);
                    contactList.ensureIndexIsVisible(maxIndex);
                }
                updateStatus("上: " + contacts.get(contactList.getSelectedIndex()).getName());
                break;

            case "DOWN":
                // 循环：最后一个往下 → 跳到第一个
                if (currentIndex < maxIndex) {
                    contactList.setSelectedIndex(currentIndex + 1);
                    contactList.ensureIndexIsVisible(currentIndex + 1);
                } else {
                    contactList.setSelectedIndex(0);
                    contactList.ensureIndexIsVisible(0);
                }
                updateStatus("下: " + contacts.get(contactList.getSelectedIndex()).getName());
                break;

            case "OK":
                Contact selected = getSelectedContact();
                if (selected != null) {
                    updateStatus("已选择: " + selected.getName());
                }
                break;

            case "CANCEL":
                // 返回菜单
                updateStatus("返回菜单");
                if (screenPanel != null) {
                    screenPanel.goToMenu();
                }
                break;

            case "CALL":
                Contact toCall = getSelectedContact();
                if (toCall != null) {
                    updateStatus("正在呼叫 " + toCall.getName() + " " + toCall.getPhoneNumber());
                }
                break;

            case "HANGUP":
                // 返回黑屏
                updateStatus("挂断");
                if (screenPanel != null) {
                    screenPanel.goToOff();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 创建标题栏
     */
    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(20, 20, 40));
        titleBar.setBorder(new EmptyBorder(8, 10, 8, 10));

        JLabel titleLabel = new JLabel("联系人", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 22));
        titleBar.add(titleLabel, BorderLayout.CENTER);

        return titleBar;
    }

    /**
     * 格式化联系人显示
     */
    private String formatContact(Contact c) {
        return "  " + c.getName() + "        " + c.getPhoneNumber();
    }

    /**
     * 获取当前选中的联系人
     */
    public Contact getSelectedContact() {
        int index = contactList.getSelectedIndex();
        if (index >= 0 && index < contacts.size()) {
            return contacts.get(index);
        }
        return null;
    }

    /**
     * 更新状态栏文字
     */
    private void updateStatus(String msg) {
        statusLabel.setText("共 " + contacts.size() + " 个联系人  |  " + msg);
    }
}
