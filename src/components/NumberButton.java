package components;

import java.awt.*;

/**
 * 数字按键（0-9, *, #）
 * 每个数字键同时显示数字和对应字母
 * 继承自 PhoneButton
 */
public class NumberButton extends PhoneButton {

    private final String number;
    private final String letters;

    public NumberButton(String number, String letters) {
        super("<html><center><b>" + number + "</b><br/><small>" + letters + "</small></center></html>");
        this.number = number;
        this.letters = letters;
        setPreferredSize(new Dimension(70, 50));
        setBackground(new Color(220, 220, 220));
    }

    @Override
    public String getLabel() {
        return number + " (" + letters + ")";
    }
}
