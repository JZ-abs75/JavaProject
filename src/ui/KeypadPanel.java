package ui;

import components.*;

import javax.swing.*;
import java.awt.*;

/**
 * 按键面板 —— 手机下方按键区域
 * 布局：3×3 控制网格 + 数字键盘
 *
 * 确认    上    取消
 * 左     确认   右
 * 打电话  下   挂电话
 */
public class KeypadPanel extends JPanel {

    private ScreenPanel screenPanel;

    // 按键动作常量
    public static final String KEY_UP = "UP";
    public static final String KEY_DOWN = "DOWN";
    public static final String KEY_LEFT = "LEFT";
    public static final String KEY_RIGHT = "RIGHT";
    public static final String KEY_OK = "OK";
    public static final String KEY_CANCEL = "CANCEL";
    public static final String KEY_CALL = "CALL";
    public static final String KEY_HANGUP = "HANGUP";

    public KeypadPanel(ScreenPanel screenPanel) {
        this.screenPanel = screenPanel;

        setLayout(new GridBagLayout());
        setBackground(new Color(40, 40, 40));
        setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 10));

        // ---- 第1部分：3×3 控制网格 ----
        add(createControlGrid(), createRowGbc(0));
        add(createSeparator(), createRowGbc(1));

        // ---- 第2部分：数字键盘 ----
        add(createNumberPad(), createRowGbc(2));
    }

    /**
     * 创建 3×3 控制网格
     * 确认    上    取消
     * 左     确认   右
     * 打电话  下   挂电话
     */
    private JPanel createControlGrid() {
        JPanel panel = new JPanel(new GridLayout(3, 3, 6, 6));
        panel.setBackground(new Color(40, 40, 40));

        // ---- 第1行：确认  上  取消 ----
        NavButton btnOK1 = new NavButton(NavButton.Direction.OK);
        NavButton btnUp = new NavButton(NavButton.Direction.UP);
        NavButton btnCancel = new NavButton(NavButton.Direction.CANCEL);

        btnOK1.addActionListener(e -> dispatch(KEY_OK));
        btnUp.addActionListener(e -> dispatch(KEY_UP));
        btnCancel.addActionListener(e -> dispatch(KEY_CANCEL));

        panel.add(btnOK1);
        panel.add(btnUp);
        panel.add(btnCancel);

        // ---- 第2行：左  确认  右 ----
        NavButton btnLeft = new NavButton(NavButton.Direction.LEFT);
        NavButton btnOK2 = new NavButton(NavButton.Direction.OK);
        NavButton btnRight = new NavButton(NavButton.Direction.RIGHT);

        btnLeft.addActionListener(e -> dispatch(KEY_LEFT));
        btnOK2.addActionListener(e -> dispatch(KEY_OK));
        btnRight.addActionListener(e -> dispatch(KEY_RIGHT));

        panel.add(btnLeft);
        panel.add(btnOK2);
        panel.add(btnRight);

        // ---- 第3行：打电话  下  挂电话 ----
        FunctionButton btnCall = new FunctionButton(FunctionButton.Type.CALL);
        NavButton btnDown = new NavButton(NavButton.Direction.DOWN);
        FunctionButton btnHangup = new FunctionButton(FunctionButton.Type.HANGUP);

        btnCall.addActionListener(e -> dispatch(KEY_CALL));
        btnDown.addActionListener(e -> dispatch(KEY_DOWN));
        btnHangup.addActionListener(e -> dispatch(KEY_HANGUP));

        // 调整功能键大小以适应网格
        btnCall.setPreferredSize(new Dimension(70, 45));
        btnHangup.setPreferredSize(new Dimension(70, 45));

        panel.add(btnCall);
        panel.add(btnDown);
        panel.add(btnHangup);

        return panel;
    }

    /**
     * 创建数字键盘（4×3 布局）
     */
    private JPanel createNumberPad() {
        JPanel panel = new JPanel(new GridLayout(4, 3, 5, 5));
        panel.setBackground(new Color(40, 40, 40));

        String[][] keyMap = {
                {"1", "abc"}, {"2", "def"}, {"3", "ghi"},
                {"4", "jkl"}, {"5", "mno"}, {"6", "pqr"},
                {"7", "stu"}, {"8", "vwx"}, {"9", "yz"},
                {"*", "+"},   {"0", "_"},   {"#", "#"},
        };

        for (String[] key : keyMap) {
            NumberButton btn = new NumberButton(key[0], key[1]);
            final String keyNum = key[0];
            btn.addActionListener(e -> {
//                System.out.println("数字键按下: " + keyNum);
                if (screenPanel != null) {
                    screenPanel.dispatchKeyPress("NUM_" + keyNum);
                }
            });
            panel.add(btn);
        }

        return panel;
    }

    /**
     * 分发按键事件到屏幕
     */
    private void dispatch(String keyCode) {
        System.out.println("按键: " + keyCode);
        if (screenPanel != null) {
            screenPanel.dispatchKeyPress(keyCode);
        }
    }

    private JSeparator createSeparator() {
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(new Color(80, 80, 80));
        return sep;
    }

    private GridBagConstraints createRowGbc(int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 0, 3, 0);
        return gbc;
    }
}
