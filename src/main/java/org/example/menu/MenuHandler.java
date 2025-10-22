package org.example.menu;


import javax.swing.*;

public class MenuHandler {
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
        JMenuItem beendenItem = new JMenuItem("Beenden");

        // Untermenüpunkte zu "Spiel" hinzufügen
        spielMenu.add(neuesSpielItem);
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

        // Menüleiste zum Frame hinzufügen
        return menuBar;
    }
}
