package portfolio;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class StudentGradeApp extends JPanel {
    private JTable gradeTable;
    private DefaultTableModel tableModel;
    private JTextField courseNameField;
    private JTextField yearField;
    private JTextField semesterField;

    private List<Grade> grades;
    private String gradeFilePath = "grades.txt";

    public StudentGradeApp() {
        setLayout(new BorderLayout());

        // 表格模型
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Course Name");
        tableModel.addColumn("Year");
        tableModel.addColumn("Semester");
        tableModel.addColumn("Credit");
        tableModel.addColumn("Score");

        // 成绩表格
        gradeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gradeTable);
        add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // 添加成绩按钮
        JButton addGradeButton = new JButton("Add Grade");
        addGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addGrade();
            }
        });
        buttonPanel.add(addGradeButton);

        // 修改成绩按钮
        JButton updateGradeButton = new JButton("Update Grade");
        updateGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGrade();
            }
        });
        buttonPanel.add(updateGradeButton);

        // 删除成绩按钮
        JButton deleteGradeButton = new JButton("Delete Grade");
        deleteGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteGrade();
            }
        });
        buttonPanel.add(deleteGradeButton);

        // 课程名称过滤按钮
        JButton filterByCourseButton = new JButton("Filter by Course");
        filterByCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterByCourse();
            }
        });
        buttonPanel.add(filterByCourseButton);

        // 学年过滤按钮
        JButton filterByYearButton = new JButton("Filter by Year");
        filterByYearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterByYear();
            }
        });
        buttonPanel.add(filterByYearButton);

        // 生成折线图按钮
        JButton generateChartButton = new JButton("Generate Chart");
        generateChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateChart();
            }
        });
        buttonPanel.add(generateChartButton);

        // 初始化成绩列表
        grades = new ArrayList<>();

        // 加载并显示成绩
        loadGrades();
        displayGrades();
    }

    // 添加成绩
    private void addGrade() {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        JTextField courseNameField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField semesterField = new JTextField();
        JTextField creditField = new JTextField();
        JTextField scoreField = new JTextField();

        panel.add(new JLabel("Course Name:"));
        panel.add(courseNameField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Semester:"));
        panel.add(semesterField);
        panel.add(new JLabel("Credit Score:"));
        panel.add(creditField);
        panel.add(new JLabel("Score:"));
        panel.add(scoreField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Grade", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String courseName = courseNameField.getText();
            String year = yearField.getText();
            String semester = semesterField.getText();
            String credit = creditField.getText();
            String score = scoreField.getText();

            if (!courseName.isEmpty() && !year.isEmpty() && !semester.isEmpty() && !credit.isEmpty() && !score.isEmpty()) {
                Grade grade = new Grade(courseName, year, semester, credit, score);
                grades.add(grade);
                saveGrades();
                displayGrades();
                JOptionPane.showMessageDialog(null, "Grade added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in all the fields!");
            }
        }
    }


    // 修改成绩
    private void updateGrade() {
        int selectedRow = gradeTable.getSelectedRow();
        if (selectedRow != -1) {
            String courseName = (String) tableModel.getValueAt(selectedRow, 0);
            String year = (String) tableModel.getValueAt(selectedRow, 1);
            String semester = (String) tableModel.getValueAt(selectedRow, 2);
            String credit = (String) tableModel.getValueAt(selectedRow, 3);
            String score = JOptionPane.showInputDialog("Enter new score for " + courseName + " (" + year + " " + semester + "):");
            if (score != null) {
                tableModel.setValueAt(score, selectedRow, 4);
                JOptionPane.showMessageDialog(null, "Grade updated successfully!");
                saveGrades();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a grade to update!");
        }
    }

    // 删除成绩
    private void deleteGrade() {
        int selectedRow = gradeTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
            grades.remove(selectedRow);
            saveGrades();
            JOptionPane.showMessageDialog(null, "Grade deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Please select a grade to delete!");
        }
    }

    // 按课程名称筛选成绩
    private void filterByCourse() {
        String courseName = JOptionPane.showInputDialog("Enter course name:");
        if (courseName != null) {
            List<Grade> filteredGrades = new ArrayList<>();
            for (Grade grade : grades) {
                if (grade.getCourseName().equalsIgnoreCase(courseName)) {
                    filteredGrades.add(grade);
                }
            }
            displayFilteredGrades(filteredGrades);
        }
    }

    // 按学年筛选成绩
    private void filterByYear() {
        String year = JOptionPane.showInputDialog("Enter year:");
        if (year != null) {
            List<Grade> filteredGrades = new ArrayList<>();
            for (Grade grade : grades) {
                if (grade.getYear().equalsIgnoreCase(year)) {
                    filteredGrades.add(grade);
                }
            }
            displayFilteredGrades(filteredGrades);
        }
    }

    // 生成折线图
    private void generateChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Create a map to store the sum of scores and the count of grades for each semester
        Map<String, Double> semesterSum = new HashMap<>();
        Map<String, Integer> semesterCount = new HashMap<>();

        // Iterate over the grades to calculate the sum of scores and count of grades for each semester
        for (Grade grade : grades) {
            String semester = grade.getYear() + " " + grade.getSemester();
            double score = Double.parseDouble(grade.getScore());

            // Update the sum and count for the semester
            semesterSum.put(semester, semesterSum.getOrDefault(semester, 0.0) + score);
            semesterCount.put(semester, semesterCount.getOrDefault(semester, 0) + 1);
        }

        // Calculate the average score for each semester and add it to the dataset
        for (String semester : semesterSum.keySet()) {
            double sum = semesterSum.get(semester);
            int count = semesterCount.get(semester);
            double averageScore = sum / count;

            dataset.addValue(averageScore, "Average Grade", semester);
        }

        // Create the chart and plot the dataset
        JFreeChart chart = ChartFactory.createLineChart(
                "Average Grade per Semester", // Chart title
                "Semester", // X-axis label
                "Average Grade", // Y-axis label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Orientation
                true, // Include legend
                true, // Include tooltips
                false // Include URLs
        );

        // Customize the chart
        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        // Create the chart panel and add it to a frame
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));

        JFrame frame = new JFrame("Average Grade Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // 保存成绩到文本文件
    private void saveGrades() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(gradeFilePath))) {
            for (Grade grade : grades) {
                writer.write(grade.getCourseName() + "," + grade.getYear() + "," + grade.getSemester() + ","
                        + grade.getCredit() + "," + grade.getScore());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while saving grades: " + e.getMessage());
        }
    }

    // 加载成绩从文本文件
    private void loadGrades() {
        File file = new File(gradeFilePath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(gradeFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        String courseName = parts[0];
                        String year = parts[1];
                        String semester = parts[2];
                        String credit = parts[3];
                        String score = parts[4];
                        Grade grade = new Grade(courseName, year, semester, credit, score);
                        grades.add(grade);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error while loading grades: " + e.getMessage());
            }
        }
    }

    // 显示所有成绩
    private void displayGrades() {
        tableModel.setRowCount(0);
        for (Grade grade : grades) {
            tableModel.addRow(new Object[]{grade.getCourseName(), grade.getYear(), grade.getSemester(),
                    grade.getCredit(), grade.getScore()});
        }
    }

    // 显示筛选后的成绩
    private void displayFilteredGrades(List<Grade> filteredGrades) {
        tableModel.setRowCount(0);
        for (Grade grade : filteredGrades) {
            tableModel.addRow(new Object[]{grade.getCourseName(), grade.getYear(), grade.getSemester(),
                    grade.getCredit(), grade.getScore()});
        }
    }
}


