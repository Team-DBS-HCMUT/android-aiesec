package com.example.aiesec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {
    private Integer id, idDep;
    private String fullName, phoneNum, dob, role;
    public static ArrayList<Member> resultList = new ArrayList<>();

    public void setId(Integer id ) {
        this.id = id;
    }

    public void setIdDep(Integer idDep) {
        this.idDep = idDep;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getIdDep() {
        return idDep;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getDob() {
        return dob;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString(){
        return "\nHọ và tên: " + this.fullName
                + "\nID: " + this.id
                + "\nVai trò: " + this.role
                + "\n";
    }
}
