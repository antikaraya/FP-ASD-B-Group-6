package FourConnect;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Connect-Four";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    public GameMain() {
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        // Update game state
                        currentState = board.stepGame(currentPlayer, row, col);

                        // Play appropriate sound clip
                        if (currentPlayer == Seed.CROSS) {
                            SoundEffect.PLAYER1.play();
                        } else {
                            SoundEffect.PLAYER2.play();
                        }

                        if (currentState == State.PLAYING) {
                            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        } else if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
                            SoundEffect.EXPLOSION.play();
                        } else if (currentState == State.DRAW) {
                            SoundEffect.GAME_OVER.play();
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
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));

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
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        board.paint(g, Cell.SIZE);

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
            JFrame frame = new JFrame(TITLE);
            GameMain gamePanel = new GameMain();
            frame.setContentPane(gamePanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
