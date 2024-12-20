/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #6
 * 1 - 5026231033 - Ayu Alfia Putri
 * 2 - 5026231034 - Antika Raya
 * 3 - 5026231106 - Nailah Qonitah Firdausa
 */

package FourConnect;

import java.awt.*;

public class Cell {
    // Define named constants for drawing
    public static final int SIZE = 100; // cell width/height (square)
    // Symbols (cross/nought) are displayed inside a cell, with padding from border
    public static final int PADDING = SIZE / 5;
    public static final int SEED_SIZE = SIZE - PADDING * 2;

    // Define properties (package-visible)
    /** Content of this cell (Seed.EMPTY, Seed.CROSS, or Seed.NOUGHT) */
    Seed content;
    /** Row and column of this cell */
    int row, col;

    /** Constructor to initialize this cell with the specified row and col */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
    }

    /** Reset this cell's content to EMPTY, ready for new game */
    public void newGame() {
        content = Seed.NO_SEED;
    }

    /** Paint itself on the graphics canvas, given the Graphics context */
    public void paint(Graphics g, int cellSize) {

        int x = col * cellSize;
        int y = row * cellSize;

        // Draw based on content
        if (content == Seed.CROSS) {
            g.setColor(Color.RED);
            g.fillOval(x + 2, y + 2, cellSize - 4, cellSize - 4);
        } else if (content == Seed.NOUGHT) {
            g.setColor(Color.YELLOW);
            g.fillOval(x + 2, y + 2, cellSize - 4, cellSize - 4);
        }
    }
}