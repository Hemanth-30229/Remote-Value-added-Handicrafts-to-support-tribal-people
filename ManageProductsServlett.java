package artisan;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

//@WebServlet("/manageProducts")
public class ManageProductsServlett extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/handicraft";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Chandu_0000";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM products WHERE is_approved = 1")) {

            out.println("<div class='product-grid'>");

            while (resultSet.next()) {
                out.println("<div class='product-item'>");
                out.println("<img src='uploads/" + resultSet.getString("image") + "' alt='" + resultSet.getString("name") + "' class='product-image' />");
                out.println("<h3>" + resultSet.getString("name") + "</h3>");
                out.println("<p class='description'>" + resultSet.getString("description") + "</p>");
                out.println("<p class='price'>Price: $" + resultSet.getDouble("price") + "</p>");
                out.println("<p class='stock'>Stock: " + resultSet.getInt("stock") + "</p>");
                out.println("</div>");
            }

            out.println("</div>");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h2>Error retrieving products</h2>");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
