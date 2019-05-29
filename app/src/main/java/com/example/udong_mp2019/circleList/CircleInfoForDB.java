package com.example.udong_mp2019.circleList;

public class CircleInfoForDB {
    private String name;
    private String school;
    private String date;
    private String introduction;
    CircleInfoForDB(){

    }

    CircleInfoForDB(String name, String school, String date, String introduction){
        this.date = date;
        this.school = school;
        this.name = name;
        this.introduction=introduction;
    }

    public String getAutority(String autority) {
        return autority;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public MemberInfoForDB getMemberInfoForDB(MemberInfoForDB memberInfoForDB){
        return memberInfoForDB;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return school+":"+name;
    }
}
