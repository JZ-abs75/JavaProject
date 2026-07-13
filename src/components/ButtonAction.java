package components;

/**
 * 按钮行为接口
 * 定义按钮被按下时的行为
 * FunctionButton 和 NavButton 实现此接口
 */
public interface ButtonAction {
    /**
     * 按钮按下时触发
     */
    void onPress();
}
