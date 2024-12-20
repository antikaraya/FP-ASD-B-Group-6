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

public class
WelcomePage extends JFrame {
    private Image backgroundImage; // Deklarasi variabel gambar

    public WelcomePage() {
        // Set frame properties
        setTitle("Welcome Page");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load background image from file
        ImageIcon icon = new ImageIcon("src/bgTTT.jpg"); // Ganti dengan path gambar Anda
        backgroundImage = icon.getImage();

        // Create a panel with a custom background
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to the Game!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.BLACK); // Set text color for better visibility
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Add vertical spacing
        contentPanel.add(welcomeLabel);

        // Add choose label
        JLabel chooseLabel = new JLabel("What do you want to play?", JLabel.CENTER);
        chooseLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        chooseLabel.setForeground(Color.BLACK);
        chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Add vertical spacing
        contentPanel.add(chooseLabel);

        // Buttons for game selection
        JButton ticTacToeButton = new JButton("Play TicTacToe");
        JButton connectFourButton = new JButton("Play Connect-Four");
        JButton aboutButton = new JButton("About"); // Buat tombol About

        ticTacToeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        connectFourButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    About about = new About(); // Membuat jendela baru
                    // Mendapatkan lokasi jendela utama
                    JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(aboutButton);
                    if (mainFrame != null) {
                        int mainX = mainFrame.getX();
                        int mainY = mainFrame.getY();
                        int mainWidth = mainFrame.getWidth();
                        // Menempatkan jendela baru di samping kanan jendela utama
                        about.setLocation(mainX + mainWidth + 10, mainY);
                    }
                    about.setVisible(true); // Menampilkan jendela baru
                });
            }
        });


        // Add buttons to content panel
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Add vertical spacing
        contentPanel.add(ticTacToeButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing
        contentPanel.add(connectFourButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing
        contentPanel.add(aboutButton);

        // Add content panel to the frame
        setContentPane(contentPanel);

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