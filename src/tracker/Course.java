package tracker;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private String name;
    private int requiredCredits;
    private List<Student> enrolled;
    private List<Integer> submissionHistory;

    public Course(String name, int requiredCredits) {
        this.name = name;
        this.requiredCredits = requiredCredits;
        this.submissionHistory = new ArrayList<>();
        this.enrolled = new ArrayList<>();
    }

    public void addEntry(int assignmentGrade) {
        submissionHistory.add(assignmentGrade);
    }

    public void enrollStudent(Student student) {
        this.enrolled.add(student);
    }

    public String getName() {
        return name;
    }

    public int getRequiredCredits() {
        return requiredCredits;
    }

    public List<Student> getEnrolled() {
        return enrolled;
    }

    public List<Integer> getSubmissionHistory() {
        return submissionHistory;
    }

    public double getAverageGrade() {
        return submissionHistory.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }


}
