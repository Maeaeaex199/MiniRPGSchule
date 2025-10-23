package inventory;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<String, Integer> items;
    private int maxSlots;
    
    public Inventory(int maxSlots) {
        this.maxSlots = maxSlots;
        this.items = new HashMap<>();
    }
    
    public boolean addItem(String itemName, int quantity) {
        if (items.size() >= maxSlots && !items.containsKey(itemName)) {
            return false; // Inventar voll
        }
        
        items.put(itemName, items.getOrDefault(itemName, 0) + quantity);
        return true;
    }
    
    public boolean removeItem(String itemName, int quantity) {
        if (!items.containsKey(itemName)) {
            return false;
        }
        
        int currentQuantity = items.get(itemName);
        if (currentQuantity < quantity) {
            return false;
        }
        
        if (currentQuantity == quantity) {
            items.remove(itemName);
        } else {
            items.put(itemName, currentQuantity - quantity);
        }
        return true;
    }
    
    public int getItemQuantity(String itemName) {
        return items.getOrDefault(itemName, 0);
    }
    
    public boolean hasItem(String itemName) {
        return items.containsKey(itemName) && items.get(itemName) > 0;
    }
    
    public Map<String, Integer> getAllItems() {
        return new HashMap<>(items);
    }
    
    public boolean isFull() {
        return items.size() >= maxSlots;
    }
    
    public int getUsedSlots() {
        return items.size();
    }
    
    public int getMaxSlots() {
        return maxSlots;
    }
}
