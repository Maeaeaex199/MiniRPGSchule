package inventory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ChestInventory {
    private List<InventoryItem> items;
    private final int maxSlots = 20;

    public ChestInventory() {
        items = new ArrayList<>();
        initializeDefaultItems();
    }

    private void initializeDefaultItems() {
        // Beispiel-Items hinzufügen
        items.add(new InventoryItem("Schwert", 1));
        items.add(new InventoryItem("Heiltrank", 3));
        items.add(new InventoryItem("Gold", 50));
        items.add(new InventoryItem("Schlüssel", 2));
        items.add(new InventoryItem("Magische Schriftrolle", 1));
    }

    public void addItem(InventoryItem item) {
        if (items.size() < maxSlots) {
            items.add(item);
        }
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void drawInventoryUI(Graphics2D g2, int screenWidth, int screenHeight) {
        // Hintergrund
        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        g2.setColor(new Color(50, 50, 50));

        int invWidth = 400;
        int invHeight = 300;
        int invX = (screenWidth - invWidth) / 2;
        int invY = (screenHeight - invHeight) / 2;

        g2.fillRect(invX, invY, invWidth, invHeight);
        g2.setComposite(originalComposite);

        // Rahmen
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(invX, invY, invWidth, invHeight);

        // Titel
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();
        String title = "Truhen-Inventar";
        int titleX = invX + (invWidth - fm.stringWidth(title)) / 2;
        g2.drawString(title, titleX, invY + 30);

        // Items anzeigen
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        fm = g2.getFontMetrics();

        int startY = invY + 60;
        int lineHeight = 25;

        for (int i = 0; i < items.size() && i < 8; i++) { // Nur erste 8 Items anzeigen
            InventoryItem item = items.get(i);
            String itemText = item.name + " x" + item.quantity;
            g2.drawString(itemText, invX + 20, startY + i * lineHeight);
        }

        // Anweisungen
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.ITALIC, 12));
        String instruction = "E drücken zum Schließen";
        int instrX = invX + invWidth - g2.getFontMetrics().stringWidth(instruction) - 10;
        g2.drawString(instruction, instrX, invY + invHeight - 15);
    }
}