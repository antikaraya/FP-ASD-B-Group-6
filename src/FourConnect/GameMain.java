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
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Connect-Four";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);
    public static final int STATUS_BAR_HEIGHT = 30;

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    public GameMain() {
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int col = mouseX / Cell.SIZE;

                if (col >= 0 && col < Board.COLS) {
                    // Cari row terendah yang tersedia di kolom tersebut
                    int row = -1;
                    for (int r = Board.ROWS - 1; r >= 0; r--) {
                        if (board.cells[r][col].content == Seed.NO_SEED) {
                            row = r;
                            break;
                        }
                    }

                    if (row != -1 && currentState == State.PLAYING) {
                        currentState = board.stepGame(currentPlayer, row, col);

                        // Mainkan efek suara jika tersedia
                        if (currentPlayer == Seed.CROSS) {
                            if (SoundEffect.PLAYER1 != null) SoundEffect.PLAYER1.play();
                        } else {
                            if (SoundEffect.PLAYER2 != null) SoundEffect.PLAYER2.play();
                        }

                        // Perbarui status
                        if (currentState == State.PLAYING) {
                            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        } else if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
                            if (SoundEffect.EXPLOSION != null) SoundEffect.EXPLOSION.play();
                        } else if (currentState == State.DRAW) {
                            if (SoundEffect.GAME_OVER != null) SoundEffect.GAME_OVER.play();
                        }
                    }
                } else {
                    newGame();
                }
                repaint();
            }
        });

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + STATUS_BAR_HEIGHT));

        initGame();
        newGame();
    }

    public void initGame() {
        board = new Board();
    }

    public void newGame() {
        board.initGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        statusBar.setText("Player 1's Turn");
        System.out.println("Game restarted!");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        board.paint(g, Cell.SIZE);

        // Draw grid lines to enhance visibility
        g.setColor(Color.BLACK);
        for (int row = 1; row < Board.ROWS; row++) {
            int y = row * Cell.SIZE;
            g.drawLine(0, y, Board.CANVAS_WIDTH, y);
        }
        for (int col = 1; col < Board.COLS; col++) {
            int x = col * Cell.SIZE;
            g.drawLine(x, 0, x, Board.CANVAS_HEIGHT);
        }

        if (currentState == State.PLAYING) {
            statusBar.setText((currentPlayer == Seed.CROSS ? "Player 1" : "Player 2") + "'s Turn");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setText("Player 1 Won! Click to restart.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setText("Player 2 Won! Click to restart.");
        } else if (currentState == State.DRAW) {
            statusBar.setText("It's a Draw! Click to restart.");
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JFrame frame = new JFrame(TITLE);
            GameMain gamePanel = new GameMain();
            frame.setContentPane(gamePanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
