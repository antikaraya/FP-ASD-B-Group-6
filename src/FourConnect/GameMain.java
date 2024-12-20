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
import java.util.Random;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Connect-Four";
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
    private boolean isPlayer2AI;

    public GameMain() {
        // Show game mode selection dialog
        String[] options = {"Player vs Player", "Player vs Computer"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Select Game Mode:",
                "Game Mode",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            isPlayer2AI = false;
            playerName1 = JOptionPane.showInputDialog("Enter player name 1:");
            playerName2 = JOptionPane.showInputDialog("Enter player name 2:");
            if (playerName1 == null || playerName1.trim().isEmpty()) {
                playerName1 = "Player 1";
            }
            if (playerName2 == null || playerName2.trim().isEmpty()) {
                playerName2 = "Player 2";
            }
        } else if (choice == 1) {
            isPlayer2AI = true;
            playerName1 = JOptionPane.showInputDialog("Enter your name:");
            playerName2 = "AI";
            if (playerName1 == null || playerName1.trim().isEmpty()) {
                playerName1 = "Player 1";
            }
        } else {
            System.exit(0); // Exit if no choice is made
        }

        // Start background music (if applicable)
        SoundEffect.BACKSOUND.loop();

        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentState == State.PLAYING) {
                    if (currentPlayer == Seed.CROSS || !isPlayer2AI) {
                        int mouseX = e.getX();
                        int col = mouseX / Cell.SIZE;
                        playerMove(col);
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

        // Create button panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel buttonRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnReset = new JButton("Reset Game");
        JButton btnExit = new JButton("Exit");

        // Add action listeners to buttons
        btnReset.addActionListener(e -> {
            newGame();
            repaint();
        });

        btnExit.addActionListener(e -> System.exit(0));

        // Add buttons to the panel
        buttonRight.add(btnReset);
        buttonRight.add(btnExit);

        // Style button panel
        buttonPanel.add(buttonRight, BorderLayout.EAST);

        // Combine statusBar and buttonPanel into southPanel
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(statusBar, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.EAST);

        super.setLayout(new BorderLayout());
//        super.add(statusBar, BorderLayout.PAGE_END);
        super.add(southPanel, BorderLayout.SOUTH);
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

    private void playerMove(int col) {
        if (col >= 0 && col < Board.COLS) {
            int row = -1;
            for (int r = Board.ROWS - 1; r >= 0; r--) {
                if (board.cells[r][col].content == Seed.NO_SEED) {
                    row = r;
                    break;
                }
            }

            if (row != -1) {
                currentState = board.stepGame(currentPlayer, row, col);

                if (currentPlayer == Seed.CROSS) {
                    if (SoundEffect.PLAYER1 != null) SoundEffect.PLAYER1.play();
                } else {
                    if (SoundEffect.PLAYER2 != null) SoundEffect.PLAYER2.play();
                }

                if (currentState == State.PLAYING) {
                    currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;

                    // Trigger AI move if applicable
                    if (isPlayer2AI && currentPlayer == Seed.NOUGHT) {
                        new Timer(500, e -> {
                            ((Timer) e.getSource()).stop();
                            aiMove();
                            repaint();
                        }).start();
                    }
                } else if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON || currentState == State.DRAW) {
                    GameNotifier.notifyWinner(currentState, playerName1, playerName2);
                    if (SoundEffect.EXPLOSION != null && currentState != State.DRAW) SoundEffect.EXPLOSION.play();
                    if (SoundEffect.GAME_OVER != null && currentState == State.DRAW) SoundEffect.GAME_OVER.play();
                }
            }
        }
    }

    private void aiMove() {
        Random random = new Random();
        int col;

        do {
            col = random.nextInt(Board.COLS);
        } while (!isValidMove(col));

        playerMove(col);
    }

    private boolean isValidMove(int col) {
        return board.cells[0][col].content == Seed.NO_SEED;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon backgroundImage = new ImageIcon("src/bgTTT.jpg");
        Image img = backgroundImage.getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        board.paint(g, Cell.SIZE);

        g.setColor(Color.BLACK);
        for (int row = 1; row < Board.ROWS; row++) {
            int y = row * Cell.SIZE;
            g.drawLine(0, y, Board.CANVAS_WIDTH, y);
        }
        for (int col = 1; col < Board.COLS; col++) {
            int x = col * Cell.SIZE;
            g.drawLine(x, 0, x, Board.CANVAS_HEIGHT);
        }

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
