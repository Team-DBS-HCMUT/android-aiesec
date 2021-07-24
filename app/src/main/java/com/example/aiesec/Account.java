package com.example.aiesec;

import java.io.Serializable;

public final class Account implements Serializable {
    public static final int DEPT_MANAGER = 0;
    public static final int DEPT_MEMBER = 1;
    public static final int MANAGEMENT_DEPT = 1;
    public static final int EVENT_DEPT = 2;
    public static final int FINANCE_DEPT = 3;
    private String userName;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private int role;
    private int dept;
    public Account(){
        userName = "";
        password = "";
        name = "";
        role = -1;
        dept = -1;
    }

    public int getDept() {
        return dept;
    }

    public void setDept(int dept) {
        this.dept = dept;
    }

    public Account(String userName, String password){
        this.userName = new String(userName);
        this.password = new String(password);
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public int getRole(){
        return role;
    }

    public void setRole(int role){
        this.role = role;
    }

    public void setPassword(String password){
        this.password = password;
    }

}
