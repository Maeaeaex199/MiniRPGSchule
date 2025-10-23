package tile;

import gamePanel.GamePanel;

import java.awt.*;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        // Map passend zu den World-Dimensionen anlegen
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        // Tiles laden
        getTileImage();

        // Map laden
        loadMap();
    }

    // Java
// ... innerhalb der Klasse TileManager ...

// Hilfsfunktion: versucht zuerst Klassenpfad, dann Dateisystem
private java.io.InputStream openResource(String[] candidates) {
    // 1) Klassenpfad versuchen
    for (String p : candidates) {
        String cp = p.startsWith("/") ? p : "/" + p;
        java.io.InputStream is = getClass().getResourceAsStream(cp);
        if (is != null) {
            return is;
        }
    }
    // 2) Dateisystem-Fallback (für Entwicklung)
    for (String p : candidates) {
        String fs = p.startsWith("/") ? p.substring(1) : p;
        java.io.File f = new java.io.File(fs);
        if (f.isFile()) {
            try {
                return new java.io.FileInputStream(f);
            } catch (java.io.FileNotFoundException ignored) {
            }
        }
    }
    return null;
}

private java.awt.image.BufferedImage loadImage(String[] candidates) {
    try (java.io.InputStream is = openResource(candidates)) {
        if (is == null) return null;
        return javax.imageio.ImageIO.read(is);
    } catch (java.io.IOException e) {
        throw new RuntimeException("Fehler beim Laden eines Bildes", e);
    }
}

public void getTileImage() {
    // Bevorzugt Klassenpfad (mit führendem Slash), optionaler Fallback ohne Slash
    String[][] candidates = new String[][]{
            {"/tiles/grass00.png", "tiles/grass00.png"},
            {"/tiles/wall.png", "tiles/wall.png"},
            {"/tiles/water01.png", "tiles/water01.png"},
            {"/tiles/earth.png", "tiles/earth.png"},
            {"/tiles/tree.png", "tiles/tree.png"},
            {"/tiles/road01.png", "tiles/road01.png"}
    };

    tile = new Tile[candidates.length];

    for (int i = 0; i < candidates.length; i++) {
        tile[i] = new Tile();
        tile[i].image = loadImage(candidates[i]);
        if (tile[i].image == null) {
            System.err.println("Konnte Tile-Bild nicht finden: " + java.util.Arrays.toString(candidates[i]));
        }
    }

    // Beispiel: Kollision für bestimmte Tiles setzen (falls benötigt)
    if (tile.length > 1) tile[1].collision = true; // wall
    if (tile.length > 2) tile[2].collision = true; // water
    if (tile.length > 4) tile[4].collision = true; // tree
}

public void loadMap() {
    // Unterstützt mehrere übliche Orte/Namen
    String[] mapCandidates = new String[]{
            "/maps/map.txt",
            "maps/map.txt",
            "/map.txt",
            "map.txt"
    };

    try (java.io.InputStream is = openResource(mapCandidates)) {
        if (is == null) {
            StringBuilder sb = new StringBuilder("Mapdatei nicht gefunden. Getestete Pfade:\n");
            for (String p : mapCandidates) sb.append(" - ").append(p).append('\n');
            throw new IllegalStateException(sb.toString());
        }

        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {

            for (int row = 0; row < gp.maxWorldRow; row++) {
                String line = br.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] numbers = line.split("\\s+");
                for (int col = 0; col < gp.maxWorldCol; col++) {
                    int num = 0;
                    if (col < numbers.length) {
                        try {
                            num = Integer.parseInt(numbers[col]);
                        } catch (NumberFormatException ignored) {
                            num = 0; // Fallback, wenn ungültig
                        }
                    }
                    // Annahme: mapTileNum[col][row] existiert bereits in dieser Klasse
                    mapTileNum[col][row] = num;
                }
            }
        }
    } catch (java.io.IOException e) {
        throw new RuntimeException("Fehler beim Laden der Map", e);
    }
}

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (tileNum < 0 || tileNum >= tile.length) {
                tileNum = 0;
            }

            Image img = tile[tileNum].image;
            if (img != null) {
                g2.drawImage(img, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}