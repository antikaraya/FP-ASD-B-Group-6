package FourConnect;

import java.awt.*;

public class Cell {
    public static final int SIZE = 100;
    public static final int PADDING = SIZE / 10;

    Seed content;
    int row, col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
    }

    public void newGame() {
        content = Seed.NO_SEED;
    }

    public void paint(Graphics g, int cellSize) {
        int x = col * cellSize;
        int y = row * cellSize;

        if (content != Seed.NO_SEED) {
            Image image = content.getImage();
            if (image != null) {
                g.drawImage(image, x + PADDING, y + PADDING, cellSize - 2 * PADDING, cellSize - 2 * PADDING, null);
            }
        }
    }
}
