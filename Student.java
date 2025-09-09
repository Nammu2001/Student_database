package jdbc.maven_demo;


import java.sql.*;
import java.util.Scanner;

public class Student {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/studentDB";
        String username = "postgres";
        String password = "root";

        try (Connection con = DriverManager.getConnection(url, username, password);
             Scanner sc = new Scanner(System.in)) {

            Class.forName("org.postgresql.Driver");

            while (true) {
                System.out.println("\n-------------------*------------------");
                System.out.println("1. ADD STUDENT");
                System.out.println("2. VIEW ALL STUDENTS");
                System.out.println("3. UPDATE STUDENT");
                System.out.println("4. DELETE STUDENT");
                System.out.println("5. SEARCH STUDENT BY ID");
                System.out.println("6. EXIT");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter Name: ");
                        String name = sc.next();
                        System.out.print("Enter Age: ");
                        int age = sc.nextInt();
                        System.out.print("Enter Course: ");
                        String course = sc.next();

                        String insertSQL = "INSERT INTO STUDENTS(STUDENT_NAME, STUDENT_AGE, STUDENT_COURSE) VALUES (?, ?, ?)";
                        try (PreparedStatement p = con.prepareStatement(insertSQL)) {
                            p.setString(1, name);
                            p.setInt(2, age);
                            p.setString(3, course);
                            p.executeUpdate();
                            System.out.println("Student added successfully!");
                        }
                        break;

                    case 2:
                        String fetchSQL = "SELECT * FROM STUDENTS";
                        try (PreparedStatement ps = con.prepareStatement(fetchSQL);
                             ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                System.out.println(rs.getInt("STUDENT_ID") + " | " +
                                        rs.getString("STUDENT_NAME") + " | " +
                                        rs.getInt("STUDENT_AGE") + " | " +
                                        rs.getString("STUDENT_COURSE"));
                            }
                        }
                        break;

                    case 3:
                        System.out.print("Enter Student ID to Update: ");
                        int uid = sc.nextInt();
                        System.out.print("Enter New Course: ");
                        String newCourse = sc.next();

                        String updateSQL = "UPDATE STUDENTS SET STUDENT_COURSE=? WHERE STUDENT_ID=?";
                        try (PreparedStatement up = con.prepareStatement(updateSQL)) {
                            up.setString(1, newCourse);
                            up.setInt(2, uid);
                            int rows = up.executeUpdate();
                            if (rows > 0) System.out.println("Student updated!");
                            else System.out.println("Student ID not found.");
                        }
                        break;

                    case 4:
                        System.out.print("Enter Student ID to Delete: ");
                        int did = sc.nextInt();

                        String deleteSQL = "DELETE FROM STUDENTS WHERE STUDENT_ID=?";
                        try (PreparedStatement dp = con.prepareStatement(deleteSQL)) {
                            dp.setInt(1, did);
                            int rows = dp.executeUpdate();
                            if (rows > 0) System.out.println("Student deleted!");
                            else System.out.println("Student ID not found.");
                        }
                        break;

                    case 5:
                        System.out.print("Enter Student ID to Search: ");
                        int sid = sc.nextInt();

                        String searchSQL = "SELECT * FROM STUDENTS WHERE STUDENT_ID=?";
                        try (PreparedStatement sp = con.prepareStatement(searchSQL)) {
                            sp.setInt(1, sid);
                            try (ResultSet rs = sp.executeQuery()) {
                                if (rs.next()) {
                                    System.out.println(rs.getInt("STUDENT_ID") + " | " +
                                            rs.getString("STUDENT_NAME") + " | " +
                                            rs.getInt("STUDENT_AGE") + " | " +
                                            rs.getString("STUDENT_COURSE"));
                                } else {
                                    System.out.println("Student not found.");
                                }
                            }
                        }
                        break;

                    case 6:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice. Try again!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

