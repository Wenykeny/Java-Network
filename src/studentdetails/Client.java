/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentdetails;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 *
 * @author moisessuquila
 */
public class Client extends JFrame implements ActionListener {

    JButton btnRegister;
    JLabel lblID, lblFName, lblLName, lblContact, lblAddress, lblSpace;
    JTextField txtID, txtFName, txtLName, txtContact, txtAddress;

    public Client() {
        createForm();
    }

    public void createForm() {
        JFrame myForm = new JFrame("Student Details");
        myForm.setSize(400, 300);
        //myForm.setPreferredSize(new Dimension(300,300));
        myForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myForm.setLocation(550, 200);

        JPanel myPanel = new JPanel();
        //myPanel.setBackground(Color.red);
        myPanel.setLayout(new GridLayout(6, 2));

        lblID = new JLabel("Student ID:", JLabel.RIGHT);
        lblFName = new JLabel("First Name:", JLabel.RIGHT);
        lblLName = new JLabel("Last Name:", JLabel.RIGHT);
        lblContact = new JLabel("Contact Number:", JLabel.RIGHT);
        lblAddress = new JLabel("Address:", JLabel.RIGHT);
        lblSpace = new JLabel("");
        txtID = new JTextField();
        txtFName = new JTextField();
        txtLName = new JTextField();
        txtContact = new JTextField();
        txtAddress = new JTextField();
        btnRegister = new JButton("Register");

        //Prevent the txtfield to get string
        txtID.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });

        myPanel.add(lblID);
        myPanel.add(txtID);
        myPanel.add(lblFName);
        myPanel.add(txtFName);
        myPanel.add(lblLName);
        myPanel.add(txtLName);
        myPanel.add(lblContact);
        myPanel.add(txtContact);
        myPanel.add(lblAddress);
        myPanel.add(txtAddress);
        myPanel.add(lblSpace);
        myPanel.add(btnRegister);

        btnRegister.addActionListener(this);
        myForm.add(myPanel);
        //myForm.pack();
        myForm.setVisible(true);
    }

    public static void main(String[] args) {
        new Client();
    }

    private void showResponse(String title, String msg) {
        JOptionPane myJO = new JOptionPane(msg, JOptionPane.PLAIN_MESSAGE);
        JDialog mydialog = myJO.createDialog(null, title);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mydialog.setVisible(false);
            }
        }).start();
        mydialog.setVisible(true);
    } 

    @Override
    public void actionPerformed(ActionEvent e) {
        Object action = e.getSource();
        if (btnRegister.equals(action)) {
            int id = 0;
            String fname = "", lname = "", contact = "", address = "";
            try {
                if (!txtID.getText().equals("") && !txtFName.getText().equals("") && !txtLName.getText().equals("") && !txtContact.getText().equals("") && !txtAddress.getText().equals("")) {
                    //Establish connection with the server
                    Socket mySoc = new Socket("localhost", 8080);

                    //Create Object stream to send to and get msg from the server
                    ObjectOutputStream toServer = new ObjectOutputStream(mySoc.getOutputStream());
                    DataInputStream fromServer = new DataInputStream(mySoc.getInputStream());

                    //Get data from the GUI
                    id = Integer.parseInt(txtID.getText().replaceAll("[a-zA-Z]", ""));
                    fname = txtFName.getText();
                    lname = txtLName.getText();
                    contact = txtContact.getText();
                    address = txtAddress.getText();

                    //Create a Student Object and send it to the Server
                    StudentDetails studOb = new StudentDetails(id, fname, lname, contact, address);
                    toServer.writeObject(studOb);

                    //Show response
                    showResponse(fromServer.readUTF(), fromServer.readUTF());

                } else {
                    showResponse("Error", "Fields must contain data. They cannot be empty");
                }

            } catch (IOException | NumberFormatException ex) {
                showResponse("Error ", ex.getMessage() + "\n\nPlese, Provide correct input value!");
            }

        }
    }
}
