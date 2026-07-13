package pages;

import model.Contact;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * 默认主页 —— 联系人列表页面
 * 只能通过下方按键操作，不支持鼠标点击
 */
public class HomePage extends JPanel {

    private DefaultListModel<String> listModel;
    private JList<String> contactList;
    private List<Contact> contacts;
    private JLabel statusLabel;

    public HomePage() {
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
        contactList.setEnabled(false);  // 禁止鼠标点击选择

        JScrollPane scrollPane = new JScrollPane(contactList);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(30, 30, 50));
        add(scrollPane, BorderLayout.CENTER);

        // ---- 底部状态栏 ----
        statusLabel = new JLabel("共 " + contacts.size() + " 个联系人  |  按▲▼浏览  确认选择", SwingConstants.CENTER);
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
                if (currentIndex > 0) {
                    contactList.setSelectedIndex(currentIndex - 1);
                    contactList.ensureIndexIsVisible(currentIndex - 1);
                    updateStatus("▲ 上一个");
                }
                break;

            case "DOWN":
                if (currentIndex < maxIndex) {
                    contactList.setSelectedIndex(currentIndex + 1);
                    contactList.ensureIndexIsVisible(currentIndex + 1);
                    updateStatus("▼ 下一个");
                }
                break;

            case "OK":
                Contact selected = getSelectedContact();
                if (selected != null) {
                    updateStatus("已选择: " + selected.getName());
                    // TODO: 后续可跳转详情页
                }
                break;

            case "CANCEL":
                updateStatus("已取消");
                break;

            case "CALL":
                Contact toCall = getSelectedContact();
                if (toCall != null) {
                    updateStatus("正在呼叫 " + toCall.getName() + " " + toCall.getPhoneNumber());
                }
                break;

            case "HANGUP":
                updateStatus("已挂断");
                break;

            default:
                // 数字键等，暂不处理
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

        JLabel titleLabel = new JLabel("📞 联系人", SwingConstants.CENTER);
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
