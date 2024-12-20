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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

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
    private JLabel timerLabel;

    private String playerName1;
    private String playerName2;

    private AIPlayer aiPlayer;
    private boolean playWithAI = false;

    private Timer gameTimer;
    private int remainingTime;
    private boolean isTimerRunning = false;

    private String initialSelectedTime;
    private int initialMode;

    public GameMain() {
        // Input mode and names
        String[] options = { "Player vs Player", "Player vs Computer" };
        initialMode = JOptionPane.showOptionDialog(null, "Choose Game Mode", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (initialMode == 1) { // Player vs AI
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

        // Select timer duration using dropdown
        JComboBox<String> timeDropdown = new JComboBox<>(new String[]{ "1 Minute", "2 Minutes", "3 Minutes", "4 Minutes", "Unlimited" });
        JPanel timePanel = new JPanel();
        timePanel.add(new JLabel("Select Game Timer:"));
        timePanel.add(timeDropdown);
        int result = JOptionPane.showConfirmDialog(null, timePanel, "Game Timer", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            initialSelectedTime = (String) timeDropdown.getSelectedItem();
            remainingTime = calculateTime(initialSelectedTime);
        } else {
            System.exit(0); // Exit if user cancels
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

        // Timer label setup
        timerLabel = new JLabel(formatTime(remainingTime));
        timerLabel.setFont(FONT_STATUS);
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        timerLabel.setOpaque(true);
        timerLabel.setBackground(COLOR_BG_STATUS);
        timerLabel.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, 30));

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
        super.add(timerLabel, BorderLayout.NORTH);
        super.add(southPanel, BorderLayout.SOUTH);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 60));
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
        remainingTime = calculateTime(initialSelectedTime); // Reset timer to the initially selected value
        restartTimer();
    }

    private int calculateTime(String selectedTime) {
        return switch (selectedTime) {
            case "1 Minute" -> 60;
            case "2 Minutes" -> 120;
            case "3 Minutes" -> 180;
            case "4 Minutes" -> 240;
            default -> Integer.MAX_VALUE; // Unlimited
        };
    }

    private void restartTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        startTimer();
    }

    private void startTimer() {
        if (remainingTime == Integer.MAX_VALUE) return; // Unlimited mode, no timer

        gameTimer = new Timer();
        isTimerRunning = true;
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (remainingTime > 0) {
                    remainingTime--;
                    SwingUtilities.invokeLater(() -> timerLabel.setText(formatTime(remainingTime)));
                } else {
                    gameTimer.cancel();
                    SwingUtilities.invokeLater(() -> showTimeUpDialog());
                    currentState = State.DRAW;
                    repaint();
                }
            }
        }, 0, 1000);
    }

    private void showTimeUpDialog() {
        String[] options = { "Exit", "Play Again", "New Game" };
        int choice = JOptionPane.showOptionDialog(null, "Time's up! What do you want to do?", "Time's Up",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);

        switch (choice) {
            case 0 -> System.exit(0); // Exit
            case 1 -> { // Play Again
                remainingTime = calculateTime(initialSelectedTime);
                newGame();
            }
            case 2 -> { // New Game
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                frame.dispose();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    JFrame newFrame = new JFrame(TITLE);
                    newFrame.setContentPane(new GameMain());
                    newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    newFrame.pack();
                    newFrame.setLocationRelativeTo(null);
                    newFrame.setVisible(true);
                });
            }
        }
    }

    private void stopTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        isTimerRunning = false;
    }

    private String formatTime(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void updatePlayerState() {
        if (currentState == State.PLAYING) {
            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
        } else if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON || currentState == State.DRAW) {
            stopTimer();
            GameNotifier.notifyWinner(currentState, playerName1, playerName2);

            if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
                if (SoundEffect.EXPLOSION != null) {
                    SoundEffect.EXPLOSION.play();
                }
            } else if (currentState == State.DRAW) {
                if (SoundEffect.GAME_OVER != null) {
                    SoundEffect.GAME_OVER.play();
                }
            }
        }
    }

    private void handleAIMove() {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Add a 1-second delay
                if (currentState == State.PLAYING) {
                    int[] move = aiPlayer.move();
                    currentState = board.stepGame(currentPlayer, move[0], move[1]);
                    updatePlayerState();

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
        ImageIcon backgroundImage = new ImageIcon("src/bgTTT.jpg");
        Image img = backgroundImage.getImage();

        int boardWidth = Board.CANVAS_WIDTH;
        int boardHeight = Board.CANVAS_HEIGHT;

        int xOffset = (getWidth() - boardWidth) / 2;
        int yOffset = (getHeight() - boardHeight) / 2;

        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(xOffset, yOffset);

        board.paint(g2d);

        g2d.translate(-xOffset, -yOffset);

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
