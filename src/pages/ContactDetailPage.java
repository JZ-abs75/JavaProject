package pages;

import model.Contact;
import repository.ContactRepository;
import repository.ContactRepositoryImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 联系人详情页
 * 显示选中联系人的完整信息
 * 确认=编辑  拨打=拨号  挂断=删除  取消=返回列表
 */
public class ContactDetailPage extends JPanel {

    private Contact contact;
    private ContactRepository repository;
    private JLabel nameLabel;
    private JLabel phoneLabel;
    private JLabel hintLabel;
    private JLabel deleteConfirmLabel;

    private boolean deleteConfirm;    // 是否在删除确认状态
    private boolean deleted;
    private boolean editRequested;
    private boolean backRequested;

    public ContactDetailPage() {
        setBackground(new Color(30, 30, 50));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 20, 30, 20));
        this.repository = new ContactRepositoryImpl();

        // 标题栏
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(20, 20, 40));
        titleBar.setBorder(new EmptyBorder(8, 10, 8, 10));
        JLabel titleLabel = new JLabel("联系人详情", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 22));
        titleBar.add(titleLabel, BorderLayout.CENTER);
        add(titleBar, BorderLayout.NORTH);

        // 中间内容
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(30, 30, 50));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(30, 30, 50));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 头像占位
        JLabel avatar = new JLabel("[*]", SwingConstants.CENTER);
        avatar.setForeground(new Color(100, 180, 255));
        avatar.setFont(new Font("Microsoft YaHei", Font.BOLD, 56));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(avatar);
        content.add(Box.createVerticalStrut(25));

        // 姓名
        nameLabel = new JLabel("", SwingConstants.CENTER);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 26));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(nameLabel);
        content.add(Box.createVerticalStrut(15));

        // 电话号码
        phoneLabel = new JLabel("", SwingConstants.CENTER);
        phoneLabel.setForeground(new Color(180, 200, 255));
        phoneLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 22));
        phoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(phoneLabel);
        content.add(Box.createVerticalStrut(25));

        // 删除确认文字（默认隐藏）
        deleteConfirmLabel = new JLabel("", SwingConstants.CENTER);
        deleteConfirmLabel.setForeground(new Color(255, 120, 100));
        deleteConfirmLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        deleteConfirmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(deleteConfirmLabel);
        content.add(Box.createVerticalStrut(15));

        // 操作提示
        hintLabel = new JLabel("确认=编辑  |  拨打=删除  |  取消=返回", SwingConstants.CENTER);
        hintLabel.setForeground(new Color(150, 150, 150));
        hintLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(hintLabel);

        centerPanel.add(content);
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * 设置要显示的联系人
     */
    public void setContact(Contact contact) {
        this.contact = contact;
        this.deleteConfirm = false;
        this.deleted = false;
        this.editRequested = false;
        this.backRequested = false;

        if (contact != null) {
            nameLabel.setText(contact.getName());
            phoneLabel.setText(contact.getPhoneNumber());
        }
        deleteConfirmLabel.setText("");
        hintLabel.setText("确认=编辑  |  拨打=拨号  |  挂断=删除  |  取消=返回");
    }

    /**
     * 处理按键
     */
    public void handleInput(String keyCode) {
        if (deleteConfirm) {
            // 删除确认状态
            switch (keyCode) {
                case "OK":
                    // 确认删除
                    if (contact != null && contact.getId() > 0) {
                        deleted = repository.delete(contact.getId());
                    }
                    deleteConfirm = false;
                    break;
                case "CANCEL":
                    // 取消删除
                    deleteConfirm = false;
                    deleteConfirmLabel.setText("");
                    hintLabel.setText("确认=编辑  |  拨打=拨号  |  挂断=删除  |  取消=返回");
                    break;
                default:
                    break;
            }
            return;
        }

        switch (keyCode) {
            case "OK":
                editRequested = true;
                break;

            case "CALL":
                // 拨号
                if (contact != null) {
                    hintLabel.setText("正在拨号 " + contact.getName() + " " + contact.getPhoneNumber() + " ...");
                }
                break;

            case "HANGUP":
                // 进入删除确认
                deleteConfirm = true;
                deleteConfirmLabel.setText("再次按确认删除 " + (contact != null ? contact.getName() : ""));
                hintLabel.setText("确认=确认删除  |  取消=取消");
                break;

            case "CANCEL":
                backRequested = true;
                break;
        }
    }

    public Contact getContact() { return contact; }
    public boolean isDeleted() { return deleted; }
    public boolean isEditRequested() { return editRequested; }
    public boolean isBackRequested() { return backRequested; }

    public void reset() {
        deleteConfirm = false;
        deleted = false;
        editRequested = false;
        backRequested = false;
    }
}
