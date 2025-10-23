package object;

import inventory.ChestInventory;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Chest extends SuperObject {
    private ChestInventory inventory;
    private boolean isOpen = false;

    public OBJ_Chest() {
        name = "Chest";
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/chest.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        collision = true;
        inventory = new ChestInventory();
    }

    public void interact() {
        isOpen = !isOpen;
        System.out.println("Truhe " + (isOpen ? "geöffnet" : "geschlossen"));
    }

    public boolean isOpen() {
        return isOpen;
    }

    public ChestInventory getInventory() {
        return inventory;
    }

    public void close() {
        isOpen = false;
    }
}