package TicTacToe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);  // Red #EF6950
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    private String playerName1;
    private String playerName2;

    private AIPlayer aiPlayer;
    private boolean playWithAI = false;

    public GameMain() {
        // Input mode and names
        String[] options = { "Player vs Player", "Player vs Computer" };
        int mode = JOptionPane.showOptionDialog(null, "Choose Game Mode", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (mode == 1) { // Player vs AI
            playWithAI = true;
            playerName1 = JOptionPane.showInputDialog("Enter player name:");
            playerName1 = (playerName1 == null || playerName1.trim().isEmpty()) ? "Player" : playerName1;
            playerName2 = "Computer";
        } else { // Player vs Player
            playWithAI = false;
            playerName1 = JOptionPane.showInputDialog("Enter player name 1:");
            playerName1 = (playerName1 == null || playerName1.trim().isEmpty()) ? "Player 1" : playerName1;
            playerName2 = JOptionPane.showInputDialog("Enter player name 2:");
            playerName2 = (playerName2 == null || playerName2.trim().isEmpty()) ? "Player 2" : playerName2;
        }

        // Start background music
        SoundEffect.BACKSOUND.loop();

        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentState == State.PLAYING) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();
                    int row = mouseY / Cell.SIZE;
                    int col = mouseX / Cell.SIZE;

                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = board.stepGame(currentPlayer, row, col);
                        updatePlayerState();

                        // Play sound for the current player move
                        if (currentPlayer == Seed.NOUGHT) {
                            SoundEffect.PLAYER1.play();  // Play sound for Player 1 (cross)
                        } else {
                            SoundEffect.PLAYER2.play();  // Play sound for Player 2 (nought)
                        }

                        if (playWithAI && currentState == State.PLAYING && currentPlayer == Seed.NOUGHT) {
                            handleAIMove();
                        }
                    }
                } else {
                    newGame();  // Restart the game if it's over
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
        if (playWithAI) {
            aiPlayer = new AIPlayerMinimax(board);
            aiPlayer.setSeed(Seed.NOUGHT);
        }
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

    private void updatePlayerState() {
        if (currentState == State.PLAYING) {
            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
        } else if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
            // Play the explosion sound when a player wins
            if (SoundEffect.EXPLOSION != null) {
                SoundEffect.EXPLOSION.play();
            }
        } else if (currentState == State.DRAW) {
            if (SoundEffect.GAME_OVER != null) {
                SoundEffect.GAME_OVER.play();
            }
        }
    }

    private void handleAIMove() {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Tambahkan jeda 1 detik
                if (currentState == State.PLAYING) {
                    int[] move = aiPlayer.move();
                    currentState = board.stepGame(currentPlayer, move[0], move[1]);
                    updatePlayerState();

                    // Play the AI player's sound after making a move
                    SoundEffect.AI_PLAYER.play(); // Play sound for AI player

                    repaint();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon backgroundImage = new ImageIcon("src/bgTTT.jpg"); //"C:\\Users\\user\\IdeaProjects\\Sudoku-Game\\src\\bgTTT.jpg"
        Image img = backgroundImage.getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);

        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? playerName1 + "'s Turn" : playerName2 + "'s Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerName1 + " Won! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerName2 + " Won! Click to play again.");
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setContentPane(new GameMain());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
