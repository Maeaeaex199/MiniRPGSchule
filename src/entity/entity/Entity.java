package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int worldX, worldY;
    public int speed;

    public BufferedImage neutral;
    public BufferedImage up1, up2, up3, up4, up5, up6, up7, up8;
    public BufferedImage down1, down2, down3, down4, down5, down6, down7, down8;
    public BufferedImage left1, left2, left3, left4, left5, left6, left7, left8;
    public BufferedImage right1, right2, right3, right4, right5, right6, right7, right8;
    
    // WICHTIG: Default-Richtung
    public String direction = "neutral";
    
    // WICHTIG: Kollisionsrechteck 
    public Rectangle solidArea = new Rectangle(0, 0, 32, 32); // Default, wird in Unterklassen überschrieben
    
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
}