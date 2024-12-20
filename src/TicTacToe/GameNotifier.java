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

import javax.swing.*;

public class GameNotifier {
    public static void notifyWinner(State state, String playerName1, String playerName2) {
        SwingUtilities.invokeLater(() -> {
            if (state == State.CROSS_WON) {
                JOptionPane.showMessageDialog(null, playerName1 + " is the winner!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            } else if (state == State.NOUGHT_WON) {
                JOptionPane.showMessageDialog(null, playerName2 + " is the winner!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            } else if (state == State.DRAW) {
                JOptionPane.showMessageDialog(null, "It's a Draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
