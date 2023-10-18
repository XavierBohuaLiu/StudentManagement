package portfolio;

public class Stu_log {
    private boolean loggedIn;
    private String studentID;

    public Stu_log() {
        loggedIn = false;
        studentID = "";
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
}
