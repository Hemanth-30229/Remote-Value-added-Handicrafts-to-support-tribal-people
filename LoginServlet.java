package project;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("loginType"); // Added to get the role type

        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/handicraft", "root", "Chandu_0000");

            // Query the user from the database
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String storedRole = rs.getString("role");

                // Verify the password
                if (BCrypt.checkpw(password, storedPassword)) {
                    // Check if the roles match
                    if (storedRole.equals(role)) {
                        // Set session attributes
                        HttpSession session = request.getSession();
                        session.setAttribute("username", username);
                        session.setAttribute("role", storedRole);

                        // Redirect based on role
                        switch (storedRole) {
                            case "customer":
                                response.sendRedirect("customer.jsp");
                                break;
                            case "admin":
                                response.sendRedirect("admin.jsp");
                                break;
                            case "artisan":
                                response.sendRedirect("artisan.jsp");
                                break;
                            case "culturalConsultant":
                                response.sendRedirect("culturalConsultant.jsp");
                                break;
                        }
                    } else {
                        // Role mismatch
                        redirectWithError(request, response, role, "Invalid credentials for this role");
                    }
                } else {
                    // Invalid password
                    redirectWithError(request, response, role, "Invalid username or password");
                }
            } else {
                // User not found
                redirectWithError(request, response, role, "Invalid username or password");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            redirectWithError(request, response, role, "Database error");
        }
    }

    // Helper method to redirect with an error message
    private void redirectWithError(HttpServletRequest request, HttpServletResponse response, String role, String errorMessage) throws IOException {
        // Store the error message and redirect to the appropriate login page
        request.setAttribute("error", errorMessage);
        String redirectPage = role + "Login.jsp"; // Dynamically set the login page based on the role
        try {
			request.getRequestDispatcher(redirectPage).forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
