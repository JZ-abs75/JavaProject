package pages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 解锁页面
 * 解锁序列：先按 # → 再按确认
 * 按错任何一步 → 显示正确操作提示
 */
public class LockScreenPage extends JPanel {

    private boolean waitingForHash;   // true=等待按#, false=已按#等待确认
    private boolean unlocked;
    private JLabel mainLabel;
    private JLabel hintLabel;

    public LockScreenPage() {
        setBackground(new Color(20, 20, 40));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 20, 40, 20));

        reset();

        // 中间：主提示
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(20, 20, 40));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(20, 20, 40));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 锁图标
        JLabel lockIcon = new JLabel("[LOCK]", SwingConstants.CENTER);
        lockIcon.setFont(new Font("Microsoft YaHei", Font.BOLD, 36));
        lockIcon.setForeground(new Color(180, 180, 200));
        lockIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(lockIcon);
        content.add(Box.createVerticalStrut(30));

        // 主提示文字
        mainLabel = new JLabel("按 # 然后按确认解锁", SwingConstants.CENTER);
        mainLabel.setForeground(Color.WHITE);
        mainLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 20));
        mainLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(mainLabel);
        content.add(Box.createVerticalStrut(20));

        // 错误提示
        hintLabel = new JLabel(" ", SwingConstants.CENTER);
        hintLabel.setForeground(new Color(255, 150, 100));
        hintLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(hintLabel);

        centerPanel.add(content);
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * 处理按键输入，返回 true 表示解锁成功
     */
    public boolean handleInput(String keyCode) {
        if (waitingForHash) {
            // 等待按 #
            if ("NUM_#".equals(keyCode)) {
                waitingForHash = false;
                mainLabel.setText("已按 # ，请按确认");
                hintLabel.setText(" ");
                return false;
            } else {
                // 按错了，显示提示
                showErrorHint();
                return false;
            }
        } else {
            // 等待按确认
            if ("OK".equals(keyCode)) {
                unlocked = true;
                return true;
            } else {
                // 按错了，重置
                showErrorHint();
                waitingForHash = true;
                mainLabel.setText("按 # 然后按确认解锁");
                return false;
            }
        }
    }

    private void showErrorHint() {
        hintLabel.setText("[!] 请先按 # 键，再按确认键解锁");
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * 重置解锁状态（从菜单返回黑屏后再亮屏时使用）
     */
    public void reset() {
        waitingForHash = true;
        unlocked = false;
        if (mainLabel != null) {
            mainLabel.setText("按 # 然后按确认解锁");
        }
        if (hintLabel != null) {
            hintLabel.setText(" ");
        }
    }
}
