package tracker;

import java.util.*;

public class StudentTracker {

    private final List<String> emailList;
    private final Map<String, Student> studentMap;

    private final Map<String, Course> courseMap;

    private int IDTracker;
    private ArrayDeque<Notification> pendingNotifications;
    private ArrayList<Notification> deliveredNotifications;

    public StudentTracker() {
        this.IDTracker = 10000;
        this.emailList = new ArrayList<>();
        this.studentMap = new LinkedHashMap<>(); //all students are stored in here
        courseMap = new HashMap<>();
        courseMap.put("Java", new Course("Java", 600));
        courseMap.put("DSA", new Course("DSA", 400));
        courseMap.put("Databases", new Course("Databases", 480));
        courseMap.put("Spring", new Course("Spring", 550));
        this.pendingNotifications = new ArrayDeque<>();
        this.deliveredNotifications = new ArrayList<>();
    }


    public void start() {
        Scanner scanner = new Scanner(System.in);
        List<Student> studentList = new ArrayList<>();



        System.out.println("Learning Progress Tracker");
        boolean running = true;

        do {
            String input = scanner.nextLine();

            if (input.matches("\\s*")) {
                System.out.println("No input.");
                continue;
            }
            switch (input) {
                case "exit" -> {
                    System.out.println("Bye!");
                    running = false;
                }
                case "back" -> {
                    System.out.println("Enter 'exit' to exit the program");
                }
                case "add students" -> {
                    addStudents(scanner, studentList);
                }
                case "list" -> {
                    printStudents();
                }
                case "add points" -> {
                    addPoints(scanner);
                }
                case "find" -> {
                    findStudent(scanner);
                }
                case "statistics" -> {
                    showStatistics(scanner);
                }
                case "notify" -> {
                    sendAllNotifications();
                }

                default -> System.out.println("Error: unknown command!");
            }

        } while (running);
    }

    public void showStatistics(Scanner scanner) {
        Statistics studentStatistics = new Statistics(studentMap, courseMap);
        studentStatistics.printAllCourseRankings();

        while (true) {

            String input = scanner.nextLine();

            if (input.equals("back")) {
                break;
            }

            switch (input) {
                case "java", "Java" ->
                    studentStatistics.printCourseInfo(courseMap.get("Java"));
                case "dsa", "DSA" ->
                        studentStatistics.printCourseInfo(courseMap.get("DSA"));
                case "Databases", "databases" ->
                        studentStatistics.printCourseInfo(courseMap.get("Databases"));
                case "Spring", "spring" ->
                        studentStatistics.printCourseInfo(courseMap.get("Spring"));
                default -> System.out.println("Unknown course.");

            }




        }


    }


    public void addStudents(Scanner scanner, List<Student> studentList) {

        System.out.println("Enter student credentials or 'back' to return:");
        while (true) {
            Student newStudent = new Student(String.valueOf(IDTracker), courseMap);
            String input = scanner.nextLine();

            if (input.equals("back")) {
                System.out.println("Total " + studentList.size() + " students have been added.");
                break;
            }

            if (newStudent.addStudent(input)) { //check if input is valid
                if (emailList.contains(newStudent.getEmail())) { //email already registered!
                    System.out.println("This email is already taken.");
                    continue;
                }
                this.emailList.add(newStudent.getEmail());
                studentList.add(newStudent);
                updateMap(newStudent);
                IDTracker++;
                System.out.println("The student has been added.");
            }

        }
    }

    public void findStudent(Scanner scanner) {
        System.out.println("Enter an id or 'back' to return:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("back")) {
                break;
            }
            if (isValidStudent(input)) {
                studentMap.get(input).printGrades();
            }
        }
    }

    public boolean isValidStudent(String studentID) {
        if (!studentID.matches("^\\d+$") || !studentMap.containsKey(studentID)) {
            System.out.println("No student is found for id=" + studentID + ".");
            return false;
        }
        return true;
    }

    public void updateMap(Student newStudent) {
        studentMap.putIfAbsent(newStudent.getId(), newStudent);
    }

    public void printStudents() {
        if (studentMap.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("Students:");
        for (String id : studentMap.keySet()) {
            System.out.println(id);
        }
    }


    public void addPoints(Scanner scanner) {
        System.out.println("Enter an id and points or 'back' to return:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("back")) {
                break;
            }

            if (isValidPoints(input)) {
                addSubmission(input);
                System.out.println("Points updated.");
            }
        }
    }

    public void addSubmission(String input) {
        String[] splitInput = input.split("\\s");
        Student targetStudent = studentMap.get(splitInput[0]);

        int javaScore = Integer.parseInt(splitInput[1]);
        int dsaScore = Integer.parseInt(splitInput[2]);
        int databasesScore = Integer.parseInt(splitInput[3]);
        int springScore = Integer.parseInt(splitInput[4]);


        targetStudent.sendSubmission(javaScore, dsaScore, databasesScore, springScore);

        //check if the course was completed and queue notification
        checkCourseCompletion("Java", javaScore, targetStudent);
        checkCourseCompletion("DSA", dsaScore, targetStudent);
        checkCourseCompletion("Databases", databasesScore, targetStudent);
        checkCourseCompletion("Spring", springScore, targetStudent);



    }

    private void checkCourseCompletion(String courseName, int score, Student targetStudent) {
        if (score >= courseMap.get(courseName).getRequiredCredits()) {
            Notification newNotification = new Notification(targetStudent, courseMap.get(courseName));
            for (Notification notification : deliveredNotifications) { //check if notification was already sent
                if (newNotification.equals(notification)){
                    return;
                }
            }
            for (Notification notification : pendingNotifications) { //check if notification is already prepared
                if (newNotification.equals(notification)){
                    return;
                }
            }

            pendingNotifications.add(newNotification); //queue notification


        }
    }


    public boolean isValidPoints(String input) {
        String validRegex = "^[\\d\\s]+$";

        String[] splitInput = input.split("\\s");

        if (!isValidStudent(splitInput[0])) {
            System.out.println("No student is found for id=" + splitInput[0] + ".");
            return false;
        }


        if (!input.matches(validRegex)) {
            System.out.println("Incorrect points format.");
            return false;
        }


        if (splitInput.length != 5) {
            System.out.println("Incorrect points format.");
            return false;
        }



        return true;
    }

    protected void sendAllNotifications() {
        if (pendingNotifications.isEmpty()) {
            System.out.println("Total 0 students have been notified.");
            return;
        }

        List<Student> notifiedStudents = new ArrayList<>();

        while (!pendingNotifications.isEmpty()) {
            Notification notification = pendingNotifications.pop();
            deliveredNotifications.add(notification);
            notification.printNotification();

            if (!notifiedStudents.contains(notification.getStudent())) { //count total students that were notified by this action
                notifiedStudents.add(notification.getStudent());
            }
        }

        System.out.println("Total " + notifiedStudents.size() +  " students have been notified.");





    }


    public ArrayDeque<Notification> getPendingNotifications() {
        return pendingNotifications;
    }

    public ArrayList<Notification> getDeliveredNotifications() {
        return deliveredNotifications;
    }
}
