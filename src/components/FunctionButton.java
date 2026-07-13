package components;

import java.awt.*;

/**
 * 功能按键（拨号键、挂断键）
 * 继承自 PhoneButton，实现 ButtonAction 接口
 */
public class FunctionButton extends PhoneButton implements ButtonAction {

    public enum Type {
        CALL("拨号", new Color(0, 180, 0)),      // 绿色拨号键
        HANGUP("挂断", new Color(220, 30, 30));   // 红色挂断键

        private final String label;
        private final Color color;

        Type(String label, Color color) {
            this.label = label;
            this.color = color;
        }

        public String getLabel() { return label; }
        public Color getColor() { return color; }
    }

    private final Type type;

    public FunctionButton(Type type) {
        super(type.label);
        this.type = type;
        setBackground(type.color);
        setForeground(Color.WHITE);
        setFont(getFont().deriveFont(Font.BOLD, 16));
        setPreferredSize(new Dimension(120, 50));
    }

    @Override
    public String getLabel() {
        return type.label;
    }

    @Override
    public void onPress() {
        System.out.println("功能键按下: " + type.label);
    }
}
