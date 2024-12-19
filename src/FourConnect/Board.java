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

public class Board {
    public static final int ROWS = 6; // Adjust board dimensions.
    public static final int COLS = 7;

    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;

    public static final int GRID_WIDTH = 8;  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // grid lines
    public static final int Y_OFFSET = 1;  // Fine tune for better display
    public static final int MAX_CELL_SIZE = 50; // Ukuran maksimal kotak

    Cell[][] cells;

    /**
     * Constructor to initialize the game board
     */
    public Board() {
        initGame();
    }

    /**
     * Initialize the game objects (run once)
     */
    public void initGame() {
        cells = new Cell[ROWS][COLS]; // allocate the array
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

//    /** Reset the game board, ready for new game */
//    public void newGame() {
//        for (int row = 0; row < ROWS; ++row) {
//            for (int col = 0; col < COLS; ++col) {
//                cells[row][col].newGame(); // clear the cell content
//            }
//        }
//    }

//    public State stepGame(Seed player, int selectedRow, int selectedCol) {
//        // Update game board
//        cells[selectedRow][selectedCol].content = player;
//
//        // Compute and return the new game state
//        if (cells[selectedRow][0].content == player  // 3-in-the-row
//                && cells[selectedRow][1].content == player
//                && cells[selectedRow][2].content == player
//                || cells[0][selectedCol].content == player // 3-in-the-column
//                && cells[1][selectedCol].content == player
//                && cells[2][selectedCol].content == player
//                || selectedRow == selectedCol     // 3-in-the-diagonal
//                && cells[0][0].content == player
//                && cells[1][1].content == player
//                && cells[2][2].content == player
//                || selectedRow + selectedCol == 2 // 3-in-the-opposite-diagonal
//                && cells[0][2].content == player
//                && cells[1][1].content == player
//                && cells[2][0].content == player) {
//            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
//        } else {
//            // Nobody win. Check for DRAW (all cells occupied) or PLAYING.
//            for (int row = 0; row < ROWS; ++row) {
//                for (int col = 0; col < COLS; ++col) {
//                    if (cells[row][col].content == Seed.NO_SEED) {
//                        return State.PLAYING; // still have empty cells
//                    }
//                }
//            }
//            return State.DRAW; // no empty cell, it's a draw
//        }
//    }

    public State stepGame(Seed player, int selectedCol) {
        // Place the piece at the lowest available row in the selected column
        for (int row = ROWS - 1; row >= 0; --row) {
            if (cells[row][selectedCol].content == Seed.NO_SEED) {
                cells[row][selectedCol].content = player;
                if (hasWon(player, row, selectedCol)) {
                    return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
                }
                return checkDraw() ? State.DRAW : State.PLAYING;
            }
        }
        return State.PLAYING; // No valid move
    }

    private boolean checkDraw() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasWon(Seed player, int row, int col) {
        return checkLine(player, row, col, 0, 1)  // Horizontal
                || checkLine(player, row, col, 1, 0)  // Vertical
                || checkLine(player, row, col, 1, 1)  // Diagonal
                || checkLine(player, row, col, 1, -1); // Anti-diagonal
    }

    private boolean checkLine(Seed player, int row, int col, int deltaRow, int deltaCol) {
        int count = 0;
        for (int d = -3; d <= 3; ++d) {
            int r = row + d * deltaRow;
            int c = col + d * deltaCol;
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS && cells[r][c].content == player) {
                if (++count == 4) return true;
            } else {
                count = 0;
            }
        }
        return false;
    }

    /**
     * Paint itself on the graphics canvas, given the Graphics context
     */
    public void paint(Graphics g, int cellSize) {
        // Draw the grid-lines
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }

        // Draw all the cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g, cellSize);  // ask the cell to paint itself
            }
        }
    }
}