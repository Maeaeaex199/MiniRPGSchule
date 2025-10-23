package inventory;

import java.awt.image.BufferedImage;

public class InventoryItem {
    public String name;
    public BufferedImage icon;
    public int quantity;
    public String description;
    
    public InventoryItem(String name, BufferedImage icon, int quantity, String description) {
        this.name = name;
        this.icon = icon;
        this.quantity = quantity;
        this.description = description;
    }
    
    public InventoryItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.description = name;
    }
}
