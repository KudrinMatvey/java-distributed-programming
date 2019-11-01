package connect6.visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Client extends JFrame {
    private JPanel rootPanel;
    private JPanel field;
    private Integer size = 10;
    private Integer amount = 19;

    public Client() {
        setContentPane(rootPanel);
        rootPanel.setVisible(true);
        rootPanel.setSize(size * amount, size * amount);
        setContentPane(field);
        setVisible(true);
        Graphics g = field.getGraphics();

        for (int x = 0; x < amount; x += size) {
            g.drawLine(x, 0, x,amount * size);
        }
        for (int y = 0; y < amount; y += size) {
            g.drawLine(0, y, amount * size, y);
        }
            g.drawLine(0, 10, 30, 58    );

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        field.addMouseListener(new MouseAdapter() {
        });
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                x = e.getY();
                super.mouseClicked(e);
            }
        });
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.setSize(500, 500);
    }
}
