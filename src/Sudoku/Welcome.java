package Sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Welcome extends JFrame {

    public Welcome() {
        // Set frame properties
        setTitle("Welcome to Sudoku Game");
        setSize(600, 400);
        setLocationRelativeTo(null); // Center the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Set background color
        getContentPane().setBackground(new Color(255, 228, 225)); // Light pastel pink background

        // Title label (Sudoku)
        JLabel titleLabel = new JLabel("Welcome to Sudoku Game!", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(0, 102, 204)); // Bright blue for title (more visible)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Description label (Solve puzzles)
        JLabel descriptionLabel = new JLabel("Solve puzzles and improve your score!", JLabel.CENTER);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        descriptionLabel.setForeground(Color.BLACK); // Black text for description
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Optional padding
        gbc.gridy = 1;
        add(descriptionLabel, gbc);

        // Start button
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.PLAIN, 20));
        startButton.setForeground(Color.BLACK); // Black text for the button
        startButton.setBackground(new Color(255, 182, 193)); // Pink pastel background for button
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(200, 50)); // Set button size
        gbc.gridy = 2;
        add(startButton, gbc);

        // Action for the Start button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close welcome screen
                new SudokuMain(); // Start the main Sudoku game
            }
        });
    }

    public static void main(String[] args) {
        // Display the welcome screen
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Welcome().setVisible(true);
            }
        });
    }
}
