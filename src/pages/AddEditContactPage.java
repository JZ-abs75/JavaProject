package pages;

import model.Contact;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 添加/编辑联系人页面
 * 使用数字键盘输入（多击法 T9 风格）
 *
 * 标准电话键盘映射:
 *   2=abc  3=def  4=ghi
 *   5=jkl  6=mno  7=pqrs
 *   8=tuv  9=wxyz
 *   0=空格  *=退格  #=字母/数字切换
 *
 * 多击: 同一键1.5秒内再次按下→循环到下一个字母
 */
public class AddEditContactPage extends JPanel {

    // 键盘映射（与按键标签一致）: 索引=数字键
    private static final String[] KEY_CHARS = {
        " 0",       // 0
        "abc1",     // 1
        "def2",     // 2
        "ghi3",     // 3
        "jkl4",     // 4
        "mno5",     // 5
        "pqr6",     // 6
        "stu7",     // 7
        "vwx8",     // 8
        "yz9",      // 9
    };

    private static final long TAP_TIMEOUT = 1500; // 多击超时(毫秒)

    private Contact originalContact;
    private String nameText;
    private String phoneText;
    private boolean editingName;
    private boolean saved;
    private boolean cancelled;
    private boolean numberMode;

    // 多击状态
    private int lastKey = -1;
    private int tapIndex = 0;
    private long lastTapTime = 0;

    private JLabel titleLabel;
    private JLabel nameFieldLabel;
    private JLabel phoneFieldLabel;
    private JLabel modeLabel;
    private JLabel hintLabel;

    public AddEditContactPage() {
        setBackground(new Color(30, 30, 50));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private void buildUI() {
        removeAll();

        // 标题栏
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(20, 20, 40));
        titleBar.setBorder(new EmptyBorder(8, 10, 8, 10));
        String title = (originalContact != null && originalContact.getId() > 0)
            ? "编辑联系人" : "新增联系人";
        titleLabel = new JLabel(title, SwingConstants.CENTER);
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
        content.add(Box.createVerticalStrut(20));

        // 姓名字段
        JLabel namePrompt = new JLabel("姓名:", SwingConstants.LEFT);
        namePrompt.setForeground(new Color(150, 150, 150));
        namePrompt.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        namePrompt.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(namePrompt);
        content.add(Box.createVerticalStrut(5));

        nameFieldLabel = createFieldLabel();
        content.add(nameFieldLabel);
        content.add(Box.createVerticalStrut(20));

        // 电话字段
        JLabel phonePrompt = new JLabel("电话:", SwingConstants.LEFT);
        phonePrompt.setForeground(new Color(150, 150, 150));
        phonePrompt.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        phonePrompt.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(phonePrompt);
        content.add(Box.createVerticalStrut(5));

        phoneFieldLabel = createFieldLabel();
        content.add(phoneFieldLabel);
        content.add(Box.createVerticalStrut(10));

        // 输入模式
        modeLabel = new JLabel("", SwingConstants.LEFT);
        modeLabel.setForeground(new Color(120, 180, 120));
        modeLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        modeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(modeLabel);
        content.add(Box.createVerticalStrut(20));

        // 操作提示
        hintLabel = new JLabel("确认=保存  |  取消=放弃  |  上/下=切换字段  |  #=模式  |  *=退格", SwingConstants.CENTER);
        hintLabel.setForeground(new Color(150, 150, 150));
        hintLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(hintLabel);

        centerPanel.add(content);
        add(centerPanel, BorderLayout.CENTER);

        refreshDisplay();
    }

    private JLabel createFieldLabel() {
        JLabel label = new JLabel("", SwingConstants.LEFT);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Microsoft YaHei", Font.BOLD, 22));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 150), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        label.setOpaque(true);
        label.setBackground(new Color(40, 40, 60));
        return label;
    }

    // ==================== 输入处理 ====================

    public void handleInput(String keyCode) {
        switch (keyCode) {
            case "UP":
            case "DOWN":
                commitTap(); // 切换字段前确认当前字符
                editingName = !editingName;
                refreshDisplay();
                break;

            case "OK":
                commitTap();
                saved = true;
                break;

            case "CANCEL":
                cancelled = true;
                break;

            case "NUM_*":
                commitTap();
                backspace();
                break;

            case "NUM_#":
                commitTap();
                numberMode = !numberMode;
                refreshDisplay();
                break;

            case "NUM_0":
                commitTap();
                if (numberMode) {
                    typeChar('0');
                } else {
                    typeChar(' ');
                }
                break;

            default:
                if (keyCode.startsWith("NUM_") && keyCode.length() == 5) {
                    char digit = keyCode.charAt(4);
                    if (digit >= '1' && digit <= '9') {
                        handleNumberKey(digit - '0');
                    }
                }
                break;
        }
    }

