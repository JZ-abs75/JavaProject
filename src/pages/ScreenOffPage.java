package pages;

import javax.swing.*;
import java.awt.*;

/**
 * 黑屏页面 —— 默认状态
 * 全黑，模拟手机关屏
 * 按任意键亮屏（由 ScreenPanel 状态机处理）
 */
public class ScreenOffPage extends JPanel {

    public ScreenOffPage() {
        setBackground(Color.BLACK);
        // 纯黑，无任何文字或组件
    }
}
