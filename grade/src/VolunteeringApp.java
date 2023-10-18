package portfolio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class VolunteeringApp {
    private JFrame mainFrame;
    private JLabel titleLabel;
    private JTable table;
    public JTextField nameField;   // Modified: Changed access modifier to public
    public JTextField timeField;   // Modified: Changed access modifier to public
    public JTextField creditField; // Modified: Changed access modifier to public
    private JButton addBtn;
    private JButton deleteBtn;
    private JButton modifyBtn;
    private JButton exitBtn;
    private DefaultTableModel tableModel;

    private String dataFilePath = "volunteering_data.txt";

    public VolunteeringApp() {
        createMainGUI();
        loadData();
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    private void createMainGUI() {
        // Create the main frame
        mainFrame = new JFrame("Volunteering App");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        // Create the title label
        titleLabel = new JLabel("My volunteering experience");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainFrame.add(titleLabel, BorderLayout.NORTH);

        // Create the table
        tableModel = new DefaultTableModel(new Object[]{"VOLUNTEERING NUMBER", "VOLUNTEERING NAME", "VOLUNTEERING TIME", "VOLUNTEERING CREDITS"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainFrame.add(scrollPane, BorderLayout.CENTER);

        // Create the buttons
        addBtn = new JButton("Add volunteering experience");
        deleteBtn = new JButton("Delete");  // New button for deleting volunteering data
        modifyBtn = new JButton("Modify");  // New button for modifying volunteering data
        exitBtn = new JButton("EXIT");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(modifyBtn);
        buttonPanel.add(exitBtn);

        mainFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to the buttons
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddVolunteeringDialog();
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedVolunteering();
            }
        });

        modifyBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifySelectedVolunteering();
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
            }
        });

        // Set the frame properties
        mainFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null); // Center the frame on the screen
        mainFrame.setVisible(true);
    }

    private void loadData() {
        // Clear the existing table data
        tableModel.setRowCount(0);

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                tableModel.addRow(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveData(String[] rowData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath, true))) {
            writer.write(String.join(",", rowData));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddVolunteeringDialog() {
        // Create the dialog
        JDialog dialog = new JDialog(mainFrame, "Add volunteering experience", true);
        dialog.setLayout(new BorderLayout());

        // Create the form
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        JLabel nameLabel = new JLabel("Work name:");
        JTextField nameField = new JTextField();
        JLabel timeLabel = new JLabel("Work time:");
        JTextField timeField = new JTextField();
        JLabel creditLabel = new JLabel("Credit:");
        JTextField creditField = new JTextField();

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(timeLabel);
        formPanel.add(timeField);
        formPanel.add(creditLabel);
        formPanel.add(creditField);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Create the buttons
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to the buttons
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String time = timeField.getText();
                String credit = creditField.getText();

                // Validate the input
                if (name.isEmpty() || time.isEmpty() || credit.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Add the new row to the table and save to the file
                int nextNumber = tableModel.getRowCount() + 1;
                String[] rowData = {String.valueOf(nextNumber), name, time, credit};
                tableModel.addRow(rowData);
                saveData(rowData);

                // Close the dialog
                dialog.dispose();
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        // Set the dialog properties
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private void deleteSelectedVolunteering() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirm the deletion
        int confirmation = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to delete this volunteering experience?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            saveTableData();
        }
    }

    private void modifySelectedVolunteering() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select a row to modify", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the existing data for the selected row
        String[] rowData = new String[tableModel.getColumnCount()];
        for (int i = 0; i < rowData.length; i++) {
            rowData[i] = tableModel.getValueAt(selectedRow, i).toString();
        }

        // Create the modify dialog with pre-filled data
        JDialog dialog = new JDialog(mainFrame, "Modify volunteering experience", true);
        dialog.setLayout(new BorderLayout());

        // Create the form
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        JLabel nameLabel = new JLabel("Work name:");
        JTextField nameField = new JTextField(rowData[1]);
        JLabel timeLabel = new JLabel("Work time:");
        JTextField timeField = new JTextField(rowData[2]);
        JLabel creditLabel = new JLabel("Credit:");
        JTextField creditField = new JTextField(rowData[3]);

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(timeLabel);
        formPanel.add(timeField);
        formPanel.add(creditLabel);
        formPanel.add(creditField);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Create the buttons
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to the buttons
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String time = timeField.getText();
                String credit = creditField.getText();

                // Validate the input
                if (name.isEmpty() || time.isEmpty() || credit.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update the data in the table and save to the file
                rowData[1] = name;
                rowData[2] = time;
                rowData[3] = credit;
                for (int i = 0; i < rowData.length; i++) {
                    tableModel.setValueAt(rowData[i], selectedRow, i);
                }
                saveTableData();

                // Close the dialog
                dialog.dispose();
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        // Set the dialog properties
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private void saveTableData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                String[] rowData = new String[tableModel.getColumnCount()];
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    rowData[col] = tableModel.getValueAt(row, col).toString();
                }
                writer.write(String.join(",", rowData));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //
    public void saveTableDataToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            saveTableData(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTableData(BufferedWriter writer) throws IOException {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            String[] rowData = new String[tableModel.getColumnCount()];
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                rowData[col] = tableModel.getValueAt(row, col).toString();
            }
            writer.write(String.join(",", rowData));
            writer.newLine();
        }
    }
    public String getNameFieldValue() {
        return nameField.getText();
    }
}
