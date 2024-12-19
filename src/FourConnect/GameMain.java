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

    // Define named constants for the drawing graphics
    public static final String TITLE = "Connect-Four";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);  // Red #EF6950
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    // Define game objects
    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    // Player names
    private String playerName1;
    private String playerName2;

    /**
     * Constructor to setup the UI and game components
     */
    public GameMain() {

        // Input player's name
        playerName1 = JOptionPane.showInputDialog("Enter player name 1:");
        playerName2 = JOptionPane.showInputDialog("Enter player name 2:");
        if (playerName1 == null || playerName1.trim().isEmpty()) {
            playerName1 = "Player 1";
        }

        if (playerName2 == null || playerName2.trim().isEmpty()) {
            playerName2 = "Player 2";
        }

        // Start background music
        SoundEffect.BACKSOUND.loop();

        // This JPanel fires MouseEvent
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
                int mouseX = e.getX();
//                int mouseY = e.getY();
                // Get the row and column clicked
//                int row = mouseY / FourConnect.Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (col >= 0 && col < Board.COLS) {
                        // Update cells[][] and return the new game state after the move
                        currentState = board.stepGame(currentPlayer, col);

                        // Play appropriate sound clip
                        if (currentPlayer == Seed.CROSS) {
                            SoundEffect.PLAYER1.play();
                        } else {
                            SoundEffect.PLAYER2.play();
                        }

                        if (currentState == State.PLAYING) {
                            // Switch player
                            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        } else if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
                            SoundEffect.EXPLOSION.play();
                        } else if (currentState == State.DRAW) {
                            SoundEffect.GAME_OVER.play();
                        }
                    }
                } else {        // game over
                    newGame();  // restart the game
                }
                // Refresh the drawing canvas
                repaint();  // Callback paintComponent().
            }
        });

        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
        super.setPreferredSize(new Dimension(FourConnect.Board.CANVAS_WIDTH, FourConnect.Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // Set up Game
        initGame();
        newGame();
    }

    /**
     * Initialize the game (run once)
     */
    public void initGame() {
        board = new FourConnect.Board();  // allocate the game-board
    }

    /**
     * Reset the game-board contents and the current-state, ready for new game
     */
    public void newGame() {
//        for (int row = 0; row < FourConnect.Board.ROWS; ++row) {
//            for (int col = 0; col < Board.COLS; ++col) {
//                board.cells[row][col].content = Seed.NO_SEED; // all cells empty
//            }
//        }
        board.initGame();
        currentPlayer = Seed.CROSS;    // cross plays first
        currentState = State.PLAYING;  // ready to play
    }

    /**
     * Custom painting codes on this JPanel
     */
    @Override
    public void paintComponent(Graphics g) {  // Callback via repaint()
        super.paintComponent(g);

        // Calculate dynamic cell size based on current panel size
        int cellWidth = getWidth() / Board.COLS;
        int cellHeight = getHeight() / Board.ROWS;
        int cellSize = Math.min(cellWidth, cellHeight);
        cellSize = Math.min(cellSize, Board.MAX_CELL_SIZE);

        setBackground(COLOR_BG); // set its background color
        board.paint(g, cellSize);  // ask the game board to paint itself

        // Print status-bar message
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

    /**
     * The entry "main" method
     */
    public static void main(String[] args) {
        // Run GUI construction codes in Event-Dispatching thread for thread safety
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            GameMain gamePanel = new GameMain();

            // Set frame size relative to screen size
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = screenSize.width * 3 / 4; // 75% of screen width
            int height = screenSize.height * 3 / 4; // 75% of screen height
            frame.setSize(new Dimension(width, height));

            JScrollPane scrollPane = new JScrollPane(gamePanel);

            // Set the content-pane of the JFrame to an instance of main JPanel
            frame.setContentPane(gamePanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.pack();
            frame.setLocationRelativeTo(null); // center the application window
            frame.setVisible(true);            // show it
        });
    }
}