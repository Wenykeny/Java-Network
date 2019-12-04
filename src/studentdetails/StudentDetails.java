/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentdetails;

import java.io.*;

/**
 *
 * @author moisessuquila
 */
public class StudentDetails implements Serializable{
    private int stud_id;
    private String stud_fname, stud_lname, stud_contact, stud_address;
    
    public StudentDetails(int id,String fname,String lname,String contact,String address){
        this.stud_id = id;
        this.stud_fname = fname;
        this.stud_lname = lname;
        this.stud_contact = contact;
        this.stud_address = address;
    }

    public int getStud_id() {
        return stud_id;
    }

    public String getStud_fname() {
        return stud_fname;
    }

    public String getStud_lname() {
        return stud_lname;
    }

    public String getStud_contact() {
        return stud_contact;
    }

    public String getStud_address() {
        return stud_address;
    }

}
