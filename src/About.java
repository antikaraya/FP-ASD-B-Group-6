/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1 (Jangan lupa diganti kelompok berapa)
 * 1 - 5026231033 - Ayu Alfia Putri
 * 2 - 5026231034 - Antika Raya
 * 3 - 5026231106 - Nailah Qonitah Firdausa
 */

import javax.swing.*;

public class About extends JFrame{
    public About(){
        setSize(200, 400);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("<html><br><center>✨Our Game✨</center><br><center>Developed by:</center><br>"
                + "<center>5026231033 - Ayu Alfia Putri</center>"
                + "<center>5026231034 - Antika Raya</center>"
                + "<center>5026231106 - Nailah Qonitah Firdausa</center><br>"
                + "<center>©️ Final Project ASD B 2024/2025</center></html>");

        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setIconTextGap(10);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);

        add(label);
        setTitle("About");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}