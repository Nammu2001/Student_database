package jdbc.maven_demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class LibraryDB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "jdbc:postgresql://localhost:5432/LIBRARY_MANAGEMENT_SYSTEM";
		String username = "postgres";
		String password = "root";
		try {
			Class.forName("org.postgresql.Driver");
			try (Connection con = DriverManager.getConnection(url, username, password);
					Scanner sc = new Scanner(System.in)) {
				while (true) {
					System.out.println("1.Add a New Book");
					System.out.println("2.View All Books");
					System.out.println("3.Search Book");
					System.out.println("4. Issue Book");
					System.out.println("5.Return Book");
					System.out.println("6.Delete Book");
					System.out.println("7.exit");
					System.out.print("enter the choice : ");
					int choice = sc.nextInt();
					sc.nextLine();
					switch (choice) {
					case 1:
						System.out.println("enter the book title :");
						String title = sc.nextLine();
						System.out.println("enter the author :");
						String author = sc.nextLine();
						System.out.println("enter the price :");
						double price = sc.nextDouble();
						System.out.println("available copies :");
						int copies = sc.nextInt();

						String addBook = "insert into books(title,author,price,available_copies)values(?, ?, ?, ?)";
						try (PreparedStatement ps = con.prepareStatement(addBook)) {
							ps.setString(1, title);
							ps.setString(2, author);
							ps.setDouble(3, price);
							ps.setInt(4, copies);
							ps.executeUpdate();
							System.out.println("book added successfully!");
						}
						break;
					case 2:
						String view = "select * from books";
						try (PreparedStatement ps = con.prepareStatement(view)) {
							ResultSet rs = ps.executeQuery();
							while (rs.next()) {
								System.out.println(
										rs.getInt("id") + " " + rs.getString("title") + " " + rs.getString("author")
												+ " " + rs.getDouble("price") + " " + rs.getInt("available_copies"));
							}
						}
						break;
					case 3:
						System.out.print("Enter book ID or Name: ");
						String keyword = sc.nextLine();
						String searchSQL = "SELECT * FROM books WHERE CAST(id AS TEXT) = ? OR title ILIKE ?";
						try (PreparedStatement ps = con.prepareStatement(searchSQL)) {
							ps.setString(1, keyword);
							ps.setString(2, "%" + keyword + "%");
							try (ResultSet rs = ps.executeQuery()) {
								boolean found = false;
								while (rs.next()) {
									System.out.println(rs.getInt("id") + " | " + rs.getString("title") + " | "
											+ rs.getString("author") + " | " + rs.getDouble("price") + " | "
											+ rs.getString("available_copies"));
									found = true;
								}
								if (!found)
									System.out.println("book not found.");
							}
						}
						break;
					case 4:
						   System.out.print("Enter book ID to issue: ");
                           int issueId = sc.nextInt();
                           String checkCopies = "SELECT available_copies FROM books WHERE id=?";
                           try (PreparedStatement ps = con.prepareStatement(checkCopies)) {
                               ps.setInt(1, issueId);
                               ResultSet rs = ps.executeQuery();
                               if (rs.next()) {
                                   int available = rs.getInt("available_copies");
                                   if (available > 0) {
                                       String issue = "UPDATE books SET available_copies = available_copies - 1 WHERE id=?";
                                       try (PreparedStatement ps2 = con.prepareStatement(issue)) {
                                           ps2.setInt(1, issueId);
                                           ps2.executeUpdate();
                                           System.out.println(" Book issued successfully!");
                                       }
                                   } else {
                                       System.out.println(" No copies available.");
                                   }
                               } else {
                                   System.out.println(" Book not found.");
                               }
                           }
                           break;
					case 5:
					     System.out.print("Enter book ID to return: ");
                         int returnId = sc.nextInt();
                         String returnBook = "UPDATE books SET available_copies = available_copies + 1 WHERE id=?";
                         try (PreparedStatement ps = con.prepareStatement(returnBook)) {
                             ps.setInt(1, returnId);
                             int rows = ps.executeUpdate();
                             if (rows > 0) {
                                 System.out.println("Book returned successfully!");
                             } else {
                                 System.out.println(" Book not found.");
                             }
                         }
                         break;
					case 6:
						  System.out.print("Enter book ID to delete: ");
                          int deleteId = sc.nextInt();
                          String deleteBook = "DELETE FROM books WHERE id=?";
                          try (PreparedStatement ps = con.prepareStatement(deleteBook)) {
                              ps.setInt(1, deleteId);
                              int rows = ps.executeUpdate();
                              if (rows > 0) {
                                  System.out.println(" Book deleted successfully!");
                              } else {
                                  System.out.println(" Book not found.");
                              }
                          }
                          break;
					case 7:
                        System.out.println("Exiting... ");
                        return;

                    default:
                        System.out.println(" Invalid choice! Try again.");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
		}
	}

}
