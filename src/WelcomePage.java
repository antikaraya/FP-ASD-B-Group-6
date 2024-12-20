/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #6
 * 1 - 5026231033 - Ayu Alfia Putri
 * 2 - 5026231034 - Antika Raya
 * 3 - 5026231106 - Nailah Qonitah Firdausa
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePage extends JFrame {
    public WelcomePage() {
        // Set frame properties
        setTitle("Welcome Page");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set layout and background
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(new Color(173, 225, 47)); // Light green and yellow

        // Create a panel for the content with vertical alignment
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(173, 255, 47)); // Same as frame background

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to the Game!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Add vertical spacing
        contentPanel.add(welcomeLabel);

        // Add choose label
        JLabel chooseLabel = new JLabel("What do you want to play?", JLabel.CENTER);
        chooseLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Add vertical spacing
        contentPanel.add(chooseLabel);

        // Buttons for game selection
        JButton ticTacToeButton = new JButton("Play TicTacToe");
        JButton connectFourButton = new JButton("Play Connect-Four");

        ticTacToeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        connectFourButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button actions
        ticTacToeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close Welcome Page
                TicTacToe.GameMain.main(null); // Launch TicTacToe
            }
        });

        connectFourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close Welcome Page
                FourConnect.GameMain.main(null); // Launch Connect Four
            }
        });

        // Add buttons to content panel
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Add vertical spacing
        contentPanel.add(ticTacToeButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing
        contentPanel.add(connectFourButton);

        // Add buttons to the frame
        add(contentPanel, BorderLayout.CENTER);

        // Center the frame
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        // Launch the welcome page
        SwingUtilities.invokeLater(() -> {
            WelcomePage welcomePage = new WelcomePage();
            welcomePage.setVisible(true);
        });
    }
}