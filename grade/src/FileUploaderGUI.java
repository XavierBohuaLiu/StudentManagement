package portfolio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileUploaderGUI extends JFrame {

    private JTextField textField;
    private JTextArea textArea;
    private JButton queryButton;
    private JButton uploadButton;

    private List<String> uploadedFiles;

    public FileUploaderGUI() {
        uploadedFiles = new ArrayList<>();

        setTitle("File Uploader and Query");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        textField = new JTextField(30);
        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(new BrowseButtonListener());

        topPanel.add(textField);
        topPanel.add(browseButton);

        add(topPanel, BorderLayout.NORTH);

        // Text area in the center
        textArea = new JTextArea(20, 50);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Upload button
        uploadButton = new JButton("Upload");
        uploadButton.addActionListener(new UploadButtonListener());
        topPanel.add(uploadButton);

        // Query button
        queryButton = new JButton("Query");
        queryButton.addActionListener(new QueryButtonListener());
        topPanel.add(queryButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class BrowseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(FileUploaderGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                textField.setText(selectedFile.getAbsolutePath());
            }
        }
    }

    private class QueryButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String destinationDirectory = "upload/";
            File destinationDir = new File(destinationDirectory);

            if (destinationDir.isDirectory()) {
                File[] files = destinationDir.listFiles();

                // Create a new window to display the file list
                JFrame fileListFrame = new JFrame("File List");
                fileListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                fileListFrame.setLayout(new BorderLayout());

                JList<String> fileList = new JList<>(getFilenames(files));
                JScrollPane scrollPane = new JScrollPane(fileList);
                fileListFrame.add(scrollPane, BorderLayout.CENTER);

                JButton downloadButton = new JButton("Download");
                downloadButton.addActionListener(new DownloadButtonListener(fileList));
                fileListFrame.add(downloadButton, BorderLayout.SOUTH);

                fileListFrame.pack();
                fileListFrame.setLocationRelativeTo(FileUploaderGUI.this);
                fileListFrame.setVisible(true);
            }
        }
    }

    private class UploadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String sourceFilePath = textField.getText();
            String destinationDirectory = "upload/";

            try {
                File sourceFile = new File(sourceFilePath);
                File destinationDir = new File(destinationDirectory);

                // Copy the file to the destination directory
                Files.copy(sourceFile.toPath(), destinationDir.toPath().resolve(sourceFile.getName()), StandardCopyOption.REPLACE_EXISTING);

                uploadedFiles.add(sourceFile.getName());
                textArea.append("File uploaded successfully!\n");
            } catch (IOException ex) {
                ex.printStackTrace();
                textArea.append("Failed to upload the file!\n");
            }
        }
    }

    private class DownloadButtonListener implements ActionListener {
        private JList<String> fileList;

        public DownloadButtonListener(JList<String> fileList) {
            this.fileList = fileList;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedFileName = fileList.getSelectedValue();
            if (selectedFileName != null) {
                String sourceDirectory = "upload/";
                String sourceFilePath = sourceDirectory + selectedFileName;

                String destinationDirectory = "download/";
                String destinationFilePath = destinationDirectory + selectedFileName;

                File destinationDir = new File(destinationDirectory);
                if (!destinationDir.exists()) {
                    destinationDir.mkdirs();
                }

                try {
                    File sourceFile = new File(sourceFilePath);
                    File destinationFile = new File(destinationFilePath);

                    // Copy the file to the destination location
                    Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    JOptionPane.showMessageDialog(FileUploaderGUI.this, "File downloaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(FileUploaderGUI.this, "Failed to download the file!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private String[] getFilenames(File[] files) {
        List<String> filenames = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                filenames.add(file.getName());
            }
        }
        return filenames.toArray(new String[0]);
    }
}
