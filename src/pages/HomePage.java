package pages;

import model.Contact;
import repository.ContactRepository;
import repository.ContactRepositoryImpl;
import ui.ScreenPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * 联系人列表页面
 * 只能通过下方按键操作，不支持鼠标点击
 * 上/下：循环翻页  |  确认：查看详情  |  打电话：新增联系人  |  取消：返回菜单
 */
public class HomePage extends JPanel {

    private DefaultListModel<String> listModel;
    private JList<String> contactList;
    private List<Contact> contacts;
    private JLabel statusLabel;
    private ScreenPanel screenPanel;
    private ContactRepository repository;

    // 导航标记
    private boolean goToDetail;
    private boolean goToAdd;

    public HomePage(ScreenPanel screenPanel) {
        this.screenPanel = screenPanel;
        this.repository = new ContactRepositoryImpl();

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 50));
        setBorder(new EmptyBorder(10, 15, 10, 15));

        // 标题栏
        JPanel titleBar = createTitleBar();
        add(titleBar, BorderLayout.NORTH);

        // 联系人列表
        listModel = new DefaultListModel<>();
        contactList = new JList<>(listModel);
        contactList.setBackground(new Color(30, 30, 50));
        contactList.setForeground(Color.WHITE);
        contactList.setFont(new Font("Microsoft YaHei", Font.PLAIN, 18));
        contactList.setSelectionBackground(new Color(50, 100, 180));
        contactList.setSelectionForeground(Color.WHITE);
        contactList.setFixedCellHeight(45);
        contactList.setBorder(new EmptyBorder(5, 5, 5, 5));
        contactList.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(contactList);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(30, 30, 50));
        add(scrollPane, BorderLayout.CENTER);

        // 状态栏
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setForeground(new Color(150, 150, 150));
        statusLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        statusLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        add(statusLabel, BorderLayout.SOUTH);

        // 加载数据
        reloadContacts();
    }

    /**
     * 从数据库重新加载联系人
     */
    public void reloadContacts() {
        contacts = repository.findAll();
        if (contacts.isEmpty()) {
            contacts = Contact.getSampleContacts();
        }
        listModel.clear();
        for (Contact c : contacts) {
            listModel.addElement(formatContact(c));
        }
        if (contacts.size() > 0) {
            contactList.setSelectedIndex(0);
        }
        updateStatus("共 " + contacts.size() + " 人  |  确认=详情  拨打=新增");
    }

    /**
     * 处理来自下方按键的输入
     */
    public void handleKeyPress(String keyCode) {
        resetNavFlags();
        int currentIndex = contactList.getSelectedIndex();
        int maxIndex = listModel.size() - 1;

        if (maxIndex < 0) return;  // 空列表

        switch (keyCode) {
            case "UP":
                if (currentIndex > 0) {
                    contactList.setSelectedIndex(currentIndex - 1);
                    contactList.ensureIndexIsVisible(currentIndex - 1);
                } else {
                    contactList.setSelectedIndex(maxIndex);
                    contactList.ensureIndexIsVisible(maxIndex);
                }
                updateStatus(getSelectedName());
                break;

            case "DOWN":
                if (currentIndex < maxIndex) {
                    contactList.setSelectedIndex(currentIndex + 1);
                    contactList.ensureIndexIsVisible(currentIndex + 1);
                } else {
                    contactList.setSelectedIndex(0);
                    contactList.ensureIndexIsVisible(0);
                }
                updateStatus(getSelectedName());
                break;

            case "OK":
                // 查看详情
                if (getSelectedContact() != null) {
                    goToDetail = true;
                }
                break;

            case "CALL":
                // 新增联系人
                goToAdd = true;
                break;

            default:
                break;
        }
    }

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

    private String formatContact(Contact c) {
        return "  " + c.getName() + "        " + c.getPhoneNumber();
    }

    private String getSelectedName() {
        Contact c = getSelectedContact();
        return c != null ? c.getName() : "";
    }

    public Contact getSelectedContact() {
        int index = contactList.getSelectedIndex();
        if (index >= 0 && index < contacts.size()) {
            return contacts.get(index);
        }
        return null;
    }

    private void updateStatus(String msg) {
        statusLabel.setText("共 " + contacts.size() + " 个联系人  |  " + msg);
    }

    // 导航标记
    public boolean isGoToDetail() { return goToDetail; }
    public boolean isGoToAdd() { return goToAdd; }
    public void resetNavFlags() {
        goToDetail = false;
        goToAdd = false;
    }
}
