package portfolio;

import javax.swing.*;
class Grade {
    private String courseName;
    private String year;
    private String semester;
    private String credit;
    private String score;

    public Grade(String courseName, String year, String semester, String credit, String score) {
        this.courseName = courseName;
        this.year = year;
        this.semester = semester;
        this.credit = credit;
        this.score = score;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getYear() {
        return year;
    }

    public String getSemester() {
        return semester;
    }

    public String getCredit() {
        return credit;
    }

    public String getScore() {
        return score;
    }
}