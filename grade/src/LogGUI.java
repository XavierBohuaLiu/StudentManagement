package portfolio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class LogGUI {
    private Stu_log user; // Instance of Stu_log class to store user login state
    private JFrame loginFrame;
    private JTextField idField;
    private JPasswordField passwordField;

    public LogGUI() {
        user = new Stu_log();

        createLoginGUI();
    }

    private void createLoginGUI() {

        loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 200);
        loginFrame.setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel idLabel = new JLabel("Student ID:");
        idField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginPanel.add(idLabel);
        loginPanel.add(idField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String password = new String(passwordField.getPassword());
                boolean loginSuccessful = checkLoginCredentials(id, password);

                if (loginSuccessful) {
                    user.setLoggedIn(true);
                    user.setStudentID(id);
                    new MainGUI();
                    loginFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid student ID or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterGUI();
            }
        });

        loginFrame.getContentPane().add(loginPanel, BorderLayout.CENTER);

        loginFrame.setVisible(true);
    }

    private boolean checkLoginCredentials(String id, String password) {
        try {
            File file = new File("log_information.txt");
            if (!file.exists()) {
                file.createNewFile();
                return false;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length == 3 && userInfo[1].equals(id) && userInfo[2].equals(password)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void showRegisterGUI() {
        JFrame registerFrame = new JFrame("Register");
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setSize(300, 200);
        registerFrame.setLocationRelativeTo(null);

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel idLabel = new JLabel("Student ID:");
        JTextField idField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton registerConfirmButton = new JButton("Confirm");
        JButton registerCancelButton = new JButton("Cancel");

        registerPanel.add(nameLabel);
        registerPanel.add(nameField);
        registerPanel.add(idLabel);
        registerPanel.add(idField);
        registerPanel.add(passwordLabel);
        registerPanel.add(passwordField);
        registerPanel.add(registerConfirmButton);
        registerPanel.add(registerCancelButton);

        registerConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = idField.getText();
                String password = new String(passwordField.getPassword());

                if (checkExistingUser(name, id)) {
                    JOptionPane.showMessageDialog(registerFrame, "User with the same name or ID already exists", "Registration Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    registerNewUser(name, id, password);
                    JOptionPane.showMessageDialog(registerFrame, "Registration successful");
                    registerFrame.dispose();
                }
            }
        });

        registerCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerFrame.dispose();
            }
        });

        registerFrame.getContentPane().add(registerPanel, BorderLayout.CENTER);

        registerFrame.setVisible(true);
    }

    private boolean checkExistingUser(String name, String id) {
        try {
            File file = new File("log_information.txt");
            if (!file.exists()) {
                return false;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length == 3 && (userInfo[0].equals(name) || userInfo[1].equals(id))) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    private void registerNewUser(String name, String id, String password) {
        try {
            FileWriter writer = new FileWriter("log_information.txt", true);
            writer.write(name + "," + id + "," + password + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