    /**
     * 处理数字键 1-9: 多击法输入
     */
    private void handleNumberKey(int key) {
        if (numberMode) {
            commitTap();
            typeChar((char) ('0' + key));
            return;
        }

        long now = System.currentTimeMillis();
        String chars = KEY_CHARS[key];
        boolean sameKey = (key == lastKey);
        boolean inTime = (now - lastTapTime) < TAP_TIMEOUT;

        if (sameKey && inTime && lastKey >= 0) {
            // 同一键 + 未超时 → 循环到下一个字母
            tapIndex = (tapIndex + 1) % chars.length();
        } else {
            // 不同键 或 超时 → 先确认上一个字符, 开始新字符
            commitTap();
            tapIndex = 0;
        }

        lastKey = key;
        lastTapTime = now;

        // 在当前文本末尾显示正在编辑的字符（暂存，commitTap 时确认）
        char ch = chars.charAt(tapIndex);
        String text = getCurrentText();
        setCurrentText(text + ch);

        refreshDisplay();
    }

    /**
     * 确认当前正在编辑的字符（不再被多击替换）
     */
    private void commitTap() {
        // 多击字符已经直接在文本中了，不需要额外操作
        // 只需重置多击状态
        lastKey = -1;
        tapIndex = 0;
    }

    private void typeChar(char ch) {
        setCurrentText(getCurrentText() + ch);
        lastKey = -1;
        tapIndex = 0;
        refreshDisplay();
    }

    private void backspace() {
        String text = getCurrentText();
        if (text.length() > 0) {
            setCurrentText(text.substring(0, text.length() - 1));
        }
        lastKey = -1;
        tapIndex = 0;
        refreshDisplay();
    }

    // ==================== 辅助方法 ====================

    private String getCurrentText() {
        return editingName ? nameText : phoneText;
    }

    private void setCurrentText(String text) {
        if (editingName) {
            nameText = text;
        } else {
            phoneText = text;
        }
    }

    private void refreshDisplay() {
        if (nameFieldLabel == null) return;

        // 高亮当前编辑字段
        Color activeBorder = new Color(100, 180, 255);
        Color inactiveBorder = new Color(100, 100, 150);

        nameFieldLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(editingName ? activeBorder : inactiveBorder, editingName ? 2 : 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        phoneFieldLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(editingName ? inactiveBorder : activeBorder, editingName ? 1 : 2),
            new EmptyBorder(8, 10, 8, 10)
        ));

        nameFieldLabel.setText(nameText + (editingName ? "_" : ""));
        phoneFieldLabel.setText(phoneText + (!editingName ? "_" : ""));

        String modeStr = numberMode ? "数字(123)" : "字母(ABC)";
        modeLabel.setText("模式: " + modeStr + "  |  ");
    }

    // ==================== 公共接口 ====================

    public void setContact(Contact contact) {
        this.originalContact = contact;
        if (contact != null && contact.getId() > 0) {
            nameText = contact.getName() != null ? contact.getName() : "";
            phoneText = contact.getPhoneNumber() != null ? contact.getPhoneNumber() : "";
        } else {
            nameText = "";
            phoneText = "";
        }
        editingName = true;
        numberMode = false;
        saved = false;
        cancelled = false;
        lastKey = -1;
        tapIndex = 0;
        lastTapTime = 0;

        buildUI();
    }

    public boolean isSaved() { return saved; }
    public boolean isCancelled() { return cancelled; }

    public Contact getResult() {
        int id = (originalContact != null) ? originalContact.getId() : -1;
        return new Contact(id, nameText.trim(), phoneText.trim());
    }

    public boolean isEditMode() {
        return originalContact != null && originalContact.getId() > 0;
    }

    public void reset() {
        this.originalContact = null;
        this.nameText = "";
        this.phoneText = "";
        this.editingName = true;
        this.numberMode = false;
        this.saved = false;
        this.cancelled = false;
        this.lastKey = -1;
        this.tapIndex = 0;
        this.lastTapTime = 0;
    }
}
