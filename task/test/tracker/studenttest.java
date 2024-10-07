package tracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

class StudentTest {

    private static Map<String, Course> getCourseMap() {
        Map<String, Course> courseMap = new HashMap<>();
        courseMap.put("Java", new Course("Java", 600));
        courseMap.put("DSA", new Course("DSA", 400));
        courseMap.put("Databases", new Course("Databases", 480));
        courseMap.put("Spring", new Course("Spring", 550));
        return courseMap;
    }


    @Test
    void testValidName() {
        Map<String, Course> courseMap = getCourseMap();

        Student student = new Student("10000", courseMap);

        String input = "validEmail@email.com";
        Assertions.assertFalse(student.addStudent(input));

        input = "";
        Assertions.assertFalse(student.addStudent(input));

        //allow -
        input = "Jimmy Henderson-Korsakoff validEmail@email.com";
        Assertions.assertTrue(student.addStudent(input));

        //disallow hyphens at the end or beginning
        input = "-Robert Jemison Van de Graaff- validEmail@email.com";
        Assertions.assertFalse(student.addStudent(input));

        //disallow double hyphens
        input = "Robert Jemison--Van de Graaff validEmail@email.com";
        Assertions.assertFalse(student.addStudent(input));

        //allow '
        input = "Jimmy Henderson'Korsakoff validEmail@email.com";
        Assertions.assertTrue(student.addStudent(input));

        input = "'Robert Jemison Van de Graaff' validEmail@email.com";
        Assertions.assertFalse(student.addStudent(input));

        input = "Robert Jemison''Van de Graaff validEmail@email.com";
        Assertions.assertFalse(student.addStudent(input));



        //allow long names
        input = "Robert Jemison Van de Graaff validEmail@email.com";
        Assertions.assertTrue(student.addStudent(input));

        //disallow numbers and most special characters
        input = "~D0MInAt0R~ validEmail@email.com";
        Assertions.assertFalse(student.addStudent(input));

        //disallow foreign characters
        input = "Stanisław Oğuz validEmail@email.com";
        Assertions.assertFalse(student.addStudent(input));

    }



    @Test
    void testValidEmail() {
        Map<String, Course> courseMap = getCourseMap();

        Student student = new Student("10000", courseMap);

        String input = "Valid Name validEmail@email.com";
        Assertions.assertTrue(student.addStudent(input));

        //No @, dot or domain
        input = "Valid Name validEmail";
        Assertions.assertFalse(student.addStudent(input));

        //No domain and dot
        input = "Valid Name validEmail@email";
        Assertions.assertFalse(student.addStudent(input));

        //No dot
        input = "Valid Name validEmail@emailcom";
        Assertions.assertFalse(student.addStudent(input));
    }

    @ParameterizedTest
    @CsvSource({"10000 10 10 5 8", "10000 5 8 7 3"})
    void testPointInputValid(String input) {
        Map<String, Course> courseMap = getCourseMap();


        Student student = new Student("10000", courseMap);
        student.addStudent("Jimmy Henderson-Korsakoff validEmail@email.com");
        StudentTracker tracker = new StudentTracker();
        tracker.updateMap(student);
        Assertions.assertTrue(tracker.isValidPoints(input));

    }


    @ParameterizedTest
    @CsvSource({"10000 7 7 7 7 7", "10000 -1 2 2 2", "10000 ? 1 1 1", "20000 5 8 7 3"})
    void testPointInputInvalid(String input) {
        Map<String, Course> courseMap = getCourseMap();

        Student student = new Student("10000", courseMap);
        student.addStudent("Jimmy Henderson-Korsakoff validEmail@email.com");
        StudentTracker tracker = new StudentTracker();
        tracker.updateMap(student);
        Assertions.assertFalse(tracker.isValidPoints(input));
    }

    //notify student that completed 1 course

    @ParameterizedTest
    @CsvSource({"10000 600 0 0 0", "10000 0 400 0 0", "10000 0 0 480 0", "10000 0 0 0 550"})
    void testNotification(String input) {
        Map<String, Course> courseMap = getCourseMap();




        Student student = new Student("10000", courseMap);
        student.addStudent("Jimmy Henderson-Korsakoff validEmail@email.com");
        StudentTracker tracker = new StudentTracker();
        tracker.updateMap(student);
        tracker.addSubmission(input);

        Assertions.assertEquals(1, tracker.getPendingNotifications().size());
        Assertions.assertEquals(0, tracker.getDeliveredNotifications().size());

        Assertions.assertFalse(tracker.getPendingNotifications().isEmpty());

        tracker.sendAllNotifications();

        tracker.addSubmission(input);

        tracker.sendAllNotifications();



        Assertions.assertTrue(tracker.getPendingNotifications().isEmpty());



    }




    //notify student that completed 2 courses




    //notify student that completed 1 course. Made another submission to another course without completing

    //notify 2 students that completed 1 course each










}