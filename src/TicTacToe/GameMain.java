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
    private JLabel player1TimeLabel;
    private JLabel player2TimeLabel;

    private String playerName1;
    private String playerName2;

    private AIPlayer aiPlayer;
    private boolean playWithAI = false;

    // Time-related variables
    private int turnTimeLimit; // Time per turn in seconds
    private int player1TimeLeft; // Player 1 total time in seconds
    private int player2TimeLeft; // Player 2 total time in seconds
    private Timer turnTimer; // Timer for turn countdown
    private Timer gameTimer; // Timer for total game time
    private int secondsLeft; // Seconds remaining in current turn

    public GameMain() {
        // Input mode and names
        String[] options = { "Player vs Player", "Player vs Computer" };
        int mode = JOptionPane.showOptionDialog(null, "Choose Game Mode", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        // Add dialog to select turn time and player time
        String[] turnTimes = { "10s", "20s", "30s", "40s", "Unlimited" };
        String selectedTurnTime = (String) JOptionPane.showInputDialog(null, "Select time per turn:",
                "Time Per Turn", JOptionPane.QUESTION_MESSAGE, null, turnTimes, turnTimes[0]);
        turnTimeLimit = getTimeInSeconds(selectedTurnTime);  // Convert to seconds

        String[] playerTimes = { "1m", "2m", "3m", "4m", "Unlimited" };
        String selectedPlayerTime = (String) JOptionPane.showInputDialog(null, "Select minutes per player:",
                "Minutes Per Player", JOptionPane.QUESTION_MESSAGE, null, playerTimes, playerTimes[0]);
        int totalPlayerTime = getTimeInMinutes(selectedPlayerTime);  // Convert to minutes

        player1TimeLeft = totalPlayerTime * 60; // Convert to seconds
        player2TimeLeft = totalPlayerTime * 60; // Convert to seconds

        // Input player names
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
                    // Check if it's the current player's turn
                    if ((currentPlayer == Seed.CROSS && currentState == State.PLAYING) ||
                            (currentPlayer == Seed.NOUGHT && currentState == State.PLAYING)) {
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
                                SoundEffect.PLAYER1.play();  // Player 1 sound (Cross)
                            } else {
                                SoundEffect.PLAYER2.play();  // Player 2 sound (Nought)
                            }

                            // Handle AI move if playing with AI
                            if (playWithAI && currentState == State.PLAYING && currentPlayer == Seed.NOUGHT) {
                                handleAIMove();
                            }
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

        // Time labels for players
        player1TimeLabel = new JLabel();
        player1TimeLabel.setFont(FONT_STATUS);
        player1TimeLabel.setPreferredSize(new Dimension(150, 30));
        player1TimeLabel.setHorizontalAlignment(JLabel.CENTER);
        player1TimeLabel.setBackground(COLOR_BG_STATUS);
        player1TimeLabel.setOpaque(true);

        player2TimeLabel = new JLabel();
        player2TimeLabel.setFont(FONT_STATUS);
        player2TimeLabel.setPreferredSize(new Dimension(150, 30));
        player2TimeLabel.setHorizontalAlignment(JLabel.CENTER);
        player2TimeLabel.setBackground(COLOR_BG_STATUS);
        player2TimeLabel.setOpaque(true);

        JPanel timePanel = new JPanel();
        timePanel.setLayout(new GridLayout(1, 2));
        timePanel.add(player1TimeLabel);
        timePanel.add(player2TimeLabel);

        super.setLayout(new BorderLayout());
        super.add(timePanel, BorderLayout.NORTH);
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

        // Initialize turn timer
        turnTimer = new Timer(1000, e -> {
            if (secondsLeft > 0) {
                secondsLeft--;
                updateTimeDisplay();
            } else {
                changeTurn(); // If time is up, switch turn
            }
        });

        // Initialize game timer for tracking total time per player
        gameTimer = new Timer(1000, e -> {
            if (currentPlayer == Seed.CROSS && player1TimeLeft > 0) {
                player1TimeLeft--;
            } else if (currentPlayer == Seed.NOUGHT && player2TimeLeft > 0) {
                player2TimeLeft--;
            }
            updateTimeDisplay();
        });
        gameTimer.start();
    }

    private void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        secondsLeft = turnTimeLimit; // Reset turn timer
        turnTimer.start(); // Start the timer for turn time
    }

    private void updatePlayerState() {
        if (currentState == State.PLAYING) {
            // Only allow the next player to make a move
            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
            secondsLeft = turnTimeLimit; // Reset turn timer for the new player
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

    private void updateTimeDisplay() {
        // Update the time labels for both players
        player1TimeLabel.setText(String.format("%s: %02d:%02d", playerName1, player1TimeLeft / 60, player1TimeLeft % 60));
        player2TimeLabel.setText(String.format("%s: %02d:%02d", playerName2, player2TimeLeft / 60, player2TimeLeft % 60));

        // Update turn timer for current player
        if (secondsLeft >= 0) {
            statusBar.setText(String.format("%s's Turn: %02d:%02d",
                    currentPlayer == Seed.CROSS ? playerName1 : playerName2,
                    secondsLeft / 60, secondsLeft % 60));
        }
    }

    private void changeTurn() {
        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
        secondsLeft = turnTimeLimit; // Reset turn timer for the next player
        turnTimer.restart(); // Restart the turn timer for the new player
    }

    private int getTimeInSeconds(String selectedTime) {
        switch (selectedTime) {
            case "10s":
                return 10;
            case "20s":
                return 20;
            case "30s":
                return 30;
            case "40s":
                return 40;
            case "Unlimited":
                return Integer.MAX_VALUE;
            default:
                return 0;
        }
    }

    private int getTimeInMinutes(String selectedTime) {
        switch (selectedTime) {
            case "1m":
                return 1;
            case "2m":
                return 2;
            case "3m":
                return 3;
            case "4m":
                return 4;
            case "Unlimited":
                return Integer.MAX_VALUE;
            default:
                return 0;
        }
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
