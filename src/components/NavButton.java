package components;

import java.awt.*;

/**
 * 导航按键（上、下、左、右、确认、取消）
 * 继承自 PhoneButton，实现 ButtonAction 接口
 */
public class NavButton extends PhoneButton implements ButtonAction {

    public enum Direction {
        UP("上"),
        DOWN("下"),
        LEFT("左"),
        RIGHT("右"),
        OK("确认"),
        CANCEL("取消");

        private final String symbol;

        Direction(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() { return symbol; }
    }

    private final Direction direction;

    public NavButton(Direction direction) {
        super(direction.symbol);
        this.direction = direction;
        setBackground(new Color(180, 180, 180));
        setFont(getFont().deriveFont(Font.BOLD, 16));
        setPreferredSize(new Dimension(70, 45));

        // 确认按钮特殊样式
        if (direction == Direction.OK) {
            setBackground(new Color(100, 150, 200));
            setForeground(Color.WHITE);
        }

        // 取消按钮特殊样式
        if (direction == Direction.CANCEL) {
            setBackground(new Color(200, 100, 100));
            setForeground(Color.WHITE);
        }
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String getLabel() {
        return direction.symbol;
    }

    @Override
    public void onPress() {
        System.out.println("导航键按下: " + direction.symbol);
    }
}
