/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231033 - Ayu Alfia Putri
 * 2 - 5026231034 - Antika Raya
 * 3 - 5026231106 - Nailah Qonitah Firdausa
 */

package Sudoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60;   // Cell width/height in pixels
    public static final int BOARD_WIDTH  = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
    // Board width/height in pixels

    // Define properties
    /** The game board composes of 9x9 Cells (customized JTextFields) */
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    /** It also contains a Puzzle with array numbers and isGiven */
    private Puzzle puzzle = new Puzzle();
    private JPanel grid = new JPanel();
    private JLabel score = new JLabel("Your Score Now: 0");
    private int totalScore;
    private SudokuMain mainFrame;
    private int filledCells;

    /** Constructor */
    public GameBoardPanel(SudokuMain mainFrame, int filledCells) {
        this.mainFrame = mainFrame;
        this.filledCells = filledCells;
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));  // JPanel

        // Allocate the 2D array of Cell, and added into JPanel.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);

                // Tentukan ketebalan garis berdasarkan posisi sel
                int top = (row % 3 == 0) ? 3 : 1;    // Garis tebal di atas subgrid
                int left = (col % 3 == 0) ? 3 : 1;   // Garis tebal di kiri subgrid
                int bottom = (row == SudokuConstants.GRID_SIZE - 1) ? 3 : 1;  // Garis tebal bawah
                int right = (col == SudokuConstants.GRID_SIZE - 1) ? 3 : 1;   // Garis tebal kanan

                // Tambahkan border dengan ketebalan sesuai posisi
                cells[row][col].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                super.add(cells[row][col]);   // Tambahkan ke JPanel
            }
        }

        // [TODO 3] Allocate a common listener as the ActionEvent listener for all the
        //  Cells (JTextFields)
        CellInputListener listener = new CellInputListener();

        // [TODO 4] Adds this common listener to all editable cells
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);   // For all editable rows and cols
                }
            }
        }

        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    /**
     * Generate a new puzzle; and reset the game board of cells based on the puzzle.
     * You can call this method to start a new game.
     */

    public void newGame() {
        puzzle.newPuzzle(filledCells);
        totalScore = 0;
        updateScore();

        // Initialize all the 9x9 cells, based on the puzzle.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }

    /**
     * Return true if the puzzle is solved
     * i.e., none of the cell have status of TO_GUESS or WRONG_GUESS
     */
    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    // [TODO 2] Define a Listener Inner Class for all the editable Cells
    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get a reference of the JTextField that triggers this action event
            Cell sourceCell = (Cell)e.getSource();
            // Retrieve the int entered
            int numberIn = Integer.parseInt(sourceCell.getText());
            // For debugging
            System.out.println("You entered " + numberIn);

            /*
             * [TODO 5] (later - after TODO 3 and 4)
             * Check the numberIn against sourceCell.number.
             * Update the cell status sourceCell.status,
             * and re-paint the cell via sourceCell.paint().
             */
            // Validate the input
            if (isConflict(sourceCell, numberIn)) {
                sourceCell.setBackground(Color.RED); // Highlight conflicting cell
                Timer timer = new Timer(1000, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        sourceCell.paint(); // Reset the color back
                    }
                });
                timer.setRepeats(false);
                timer.start(); // Revert the color after 1 second
                showConflictWarning(); // Show conflict warning
            } else {
                // No conflict, check if correct
                if (numberIn == sourceCell.number) {
                    sourceCell.status = CellStatus.CORRECT_GUESS;
                    totalScore += 5; // Add score if the guess is correct
                } else {
                    sourceCell.status = CellStatus.WRONG_GUESS;
                }
                sourceCell.paint(); // Update the cell display
            }

            /*
             * [TODO 6] (later)
             * Check if the player has solved the puzzle after this move,
             *   by calling isSolved(). Put up a congratulation JOptionPane, if so.
             */
            if (isSolved()){
                JOptionPane.showMessageDialog(null, "Congratulation! Your Total Score is " + totalScore);
            }
            updateScore();
            mainFrame.updateScoreLabel();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Set warna garis
        g2d.setColor(Color.BLACK);

        // Ukuran grid
        int gridSize = SudokuConstants.GRID_SIZE; // 9
        int cellSize = CELL_SIZE; // 60 (sudah didefinisikan)

        // Menggambar garis horizontal dan vertikal
        for (int i = 0; i <= gridSize; i++) {
            // Garis vertikal
            g2d.setStroke(i % 3 == 0 ? new BasicStroke(3) : new BasicStroke(1)); // Tebal di setiap 3 kolom
            g2d.drawLine(i * cellSize, 0, i * cellSize, gridSize * cellSize);

            // Garis horizontal
            g2d.setStroke(i % 3 == 0 ? new BasicStroke(3) : new BasicStroke(1)); // Tebal di setiap 3 baris
            g2d.drawLine(0, i * cellSize, gridSize * cellSize, i * cellSize);
        }
    }

    public int getTotalScore() {
        return totalScore;
    }
    private void updateScore(){
        score.setText("Your Score Now: " + totalScore);
    }

    private boolean isConflict(Cell sourceCell, int numberIn) {
        // Check if the number already exists in the same row, column, or sub-grid
        // Check row
        for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {
            if (cells[sourceCell.row][col].number == numberIn) {
                return true; // Conflict in the same row
            }
        }

        // Check column
        for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
            if (cells[row][sourceCell.col].number == numberIn) {
                return true; // Conflict in the same column
            }
        }

        // Check sub-grid (3x3 grid)
        int subGridRow = (sourceCell.row / 3) * 3;
        int subGridCol = (sourceCell.col / 3) * 3;
        for (int r = subGridRow; r < subGridRow + 3; r++) {
            for (int c = subGridCol; c < subGridCol + 3; c++) {
                if (cells[r][c].number == numberIn) {
                    return true; // Conflict in the sub-grid
                }
            }
        }

        return false; // No conflict
    }

    private void showConflictWarning() {
        JOptionPane.showMessageDialog(null, "This number conflicts with another number in the puzzle.",
                "Conflict Detected", JOptionPane.WARNING_MESSAGE);
    }
}