package FourConnect;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Connect Four";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_PLAYER1 = new Color(239, 105, 80);  // Red #EF6950
    public static final Color COLOR_PLAYER2 = new Color(64, 154, 225); // Blue #409AE1
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    private String playerName1;
    private String playerName2;

    public GameMain() {
        // Input player names
        playerName1 = JOptionPane.showInputDialog("Enter player name 1:");
        playerName2 = JOptionPane.showInputDialog("Enter player name 2:");
        if (playerName1 == null || playerName1.trim().isEmpty()) {
            playerName1 = "Player 1";
        }
        if (playerName2 == null || playerName2.trim().isEmpty()) {
            playerName2 = "Player 2";
        }

        // Start background music (if applicable)
        SoundEffect.BACKSOUND.loop();

        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int col = mouseX / Cell.SIZE;

                if (col >= 0 && col < Board.COLS) {
                    int row = -1;
                    for (int r = Board.ROWS - 1; r >= 0; r--) {
                        if (board.cells[r][col].content == Seed.NO_SEED) {
                            row = r;
                            break;
                        }
                    }

                    if (row != -1 && currentState == State.PLAYING) {
                        currentState = board.stepGame(currentPlayer, row, col);

                        // Play sound effects based on player
                        if (currentPlayer == Seed.CROSS) {
                            if (SoundEffect.PLAYER1 != null) SoundEffect.PLAYER1.play();
                        } else {
                            if (SoundEffect.PLAYER2 != null) SoundEffect.PLAYER2.play();
                        }

                        // Update the player turn and game status
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
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        initGame();
        newGame();
    }

    private void initGame() {
        board = new Board();
    }

    private void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon backgroundImage = new ImageIcon("C:\\Users\\user\\IdeaProjects\\Sudoku-Game\\src\\bgTTT.jpg");
        Image img = backgroundImage.getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
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

        // Draw player pieces using images (SpongeBob and Patrick)
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                Seed content = board.cells[row][col].content;
                if (content != Seed.NO_SEED) {
                    Image pieceImage = content.getImage();
                    if (pieceImage != null) {
                        int x = col * Cell.SIZE;
                        int y = row * Cell.SIZE;
                        g.drawImage(pieceImage, x, y, Cell.SIZE, Cell.SIZE, null);
                    }
                }
            }
        }

        // Update the status message based on the current state of the game
        if (currentState == State.PLAYING) {
            statusBar.setText((currentPlayer == Seed.CROSS ? playerName1 : playerName2) + "'s Turn");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setText(playerName1 + " Won! Click to restart.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setText(playerName2 + " Won! Click to restart.");
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
