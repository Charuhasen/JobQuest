package com.example.jobquestV1;

import java.util.List;
//Applicants model class with setters and getter
public class MyApplicantsModel {

    String fName;
    String lName;
    String email;
    String phNumber;
    String UserID;

    MyApplicantsModel(String fName, String lName, String email, String phNumber, String UserID) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phNumber = phNumber;
        this.UserID = UserID;
    }

    MyApplicantsModel() {

    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}