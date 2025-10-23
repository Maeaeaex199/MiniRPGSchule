package ui;

import inventory.Inventory;
import java.awt.*;
import java.util.Map;

public class InventoryUI {
    private boolean isOpen = false;
    
    public void toggle() {
        isOpen = !isOpen;
    }
    
    public void open() {
        isOpen = true;
    }
    
    public void close() {
        isOpen = false;
    }
    
    public boolean isOpen() {
        return isOpen;
    }
    
    public void draw(Graphics2D g2, Inventory inventory, int screenWidth, int screenHeight) {
        if (!isOpen) return;
        
        // Halbtransparenter Hintergrund
        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        g2.setColor(new Color(50, 50, 50));
        
        int panelWidth = 400;
        int panelHeight = 300;
        int x = (screenWidth - panelWidth) / 2;
        int y = (screenHeight - panelHeight) / 2;
        
        g2.fillRoundRect(x, y, panelWidth, panelHeight, 15, 15);
        
        // Rahmen
        g2.setComposite(originalComposite);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, panelWidth, panelHeight, 15, 15);
        
        // Titel
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        String title = "INVENTAR";
        FontMetrics titleFm = g2.getFontMetrics();
        int titleX = x + (panelWidth - titleFm.stringWidth(title)) / 2;
        g2.drawString(title, titleX, y + 35);
        
        // Inventar-Info
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        String info = "Belegte Slots: " + inventory.getUsedSlots() + "/" + inventory.getMaxSlots();
        FontMetrics infoFm = g2.getFontMetrics();
        int infoX = x + (panelWidth - infoFm.stringWidth(info)) / 2;
        g2.drawString(info, infoX, y + 60);
        
        // Linie
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawLine(x + 20, y + 70, x + panelWidth - 20, y + 70);
        
        // Items auflisten
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        Map<String, Integer> items = inventory.getAllItems();
        
        if (items.isEmpty()) {
            String emptyText = "Inventar ist leer";
            FontMetrics emptyFm = g2.getFontMetrics();
            int emptyX = x + (panelWidth - emptyFm.stringWidth(emptyText)) / 2;
            g2.drawString(emptyText, emptyX, y + 120);
        } else {
            int itemY = y + 90;
            int itemIndex = 0;
            
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                if (itemIndex >= 8) break; // Max 8 Items anzeigen
                
                String itemText = entry.getKey() + " x" + entry.getValue();
                g2.drawString(itemText, x + 30, itemY + (itemIndex * 25));
                itemIndex++;
            }
            
            if (items.size() > 8) {
                g2.setColor(Color.YELLOW);
                g2.drawString("... und " + (items.size() - 8) + " weitere Items", x + 30, itemY + (8 * 25));
            }
        }
        
        // Steuerung anzeigen
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        String controls = "ESC oder PG_DOWN zum Schließen";
        FontMetrics controlsFm = g2.getFontMetrics();
        int controlsX = x + (panelWidth - controlsFm.stringWidth(controls)) / 2;
        g2.drawString(controls, controlsX, y + panelHeight - 15);
    }
}
