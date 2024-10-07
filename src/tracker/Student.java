package tracker;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private String firstName;
    private String lastName;
    private String email;
    private String id;
    private final Map<String, Integer> grades;
    private final Map<String, Integer> submissions;
    private final Map<String, Course> courseMap;

    public Student(String id, Map<String, Course> courseMap) {
        this.courseMap = courseMap;

        this.id = id;

        this.grades = new HashMap<>();
        this.grades.put("Java", 0);
        this.grades.put("DSA", 0);
        this.grades.put("Databases", 0);
        this.grades.put("Spring", 0);

        this.submissions = new HashMap<>();
        this.submissions.put("Java", 0);
        this.submissions.put("DSA", 0);
        this.submissions.put("Databases", 0);
        this.submissions.put("Spring", 0);
    }


    public boolean addStudent(String input) {
        String[] entry = input.split(" ");

        //not enough elements for first, last and email
        if (entry.length < 3) {
            System.out.println("Incorrect credentials.");
            return false;
        }



        //first name
        if (!isValidFirstName(entry[0])) {
            System.out.println("Incorrect first name.");
            return false;
        }
        firstName = entry[0];

        //last name
        StringBuilder combinedLastName = new StringBuilder();
        for (int i = 1; i < entry.length - 1; i++) { //allow long names
            combinedLastName.append(entry[i]);
            combinedLastName.append(" ");
        }
        String untrimmedLastName = combinedLastName.toString();

        if (!isValidLastName(untrimmedLastName.trim())) {
            System.out.println("Incorrect last name.");
            return false;
        }
        lastName = untrimmedLastName.trim();

        //email
        if (!isValidEmail(entry[entry.length - 1])) {
            System.out.println("Incorrect email.");
            return false;
        }
        email = entry[entry.length - 1];



        return true;

    }


    public boolean isValidFirstName(String firstName) {
        String validNameRegex = "^(?!.*['-]{2})[A-Za-z][A-Za-z' -]*[A-Za-z]$";
        if (!firstName.matches(validNameRegex)) {
            return false;
        }
        if (firstName.length() < 2) {
            return false;
        }



        return true;
    }


    public boolean isValidLastName(String lastName) {
        String[] splitLastName = lastName.split(" ");

        for (String name : splitLastName) {
            String validNameRegex = "^(?!.*['-]{2})[A-Za-z][A-Za-z' -]*[A-Za-z]$";
            if (!name.matches(validNameRegex)) {
                return false;
            }
            if (name.length() < 2) {
                return false;
            }
        }


        return true;
    }

    public boolean isValidEmail(String email) {
        if (!email.matches(".*@.*") && !email.matches(".*\\..*")) {
            return false;
        }

        String[] splitAtDot = email.split("\\.");

        //@ after domain
        if (splitAtDot[splitAtDot.length - 1].contains("@")) {
            return false;
        }

        String[] splitAtAt = email.split("@");
        //multiple @
        if (splitAtAt.length != 2) {
            return false;
        }


        return true;
    }


    public void sendSubmission(int JavaGrade, int DSAGrade, int DatabasesGrade, int SpringGrade) {

        updateGradeAndSubmission("Java", JavaGrade);
        updateGradeAndSubmission("DSA", DSAGrade);
        updateGradeAndSubmission("Databases", DatabasesGrade);
        updateGradeAndSubmission("Spring", SpringGrade);

    }

    private void updateGradeAndSubmission(String course, int points) {
        if (points > 0) {
            //update Student Data
            int coursePoints = points + this.grades.get(course);
            this.grades.put(course, coursePoints);

            if (this.submissions.get(course) == 0) { //enroll the student if it's the first submission
                courseMap.get(course).enrollStudent(this);
            }

            int courseSubmissions = this.submissions.get(course) + 1;
            this.submissions.put(course, courseSubmissions);


            courseMap.get(course).addEntry(points);

        }
    }

    public String toString() {
        return id;
    }

    public void printGrades() {
        String grades = String.format("%s points: Java=%d; DSA=%d; Databases=%d; Spring=%d",
                id, getJavaGrade(), getDSAGrade(), getDatabasesGrade(), getSpringGrade());
        System.out.println(grades);
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public int getSpecifiedGrade(String course) {
        return this.grades.get(course);
    }

    public int getJavaGrade() {
        return this.grades.get("Java");
    }

    public int getDSAGrade() {
        return this.grades.get("DSA");
    }

    public int getDatabasesGrade() {
        return this.grades.get("Databases");
    }

    public int getSpringGrade() {
        return this.grades.get("Spring");
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
