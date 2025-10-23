package entity;

import entity.Entity;
import gamePanel.GamePanel;
import keyHandler.KeyHandler;
import inventory.Inventory;
import ui.InventoryUI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    
    // Inventar hinzufügen
    private Inventory inventory;

    public final int screenX;
    public final int screenY;
    public int hasKey = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
        // Inventar mit 20 Slots initialisieren
        this.inventory = new Inventory(20);

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 12;
        solidArea.y = 18;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = gp.tileSize/3;
        solidArea.height = gp.tileSize/3;

        setDefaultValues();
        getPlayerImage();
    }
    
    // Getter für das Inventar
    public Inventory getInventory() {
        return inventory;
    }
    
    public void setDefaultValues() {
        worldX = gp.tileSize * 38;
        worldY = gp.tileSize * 9;
        speed = 5;
        direction = "neutral";
    }

    public void getPlayerImage() {
        try {
            neutral = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/up/player_run_up1.png")));

            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/up/player_run_up1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/up/player_run_up2.png")));
            up3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/up/player_run_up3.png")));
            up4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/up/player_run_up4.png")));
            up5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/up/player_run_up5.png")));
            up6 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/up/player_run_up6.png")));
            up7 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/up/player_run_up7.png")));
            up8 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/up/player_run_up8.png")));

            down1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/down/player_run_down1.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/down/player_run_down2.png")));
            down3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/down/player_run_down3.png")));
            down4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/down/player_run_down4.png")));
            down5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/down/player_run_down5.png")));
            down6 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/down/player_run_down6.png")));
            down7 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/down/player_run_down7.png")));
            down8 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/down/player_run_down8.png")));

            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/left/player_run_left1.png")));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/left/player_run_left2.png")));
            left3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/left/player_run_left3.png")));
            left4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/left/player_run_left4.png")));
            left5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/left/player_run_left5.png")));
            left6 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/left/player_run_left6.png")));
            left7 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/left/player_run_left7.png")));
            left8 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/left/player_run_left8.png")));

            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/right/player_run_right1.png")));
            right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/right/player_run_right2.png")));
            right3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/right/player_run_right3.png")));
            right4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/right/player_run_right4.png")));
            right5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/right/player_run_right5.png")));
            right6 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/right/player_run_right6.png")));
            right7 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/right/player_run_right7.png")));
            right8 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/run/right/player_run_right8.png")));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (keyH.upPressed) {
            direction = "up";
        } else if (keyH.downPressed) {
            direction = "down";
        } else if (keyH.leftPressed) {
            direction = "left";
        } else if (keyH.rightPressed) {
            direction = "right";
        } else {
            direction = "neutral";
        }
        
        collisionOn = false;
        gp.cChecker.checkTile(this);

        int objIndex = gp.cChecker.checkObject(this, true);
        pickupObject(objIndex);

        if (!collisionOn) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
    }

    public void pickupObject(int i) {
        if (i != 999) {
            String objectName = gp.obj[i].name;
            switch (objectName) {
                case "Key":
                    if (inventory.addItem("Key", 1)) {
                        gp.obj[i] = null;
                        hasKey++; // Für Rückwärtskompatibilität
                        System.out.println("Schlüssel aufgehoben! Inventar: " + inventory.getItemQuantity("Key"));
                    } else {
                        System.out.println("Inventar ist voll!");
                    }
                    break;
                case "Door":
                    if (inventory.hasItem("Key")) {
                        gp.obj[i] = null;
                        inventory.removeItem("Key", 1);
                        hasKey--; // Für Rückwärtskompatibilität
                        System.out.println("Tür geöffnet! Verbleibende Schlüssel: " + inventory.getItemQuantity("Key"));
                    } else {
                        System.out.println("Du brauchst einen Schlüssel!");
                    }
                    break;
                case "Chest":
                    // Kisten können NICHT mehr aufgesammelt werden
                    // Sie können nur noch mit SPACE geöffnet werden
                    System.out.println("Drücke SPACE um die Truhe zu öffnen");
                    break;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch(direction) {
            case "up":
                image = up1;
                break;
            case "down":
                image = down1;
                break;
            case "left":
                image = left1;
                break;
            case "right":
                image = right1;
                break;
            case "neutral":
                image = neutral;
                break;
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        
        // Inventar-Status anzeigen (optional, nur wenn nicht das komplette UI offen ist)
        if (!gp.getInventoryUI().isOpen()) {
            drawInventoryStatus(g2);
        }
    }
    
    private void drawInventoryStatus(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Inventar: " + inventory.getUsedSlots() + "/" + inventory.getMaxSlots(), 10, 30);
        
        // Zeige Schlüssel an
        if (inventory.hasItem("Key")) {
            g2.drawString("Schlüssel: " + inventory.getItemQuantity("Key"), 10, 50);
        }
        
        // Hinweis für Inventar
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("PG_DOWN für Inventar", 10, 70);
    }
}