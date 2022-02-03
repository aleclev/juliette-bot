package Interface;

import Adapteurs.MetaAdapter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    private JButton STOPButton;
    private JPanel panel1;

    public MainWindow() {
        STOPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("Click event!\n");
            }
        });
    }
}
