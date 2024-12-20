/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #6
 * 1 - 5026231033 - Ayu Alfia Putri
 * 2 - 5026231034 - Antika Raya
 * 3 - 5026231106 - Nailah Qonitah Firdausa
 */

package TicTacToe;

public class AIPlayerMinimax extends AIPlayer {

    /** constructor */
    public AIPlayerMinimax(Board board) {
        super(board);
    }

    /** Minimax algorithm to determine the best move */
    @Override
    public int[] move() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    cells[row][col].content = mySeed;
                    int score = minimax(0, false);
                    cells[row][col].content = Seed.NO_SEED;
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[] {row, col};
                    }
                }
            }
        }

        return bestMove;
    }

    /** Minimax recursive function */
    private int minimax(int depth, boolean isMaximizing) {
        if (hasWon(mySeed)) return 10 - depth;
        if (hasWon(oppSeed)) return depth - 10;
        if (isFull()) return 0;

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    cells[row][col].content = isMaximizing ? mySeed : oppSeed;
                    int score = minimax(depth + 1, !isMaximizing);
                    cells[row][col].content = Seed.NO_SEED;
                    bestScore = isMaximizing ? Math.max(score, bestScore) : Math.min(score, bestScore);
                }
            }
        }

        return bestScore;
    }

    /** Check if the board is full */
    private boolean isFull() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Check if a seed has won */
    private boolean hasWon(Seed seed) {
        for (int row = 0; row < ROWS; row++) {
            if (cells[row][0].content == seed && cells[row][1].content == seed && cells[row][2].content == seed) {
                return true;
            }
        }
        for (int col = 0; col < COLS; col++) {
            if (cells[0][col].content == seed && cells[1][col].content == seed && cells[2][col].content == seed) {
                return true;
            }
        }
        if (cells[0][0].content == seed && cells[1][1].content == seed && cells[2][2].content == seed) {
            return true;
        }
        if (cells[0][2].content == seed && cells[1][1].content == seed && cells[2][0].content == seed) {
            return true;
        }
        return false;
    }
}