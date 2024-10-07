package tracker;

import java.util.*;
import java.util.stream.Collectors;

public class Statistics {
    private final Map<String, Student> studentMap;
    private final Map<String, Integer> creditMap;
    private final Map<String, Course> courseMap;

    public Statistics(Map<String, Student> studentMap, Map<String, Course> courseMap) {
        this.studentMap = studentMap;
        this.courseMap = courseMap;
        this.creditMap = new HashMap<>();
        creditMap.put("Java", 600);
        creditMap.put("DSA", 400);
        creditMap.put("Databases", 480);
        creditMap.put("Spring", 550);
    }

    public void printAllCourseRankings() {
        Comparator<Course> popularityComp = Comparator.comparingInt(o -> o.getEnrolled().size());
        Comparator<Course> activityComp = Comparator.comparingInt(o -> o.getSubmissionHistory().size());
        Comparator<Course> difficultyComp = (o1, o2) -> {
            double averageGradeO1 = o1.getSubmissionHistory().stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0);
            double averageGradeO2 = o2.getSubmissionHistory().stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0);
            return Double.compare(averageGradeO1, averageGradeO2);
        };

        List<Course> sortByPopularity = courseMap.values().stream()
                .sorted(popularityComp)
                .toList();
        List<Course> sortByActivity = courseMap.values().stream()
                .sorted(activityComp)
                .toList();
        List<Course> sortByDifficulty = courseMap.values().stream()
                .sorted(difficultyComp)
                .toList();



        System.out.println("Type the name of a course to see details or 'back' to quit");
        System.out.println("Most popular: " + getMostPopular(sortByPopularity));
        System.out.println("Least popular: " + getLeastPopular(sortByPopularity));
        System.out.println("Highest activity: " + getMostActive(sortByActivity));
        System.out.println("Lowest activity: " + getLeastActive(sortByActivity));
        System.out.println("Easiest course: " + getEasiest(sortByDifficulty));
        System.out.println("Hardest course: " + getHardest(sortByDifficulty));

    }

    private String getMostPopular(List<Course> courseList) {
        if (courseList.get(courseList.size() - 1).getSubmissionHistory().isEmpty()) {
            return "n/a";
        }
        int highestPopularity = courseList.get(courseList.size() - 1).getEnrolled().size();
        List<Course> mostPopular = courseList.stream()
                .filter(course -> course.getEnrolled().size() == highestPopularity)
                .toList();



        return joinedCourses(mostPopular);

    }

    private String getLeastPopular(List<Course> courseList) {
        if (courseList.get(0).getSubmissionHistory().isEmpty()) {
            return "n/a";
        }

        int lowestPopularity = courseList.get(0).getEnrolled().size();
        List<Course> leastPopular = courseList.stream()
                .filter(course -> course.getEnrolled().size() == lowestPopularity)
                .toList();

        if (joinedCourses(leastPopular).equals(getMostPopular(courseList))) {
            return "n/a";
        }

        return joinedCourses(leastPopular);
    }

    private String getMostActive(List<Course> courseList) {
        if (courseList.get(courseList.size() - 1).getSubmissionHistory().isEmpty()) {
            return "n/a";
        }

        int highestActivity = courseList.get(courseList.size() - 1).getSubmissionHistory().size();
        List<Course> mostActive = courseList.stream()
                .filter(course -> course.getSubmissionHistory().size() == highestActivity)
                .toList();

        return joinedCourses(mostActive);
    }

    private String getLeastActive(List<Course> courseList) {
        if (courseList.get(0).getSubmissionHistory().isEmpty()) {
            return "n/a";
        }

        int lowestActivity = courseList.get(0).getSubmissionHistory().size();
        List<Course> leastActive = courseList.stream()
                .filter(course -> course.getSubmissionHistory().size() == lowestActivity)
                .toList();

        if (joinedCourses(leastActive).equals(getMostActive(courseList))) {
            return "n/a";
        }

        return joinedCourses(leastActive);

    }

    private String getEasiest(List<Course> courseList) {
        if (courseList.get(courseList.size() - 1).getSubmissionHistory().isEmpty()) {
            return "n/a";
        }


        double highestAverage = courseList.get(courseList.size() - 1).getAverageGrade();
        List<Course> easiestCourses = courseList.stream()
                .filter(course -> course.getAverageGrade() == highestAverage)
                .toList();

        return joinedCourses(easiestCourses);
    }


    private String getHardest(List<Course> courseList) {

        if (courseList.get(0).getSubmissionHistory().isEmpty()) {
            return "n/a";
        }


        double highestAverage = courseList.get(0).getAverageGrade();
        List<Course> hardestCourses = courseList.stream()
                .filter(course -> course.getAverageGrade() == highestAverage)
                .toList();

        if (joinedCourses(hardestCourses).equals(getEasiest(courseList))) {
            return "n/a";
        }

        return joinedCourses(hardestCourses);
    }

    private String joinedCourses(List<Course> easiestCourses) {
        return easiestCourses.stream()
                .map(Course::getName)
                .collect(Collectors.joining(", "));
    }

    public void printCourseInfo(Course course) {

        System.out.println(course.getName());
        System.out.println("id     points    completed");
        if (studentMap.isEmpty()) { //no students available
            return;
        }

        List<Student> sortedStudentList = getSortedStudentsInCourse(course.getName());

        for (Student student : sortedStudentList) {
            if (student.getSpecifiedGrade(course.getName()) == 0) { //Check if student not enrolled
                continue;
            }
            double percentageCompleted = ((double) student.getSpecifiedGrade(course.getName()) / (double) course.getRequiredCredits()) * 100;
            percentageCompleted = Math.round(percentageCompleted * 10.0) / 10.0;
            System.out.println(student.getId() + " " + student.getSpecifiedGrade(course.getName()) +
                    "        " + percentageCompleted + "%");
        }

    }



    private List<Student> getSortedStudentsInCourse(String courseName) {

        List<Student> sortedStudentList = new ArrayList<>(studentMap.values());

        switch (courseName) {
            case "Java" -> {
                Comparator<Student> studentComparator = Comparator.comparingInt(Student::getJavaGrade)
                        .reversed()
                        .thenComparing(Student::getId);
                sortedStudentList.sort(studentComparator);
                return sortedStudentList;
            }
            case "DSA" -> {
                Comparator<Student> studentComparator = Comparator.comparingInt(Student::getDSAGrade)
                        .reversed()
                        .thenComparing(Student::getId);
                sortedStudentList.sort(studentComparator);
                return sortedStudentList;
            }
            case "Databases" -> {
                Comparator<Student> studentComparator = Comparator.comparingInt(Student::getDatabasesGrade)
                        .reversed()
                        .thenComparing(Student::getId);
                sortedStudentList.sort(studentComparator);
                return sortedStudentList;
            }
            case "Spring" -> {
                Comparator<Student> studentComparator = Comparator.comparingInt(Student::getSpringGrade)
                        .reversed()
                        .thenComparing(Student::getId);
                sortedStudentList.sort(studentComparator);
                return sortedStudentList;
            }
            default -> {
                return null;
            }
        }
    }






}
