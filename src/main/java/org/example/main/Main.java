package main;// Java
import javax.swing.*;
import gamePanel.GamePanel;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setTitle("HugeCocknBalls");

            GamePanel gamePanel = null;
            try {
                gamePanel = new GamePanel();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            window.add(gamePanel);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            // Sicherstellen, dass KeyListener Events erhält
            gamePanel.requestFocusInWindow();

            // Gameloop starten
            gamePanel.startGameThread();
        });
    }
}