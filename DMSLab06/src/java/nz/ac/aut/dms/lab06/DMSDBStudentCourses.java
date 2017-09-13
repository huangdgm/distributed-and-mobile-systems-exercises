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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class DMSDBStudentCourses extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private String dbDriverURL, dbURL, sqlQuery, sqlQueryCourse;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet, resultSetCourse, resultSetStudent;
    PrintWriter out;
    String studentID;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        boolean StudentCourses;
                
        studentID = (String)request.getParameter("studentID");
        if(studentID == null)
            StudentCourses = false;
        else
            StudentCourses = true;
        
        response.setContentType("text/html;charset=UTF-8");
        try {
            out = response.getWriter();
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DMSDBStudentCourses</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DMSDBStudentCourses at " +
                    request.getContextPath() + "</h1>");
            if(StudentCourses){
                out.println("<h1>You are looking for the courses for"
                        + " Student ID " + studentID + "</h1>");
                connectDMSDB();
                displayStudentCourses();
            }
            else
                out.println("Request does not contain the StudentID Parameter");
            out.println("</body>");
            out.println("</html>");
        } catch(IOException ex){
            Logger.getLogger(DMSDBStudentCourses.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void displayStudentCourses(){
        sqlQuery = "select courseID, stID from STUDENT_COURSE_RESULT "
                + " where stID = ?";
        
        try {
            // Step 3: Creating the SQL Statement
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1 , studentID);
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                sqlQuery = "select * from STUDENT"
                        + " where stID = ?";
                sqlQueryCourse = "select * from COURSE"
                + " where COURSEID = ?";
            }
            else{
                out.println("<h1>Student ID : " + studentID
                        + " is not registered for any course </h1>");
                return;
            }
            
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1 , resultSet.getString(2));
            resultSetStudent = statement.executeQuery();
            if(resultSetStudent.next()){
                out.println("<h1>Student ID : "
                        + resultSetStudent.getString(1) + "</h1>");
                out.println("<h1>Student Name : "
                        + resultSetStudent.getString(2) + " " 
                        + resultSetStudent.getString(3) + "</h1>");
                out.println("<h1>Courses of this student are:</h1>");
            }
                        
            statement = connection.prepareStatement(sqlQueryCourse);
            statement.setString(1 , resultSet.getString(1));
            resultSetCourse = statement.executeQuery();
            
            out.println("<h2>COURSEES for the above Student from DMS Database</h2>");
            out.println("<TABLE  border=\"2\" bordercolor=\"#000000\""
                    + "cellpadding=\"5px\">");
            out.println("<TR><TD><b>Course ID</b></TD>"
                    + "<TD><b>Course Name</b></TD>"
                    + "<TD><b>Course Description</b></TD>"
                    + "<TD><b>Course Level</b></TD></TR>");
            while(resultSetCourse.next()){
                out.println("<TR>");
                out.println("<TD>" + resultSetCourse.getString(1) + "</TD><TD>"
                        + resultSetCourse.getString(2) + "</TD><TD>"
                        + resultSetCourse.getString(3) + "</TD><TD>"
                        + resultSetCourse.getString(4) + "</TD>");
                out.println("</TR>");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DMSDBStudentCourses.class.getName()).log(Level.SEVERE, null, ex);
            out.println(ex.getMessage());
        }
        out.println("</TABLE>");
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
            out.println("</p>");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DMSDBStudentCourses.class.getName()).log(Level.SEVERE, null, ex);
            out.println(ex.getMessage());
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
