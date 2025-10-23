package menu;

import javax.swing.*;

public class MenuHandler {
    private gamePanel.GamePanel gamePanel; // Referenz zum GamePanel
    
    public void setGamePanel(gamePanel.GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public JMenuBar mainmenu() {
        // Hauptfenster erstellen
        JFrame frame = new JFrame("Spiel-Menü");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Menüleiste erstellen
        JMenuBar menuBar = new JMenuBar();

        // Menüpunkte erstellen
        JMenu spielMenu = new JMenu("Spiel");
        JMenu einstellungenMenu = new JMenu("Einstellungen");
        JMenu hilfeMenu = new JMenu("Hilfe");

        // Untermenüpunkte für "Spiel" erstellen
        JMenuItem neuesSpielItem = new JMenuItem("Neues Spiel");
        JMenuItem pauseItem = new JMenuItem("Pause (ESC)");
        JMenuItem beendenItem = new JMenuItem("Beenden");

        // Untermenüpunkte zu "Spiel" hinzufügen
        spielMenu.add(neuesSpielItem);
        spielMenu.add(pauseItem);
        spielMenu.addSeparator();
        spielMenu.add(beendenItem);

        // Untermenüpunkte für "Hilfe" erstellen
        JMenuItem anleitungItem = new JMenuItem("Anleitung");
        hilfeMenu.add(anleitungItem);

        // Menüpunkte zur Menüleiste hinzufügen
        menuBar.add(spielMenu);
        menuBar.add(einstellungenMenu);
        menuBar.add(hilfeMenu);

        // Aktionen für Menüpunkte definieren
        beendenItem.addActionListener(e -> {
            System.exit(0);
        });

        neuesSpielItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Neues Spiel gestartet!");
            // Hier Logik für neues Spiel einfügen
        });

        // === NEU: Pause-Aktion ===
        pauseItem.addActionListener(e -> {
            if (gamePanel != null) {
                gamePanel.togglePause();
            }
        });

        // Menüleiste zum Frame hinzufügen
        return menuBar;
    }
}