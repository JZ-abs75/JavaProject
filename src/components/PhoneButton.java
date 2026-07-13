package components;

import javax.swing.*;
import java.awt.*;

/**
 * 手机按钮抽象基类
 * 继承 JButton，为所有手机按键提供统一样式
 */
public abstract class PhoneButton extends JButton {

    public PhoneButton(String text) {
        super(text);
        setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        setFocusPainted(false);
        setMargin(new Insets(8, 4, 8, 4));
    }

    /**
     * 获取按钮显示标签
     */
    public abstract String getLabel();
}
