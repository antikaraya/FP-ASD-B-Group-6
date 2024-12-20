package FourConnect;

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
