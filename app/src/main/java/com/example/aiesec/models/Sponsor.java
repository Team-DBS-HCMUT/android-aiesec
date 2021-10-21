package com.example.aiesec;

import java.io.Serializable;
import java.util.ArrayList;

public class Sponsor implements Serializable {
    private Integer idSponsor, idDept, money;
    public Person person;
    public Company company;
    public static ArrayList<Sponsor> personList = new ArrayList<>();
    public static ArrayList<Sponsor> companyList = new ArrayList<>();
    public Sponsor(){
        person = null;
        company = null;
        idSponsor = idDept = money = null;
    }

    public Integer getIdSponsor() {
        return idSponsor;
    }

    public void setIdSponsor(Integer idSponsor) {
        this.idSponsor = idSponsor;
    }

    public Integer getIdDept() {
        return idDept;
    }

    public void setIdDept(Integer idDept) {
        this.idDept = idDept;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }


    public void setPerson(Person person) {
        this.person = person;
    }


    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString(){
        if (this.person != null){
            return "\nHọ và tên: " + this.person.fullName
                    + "\nSố tiền đã ủng hộ: " + this.money
                    + "đ\n";
        }

        return "\nTên công ty: " + this.company.compName
                + "\nNgười đại diện: " + this.company.repName
                + "\nSố tiền đã ủng hộ: " + this.money
                + "đ\nSĐT: " + this.company.getRepPhoneNum() + "\n";

    }

    static class Person implements Serializable{
        private Integer pMoney;
        private Integer CMND;
        private String fullName, village, district, province, dob;


        public Integer getCMND() {
            return CMND;
        }

        public void setCMND(Integer CMND) {
            this.CMND = CMND;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getVillage() {
            return village;
        }

        public void setVillage(String village) {
            this.village = village;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

    }
    static class Company implements Serializable{
        private Integer MS;
        private String compName, repName, repPhoneNum;

        public Integer getMS() {
            return MS;
        }

        public void setMS(Integer MS) {
            this.MS = MS;
        }

        public String getCompName() {
            return compName;
        }

        public void setCompName(String compName) {
            this.compName = compName;
        }

        public String getRepName() {
            return repName;
        }

        public void setRepName(String repName) {
            this.repName = repName;
        }

        public String getRepPhoneNum() {
            return repPhoneNum;
        }

        public void setRepPhoneNum(String repPhoneNum) {
            this.repPhoneNum = repPhoneNum;
        }
    }
}
