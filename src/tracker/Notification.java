package tracker;

import java.util.Objects;

public class Notification {

    private Student student;
    private Course course;

    public Notification(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void printNotification() {
        System.out.println("To: " + student.getEmail());
        System.out.println("Re: Your Learning Progress");
        System.out.println("Hello, "+ student.getFirstName() + " " + student.getLastName() +
                "! You have accomplished our " + course.getName() + " course!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(student, that.student) && Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }
}
