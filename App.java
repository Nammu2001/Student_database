package jdbc.maven_demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String url = "jdbc:postgresql://localhost:5432/user_management";
		String username="postgres";
		String password = "root";
		try {
			//load the driver
			Class.forName("org.postgresql.Driver");
			//establish the connection
			Connection con = DriverManager.getConnection(url, username, password);
			//create using prepared statement
			String createUser = "INSERT INTO company.employee1(EMP_NAME,EMP_AGE,EMP_EMAIL,EMP_SALARY,DEPT_ID)VALUES(?,?,?,?,?)";
					PreparedStatement pstmt = con.prepareStatement(createUser);
					pstmt.setString(1, "Nammu");
					pstmt.setInt(2, 24);
					pstmt.setString(3, "nammu@gmail.com");
					pstmt.setDouble(4, 20000);
					pstmt.setInt(5, 3);
					//pstmt.addBatch();
					//int[] result= pstmt.executeBatch()
					pstmt.executeUpdate();
					System.out.println("1 row inserted");
					
					//fetch
					String fetchuser ="SELECT * FROM company.employee1";
					PreparedStatement fecthpstmt = con.prepareStatement(fetchuser);
					ResultSet res = fecthpstmt.executeQuery();
					while(res.next()) {
						System.out.println(res.getString("emp_name"));
					}
					
					//update
					String updateUser="UPDATE company.employee1 SET dept_id=? WHERE emp_id=? ";
					PreparedStatement updatepstmt = con.prepareStatement(updateUser);
					updatepstmt.setInt(1, 5);
					updatepstmt.setInt(2, 13);
					updatepstmt.executeUpdate();
					System.out.println("user updated");
					
					//delete
					String deleteUser="DELETE FROM company.employee1 WHERE EMP_ID=?";
					PreparedStatement deletepstmt = con.prepareStatement(deleteUser);
					deletepstmt.setInt(1, 11);
					deletepstmt.executeUpdate();
					System.out.println("user deleted");
					
		con.close();	
		}catch(Exception e) {
			System.out.println("error" + e.getMessage());
		}
    }
}
