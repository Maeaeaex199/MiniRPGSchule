package gamePanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import collisionChecker.CollisionChecker;
import entity.Player;
import keyHandler.KeyHandler;
import object.SuperObject;
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
                
                // Nur updaten wenn nicht pausiert
                if (!paused) {
                    update();
                }
                
                // Immer repainten (für Pause-Overlay)
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
            togglePause();
            lastPauseToggleTime = currentTime;
        }
        
        prevEscapePressed = escPressed;
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

        g2.dispose();
    }
}