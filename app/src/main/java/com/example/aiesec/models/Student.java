package com.example.aiesec;

import java.io.Serializable;
import java.util.ArrayList;

public class Student implements Serializable {
    private Integer id;
    private String uniAbbreviation, phoneNum, uni, fullName, major;
    private String dob;
    public static ArrayList<Student> resultList = new ArrayList<>();
    public Student(){
        id = null;
        uniAbbreviation = phoneNum = uni = fullName = major = "";
        dob = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUniAbbreviation() {
        return uniAbbreviation;
    }

    public void setUniAbbreviation(String uniAbbreviation) {
        this.uniAbbreviation = uniAbbreviation;
    }

    public String getUni() {
        return uni;
    }

    public void setUni(String uni) {
        this.uni = uni;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString(){
        return "\nTên sinh viên: " + this.fullName
                + "\nMã số sinh viên: " + this.id
                + "\nTrường: " + this.uni + "\n";
    }
}
