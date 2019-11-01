package connect6.visual;

import javax.swing.*;
import java.awt.*;

public class MyField extends JFrame {
    private final int amount;
    private final int size;

    public MyField(int amount, int size) throws HeadlessException {
        this.amount = amount;
        this.size = size;
    }

    public void paint(Graphics g) {
        super.paint(g);
        for (int x = 0; x < amount; x += size) {
            g.drawLine(x, 0, x,amount * size);
        }
        for (int y = 0; y < amount; y += size) {
            g.drawLine(0, y, amount * size, y);
        }
    }
}
