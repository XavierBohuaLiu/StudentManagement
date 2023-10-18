package portfolio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI {
    public MainGUI(){
        showMainGUI();
    }

    public void showMainGUI(){
        JFrame frame = new JFrame("Student Information");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        // Create buttons
        JButton gradeButton = new JButton("Grade");
        JButton volunteeringButton = new JButton("Volunteering");
        JButton fileUploadButton = new JButton("FileUpload");

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(gradeButton);
        buttonPanel.add(volunteeringButton);
        buttonPanel.add(fileUploadButton);

        // Add button listeners
        gradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStudentGradeApp();
            }
        });

        volunteeringButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showVolunteeringApp();
            }
        });

        fileUploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileUploaderGUI();
            }
        });

        // Add the button panel to the frame
        frame.getContentPane().add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static void showStudentGradeApp() {
        JFrame frame = new JFrame("Student Grade Management System");
        frame.setLocation(600 ,300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create an instance of your custom JPanel class
        StudentGradeApp panel = new StudentGradeApp();

        // Add the panel to the frame
        frame.getContentPane().add(panel);

        frame.pack();
        frame.setVisible(true);
    }

    private static void showVolunteeringApp() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new VolunteeringApp();
            }
        });
    }

    private static void showFileUploaderGUI() {
        JFrame frame = new JFrame("File Uploader");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create an instance of your custom JPanel class
        portfolio.FileUploaderGUI panel = new portfolio.FileUploaderGUI();

        // Add the panel to the frame
        frame.getContentPane().add(panel);

        frame.pack();
        frame.setVisible(true);
    }
}