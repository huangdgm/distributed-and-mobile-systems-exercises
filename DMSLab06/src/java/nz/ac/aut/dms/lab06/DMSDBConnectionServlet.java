/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.lab06;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Dong Huang
 */
public class DMSDBConnectionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private String dbDriverURL, dbURL, sqlQuery;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    PrintWriter out;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        try {
            out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DMS DB Connection Servlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DMS DB Connection Servlet at " + request.getContextPath() + "</h1>");
            
            // Connecting with DMS Database
            connectDMSDB();
            
            // Displaying COURSE Table
            displayCourses();
            
            // Displaying STUDENT Table
            displayStudents();
            
            out.println("</body>");
            out.println("</html>");
        } catch(IOException ex){
            Logger.getLogger(
                    DMSDBConnectionServlet.class.getName()).log(Level.SEVERE,
                            null, ex);
        }
    }
    
    private void connectDMSDB(){
        try {
            dbDriverURL = "org.apache.derby.jdbc.ClientDriver";
            dbURL = "jdbc:derby://localhost:1527/DMSDB;" +
                    "user=dms;password=dms2017";
            
            // Step 1: Loading the drivers for JAVA DB
            Class.forName(dbDriverURL);
            
            // Step 2: Connecting to sample Database in Java DB
            connection = DriverManager.getConnection(dbURL);
            out.println("<h2> Connecting with the Database DMS</h2>");
            out.println("<p>");
            out.println("Database is connected...");
            
            // Step 3: Creating the SQL Statement
            statement = connection.createStatement();
            out.println("<br>Statement is created...");
            out.println("</p>");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(
                    DMSDBConnectionServlet.class.getName()).log(Level.SEVERE,
                            null, ex);
        }
    }
    
    private void displayCourses(){
        out.println("<h2>COURSE Table from DMS Database: Contents</h2>");
        out.println("<TABLE  border=\"2\" bordercolor=\"#000000\""
                + "cellpadding=\"5px\">");
        sqlQuery = "select * from COURSE";
        try {
            resultSet = statement.executeQuery(sqlQuery);
            // Step 7: Reading data from the ResultSet
            out.println("<TR><TD><b>Course ID</b></TD>"
                    + "<TD><b>Course Name</b></TD>"
                    + "<TD><b>Course Description</b></TD>"
                    + "<TD><b>Course Level</b></TD></TR>");
            while(resultSet.next()){
                out.println("<TR>");
                out.println("<TD>" + resultSet.getString(1) + "</TD><TD>"
                        + resultSet.getString(2) + "</TD><TD>"
                        + resultSet.getString(3) + "</TD><TD>"
                        + resultSet.getString(4) + "</TD>");
                out.println("</TR>");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DMSDBConnectionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.println("</TABLE>");
    }
    
    private void displayStudents(){
        out.println("<h2>STUDENT Table from DMS Database: Contents</h2>");
        out.println("<TABLE  border=\"2\" bordercolor=\"#000000\""
                + "cellpadding=\"5px\">");
        sqlQuery = "select * from STUDENT";
        try {
            resultSet = statement.executeQuery(sqlQuery);
            // Step 7: Reading data from the ResultSet
            out.println("<TR><TD><b>Student ID</b></TD>"
                    + "<TD><b>Student First Name</b></TD>"
                    + "<TD><b>Student Last Name</b></TD>"
                    + "<TD><b>Student's Courses</b></TD></TR>");
            while(resultSet.next()){
                out.println("<TR>");
                out.println("<TD>" + resultSet.getString(1) + "</TD><TD>"
                        + resultSet.getString(2) + "</TD><TD>"
                        + resultSet.getString(3) + "</TD>"
                        + "<TD><A HREF=\"/LAB06/DMSDBStudentCourses?studentID="
                        + resultSet.getString(1) + "\">Courses</A></TD>");
                out.println("</TR>");
            }
        } catch (SQLException ex) {
            Logger.getLogger(
                    DMSDBConnectionServlet.class.getName()).log(Level.SEVERE,
                            null, ex);
        }
        out.println("</TABLE>");
    }
    
    public void destroy(){
        super.destroy();
        // close database connection
        try
        {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }catch (SQLException e){
            System.err.println("SQL Exception while closing: " + e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
