/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231033 - Ayu Alfia Putri
 * 2 - 5026231034 - Antika Raya
 * 3 - 5026231106 - Nailah Qonitah Firdausa
 */

package Sudoku;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The main Sudoku program
 */
public class SudokuMain extends JFrame {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // private variables
    GameBoardPanel board;
    JButton btnNewGame = new JButton("New Game");
    JButton btnExit = new JButton("Exit");
    JButton btnReset = new JButton("Reset Game");
    JLabel scoreLabel = new JLabel();
    JLabel timerLabel = new JLabel("Timer: 0 seconds");
    String name;

    // Declare variable
    private Timer timer;
    private int seconds;
    private int totalScore;

    JComboBox<String> levelBox;
    int filledCells; // Menyimpan jumlah puzzle berdasarkan level yang diinginkan.

    // Constructor
    public SudokuMain() {
        // Menambahkan halaman Selamat Datang sebelum bermain
//        welcomeScreen();

        // User choose level and enter their name
        name = JOptionPane.showInputDialog(this, "Your Name");
        String[] level = {"Easy", "Intermediate", "Difficult"};
        levelBox = new JComboBox<>(level);
        JOptionPane.showMessageDialog(this, levelBox, "Choose Level", JOptionPane.QUESTION_MESSAGE);

        // Set jumlah puzzle terisi berdasarkan level yang dipilih
        String selectedLevel = (String) levelBox.getSelectedItem();
        if ("Easy".equals(selectedLevel)) {
            filledCells = 65;
        } else if ("Intermediate".equals(selectedLevel)) {
            filledCells = 45;
        } else if ("Difficult".equals(selectedLevel)) {
            filledCells = 30;
        }

        board = new GameBoardPanel(this, filledCells);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(board, BorderLayout.CENTER);

        // Add a button to the south to re-start the game via board.newGame()
        // Add button score
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(timerLabel);
        buttonPanel.add(scoreLabel);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnExit);
        cp.add(buttonPanel, BorderLayout.SOUTH);

        //set initial text for scoreLabel
        totalScore = 0; // Initialize score
        scoreLabel.setText("Score: " + totalScore);

        // Initialize the game board and timer
        initializeTimer();
        board.newGame();
        totalScore = 0;
        startTimer();

        // Action listener for New Game button
        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.newGame();  // Restart the game
                totalScore = 0;  // Reset the score
                updateScoreLabel();
            }
        });

        // Action listener for Reset Game button
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.newGame();  // RESET GAME: Mengulang permainan dari awal
                totalScore = 0;  // RESET SCORE: Mengatur ulang skor
                updateScoreLabel();
            }
        });

        // Action listener for Exit button
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // **EXIT: Menutup aplikasi**
            }
        });

        pack();     // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // to handle window-closing
        setTitle("Sudoku");
        setVisible(true);
    }

    private void initializeTimer(){
        seconds = 0;
        timer = new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                seconds++;
                updateTimerLabel();
            }
        });
    }

    // Method to start the timer
    private void startTimer(){
        timer.start();
    }

    // Method to restart the timer
    private void restartTimer(){
        timer.stop();
        seconds = 0;
        updateTimerLabel(); // Mengupdate label timer
        timer.start();
    }

    // Method to update the timer label
    private void updateTimerLabel(){
        timerLabel.setText("Timer: " + seconds + " seconds");
    }

    public void updateScoreLabel(){
        totalScore = board.getTotalScore();
        scoreLabel.setText("Your Score Now: " + totalScore);
    }

    /** The entry main() entry method */
    public static void main(String[] args) {
        // [TODO 1] Check "Swing program template" on how to run
        //  the constructor of "SudokuMain"
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SudokuMain();  // Let the constructor do the job
            }
        });
    }
}