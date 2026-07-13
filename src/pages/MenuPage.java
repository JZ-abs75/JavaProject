package pages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 菜单页面 —— 功能选择
 * 左/右循环切换功能，确认进入对应功能
 */
public class MenuPage extends JPanel {

    private final String[] functions = {
            "联系人",
            "拨号",
            "短信",
            "设置"
    };

    private final String[] functionKeys = {
            "联系人", "拨号", "短信", "设置"
    };

    private int currentIndex;
    private boolean confirmed;
    private String selectedFunction;

    private JLabel iconLabel;
    private JLabel nameLabel;
    private JLabel navHint;

    public MenuPage() {
        setBackground(new Color(20, 20, 40));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 20, 40, 20));

        reset();

        // 中间内容
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(20, 20, 40));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(20, 20, 40));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 大图标（用文字代替emoji）
        iconLabel = new JLabel(getIconFor(currentIndex), SwingConstants.CENTER);
        iconLabel.setForeground(new Color(100, 180, 255));
        iconLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(iconLabel);
        content.add(Box.createVerticalStrut(20));

        // 功能名
        nameLabel = new JLabel(functions[currentIndex], SwingConstants.CENTER);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 28));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(nameLabel);
        content.add(Box.createVerticalStrut(30));

        // 操作提示
        navHint = new JLabel("< 左右选择  >    确认进入", SwingConstants.CENTER);
        navHint.setForeground(new Color(150, 150, 150));
        navHint.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        navHint.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(navHint);

        centerPanel.add(content);
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * 处理按键
     */
    public void handleInput(String keyCode) {
        switch (keyCode) {
            case "LEFT":
                currentIndex = (currentIndex - 1 + functions.length) % functions.length;
                updateDisplay();
                break;

            case "RIGHT":
                currentIndex = (currentIndex + 1) % functions.length;
                updateDisplay();
                break;

            case "OK":
                confirmed = true;
                selectedFunction = functionKeys[currentIndex];
                break;
        }
    }

    private void updateDisplay() {
        iconLabel.setText(getIconFor(currentIndex));
        nameLabel.setText(functions[currentIndex]);
    }

    private String getIconFor(int index) {
        switch (index) {
            case 0: return "[*]";
            case 1: return "[#]";
            case 2: return "[:]";
            case 3: return "[=]";
            default: return "[*]";
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getSelectedFunction() {
        return selectedFunction;
    }

    /**
     * 重置菜单状态
     */
    public void reset() {
        currentIndex = 0;
        confirmed = false;
        selectedFunction = null;
        if (iconLabel != null) {
            updateDisplay();
        }
    }
}
