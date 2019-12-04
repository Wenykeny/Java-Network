/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentdetails;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.logging.*;

/**
 *
 * @author moisessuquila
 */
public class Server {

    //static int count = 1;

    public static void main(String[] args) throws IOException, SQLException {
        new Server();
    }

    public Server() throws IOException, SQLException {
        try {
            ServerSocket myServer = new ServerSocket(8080); //Network Connection
            System.out.println("********** Server Online **********\n");

            //Setting the server to accept multiple connections
            while (true) {
                Socket mySoc = myServer.accept();
                SaveToDatabase save = new SaveToDatabase(mySoc);
                new Thread(save).start();
                //System.out.println("------------ Client no: " + count++ + " ------------ ");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}

class SaveToDatabase implements Runnable {

    private Connection conn;
    private PreparedStatement stmt;
    ObjectInputStream inObject;
    ObjectOutputStream outObject;
    DataOutputStream outObjectResponse;

    public SaveToDatabase(Socket mySoc) throws IOException, SQLException {
        inObject = new ObjectInputStream(mySoc.getInputStream());
        outObjectResponse = new DataOutputStream(mySoc.getOutputStream());
        conn = DriverManager.getConnection("jdbc:mysql://localhost/PIHE2019", "root", "");
    }

    public String saveToDatabase(StudentDetails studData) throws SQLException {
        String response = "";
        if (searchResult(studData.getStud_id()) == 0) {
            stmt = conn.prepareStatement("INSERT INTO details(stud_id,stud_fname,stud_lname,stud_contactNo,stud_address) VALUES(?,?,?,?,?)");

            // Prepere the value to be inserted into the database
            stmt.setInt(1, studData.getStud_id());
            stmt.setString(2, studData.getStud_fname());
            stmt.setString(3, studData.getStud_lname());
            stmt.setString(4, studData.getStud_contact());
            stmt.setString(5, studData.getStud_address());
            
            //Execute query
            if (stmt.execute() == false) {
                response = "inserted";
            }else{
                response = "uninserted";
            }
            
        }else{
            response = "exists";
        }
        return response;
    }

    private int searchResult(int getID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM details WHERE stud_id = ?");
        stmt.setInt(1, getID);
        ResultSet rs = stmt.executeQuery();

        int count = 0;
        while (rs.next()) {
            count++;
        }
        return count;
    }

    public String ShowData(StudentDetails myStud) {
        return myStud.getStud_fname().substring(0, 1).toUpperCase() + myStud.getStud_fname().substring(1).toLowerCase();
    }

    @Override
    public void run() {
        try {

            //Get a object from the client
            Object getStudentObject = inObject.readObject();

            //Cast and send the object to a method to save it.
            switch (saveToDatabase((StudentDetails) getStudentObject)) {
                case "exists":
                    outObjectResponse.writeUTF("Alert! Student ID");
                    outObjectResponse.writeUTF("This Student ID already exist. Please select other ID number");
                    break;
                case "inserted":
                    outObjectResponse.writeUTF("Success! Student Info");
                    outObjectResponse.writeUTF(ShowData((StudentDetails) getStudentObject) + "'s data was successfully stored");
                    break;
                case "uninserted":
                    outObjectResponse.writeUTF("Dangerous! System Error");
                    outObjectResponse.writeUTF("Sorry! For some reason SYSTEM CRASHED");
                    break;
                default:
                    outObjectResponse.writeUTF("Dangerous! System Error");
                    outObjectResponse.writeUTF("Sorry! For some reason SYSTEM CRASHED");
            }

            System.out.println("");
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SaveToDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
