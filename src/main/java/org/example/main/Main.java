package main;// Java
import javax.swing.*;
import gamePanel.GamePanel;
import org.example.menu.MenuHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            MenuHandler menuHandler = new MenuHandler();



            GamePanel gamePanel = null;
            try {
                gamePanel = new GamePanel();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setTitle("HugeCocknBalls");

            window.add(gamePanel);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setJMenuBar(menuHandler.mainmenu());
            window.setVisible(true);

            // Sicherstellen, dass KeyListener Events erhält
            gamePanel.requestFocusInWindow();

            gamePanel.setupGame();
            // Gameloop starten
            gamePanel.startGameThread();
        });
    }
}