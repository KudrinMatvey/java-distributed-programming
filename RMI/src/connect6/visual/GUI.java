package connect6.visual;

import javax.swing.*;
import java.rmi.RemoteException;

public class GUI extends JFrame {
    private JTextPane TextPanel;
    private JButton buildButton;
    private JPanel mainPanel;
    private JPanel Jpanel1;

    public GUI() throws RemoteException {
        Jpanel1 = new Connect6GUI();
        setContentPane(mainPanel);
        setVisible(true);
        buildButton.addActionListener(e -> {
            ((Connect6GUI) Jpanel1).paintComponent(getGraphics());

        });
    }

    public static void main(String[] args) throws RemoteException {
        GUI G = new GUI();
        G.setSize(1400, 1000);
        G.setVisible(true);
        G.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ((Connect6GUI) G.Jpanel1).startGame();
    }
}
