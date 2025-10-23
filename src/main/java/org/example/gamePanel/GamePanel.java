package gamePanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import collisionChecker.CollisionChecker;
import entity.Player;
import keyHandler.KeyHandler;
import object.SuperObject;
import object.OBJ_Chest;
import main.AssetSetter;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 8;
    final int scale = 4;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 16;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 60;

    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];

    // === PAUSENFUNKTION ===
    private volatile boolean paused = false;
    private boolean prevEscapePressed = false;
    private long lastPauseToggleTime = 0;
    private static final long PAUSE_COOLDOWN = 250; // Millisekunden

    // === INVENTAR-SYSTEM ===
    private OBJ_Chest currentChest = null;
    private boolean prevInteractPressed = false;

    // These fields are currently unused; keep or remove as needed.
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 5;

    public GamePanel() throws IOException {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS; // nanoseconds per frame, as double
        double delta = 0;
        long lastTime = System.nanoTime();

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                // Pause-Toggle prüfen
                handlePauseToggle();

                // Chest-Interaktion prüfen
                handleChestInteraction();

                // Nur updaten wenn nicht pausiert und kein Inventar offen
                if (!paused && !isAnyChestOpen()) {
                    update();
                }

                // Immer repainten (für Overlays)
                repaint();
                delta--;
            } else {
                // Prevent 100% CPU busy-waiting
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) { }
            }
        }
    }

    // === PAUSE-HANDLING ===
    private void handlePauseToggle() {
        boolean escPressed = keyH.escapePressed;
        long currentTime = System.currentTimeMillis();

        // ESC gedrückt + nicht vorher gedrückt + Cooldown abgelaufen
        if (escPressed && !prevEscapePressed &&
                (currentTime - lastPauseToggleTime) > PAUSE_COOLDOWN) {

            // Wenn ein Inventar offen ist, schließe es statt zu pausieren
            if (isAnyChestOpen()) {
                closeAllChests();
            } else {
                togglePause();
            }
            lastPauseToggleTime = currentTime;
        }

        prevEscapePressed = escPressed;
    }

    // === CHEST-INTERAKTION ===
    private void handleChestInteraction() {
        boolean interactPressed = keyH.interactPressed;

        if (interactPressed && !prevInteractPressed) {
            // Schaue nach naheliegenden Truhen
            checkChestInteraction();
        }

        prevInteractPressed = interactPressed;
    }

    private void checkChestInteraction() {
        for (SuperObject object : obj) {
            if (object instanceof OBJ_Chest) {
                OBJ_Chest chest = (OBJ_Chest) object;

                // Prüfe Entfernung zur Truhe
                int distance = Math.abs(player.worldX - chest.worldX) +
                        Math.abs(player.worldY - chest.worldY);

                if (distance < tileSize * 2) { // Innerhalb von 2 Tiles
                    chest.interact();
                    currentChest = chest.isOpen() ? chest : null;
                    break;
                }
            }
        }
    }

    private boolean isAnyChestOpen() {
        for (SuperObject object : obj) {
            if (object instanceof OBJ_Chest && ((OBJ_Chest) object).isOpen()) {
                return true;
            }
        }
        return false;
    }

    private void closeAllChests() {
        for (SuperObject object : obj) {
            if (object instanceof OBJ_Chest) {
                ((OBJ_Chest) object).close();
            }
        }
        currentChest = null;
    }

    public void togglePause() {
        paused = !paused;
        System.out.println("Spiel " + (paused ? "pausiert" : "fortgesetzt"));
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public void update() {
        player.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Spielwelt zeichnen
        tileM.draw(g2);
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }
        player.draw(g2);

        // === CHEST-INVENTAR ===
        for (SuperObject object : obj) {
            if (object instanceof OBJ_Chest) {
                OBJ_Chest chest = (OBJ_Chest) object;
                if (chest.isOpen()) {
                    chest.getInventory().drawInventoryUI(g2, screenWidth, screenHeight);
                }
            }
        }

        // === PAUSE-OVERLAY ===
        if (paused) {
            // Halbtransparenter schwarzer Overlay
            Composite originalComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            g2.setComposite(originalComposite);

            // Pause-Text
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 36));
            FontMetrics fm = g2.getFontMetrics();

            String pauseText = "PAUSIERT";
            int textWidth = fm.stringWidth(pauseText);
            int textX = (screenWidth - textWidth) / 2;
            int textY = screenHeight / 2 - 20;
            g2.drawString(pauseText, textX, textY);

            // Anleitung
            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            fm = g2.getFontMetrics();
            String instructionText = "ESC drücken zum Fortsetzen";
            int instrWidth = fm.stringWidth(instructionText);
            int instrX = (screenWidth - instrWidth) / 2;
            int instrY = textY + 50;
            g2.drawString(instructionText, instrX, instrY);
        }

        // === INTERAKTIONS-HILFE ===
        if (!paused && !isAnyChestOpen()) {
            // Schaue nach naheliegenden Truhen für Interaktions-Hint
            for (SuperObject object : obj) {
                if (object instanceof OBJ_Chest) {
                    int distance = Math.abs(player.worldX - object.worldX) +
                            Math.abs(player.worldY - object.worldY);

                    if (distance < tileSize * 2) {
                        g2.setColor(Color.YELLOW);
                        g2.setFont(new Font("Arial", Font.BOLD, 16));
                        String hint = "SPACE drücken zum Öffnen";
                        FontMetrics fm = g2.getFontMetrics();
                        int hintX = (screenWidth - fm.stringWidth(hint)) / 2;
                        g2.drawString(hint, hintX, 50);
                        break;
                    }
                }
            }
        }

        g2.dispose();
    }
}