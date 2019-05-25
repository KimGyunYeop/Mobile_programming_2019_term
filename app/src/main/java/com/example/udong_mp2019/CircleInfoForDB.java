package com.example.udong_mp2019;

import java.util.Date;

public class CircleInfoForDB {
    private String name;
    private String school;
    private String date;
    private String introduction;
    CircleInfoForDB(){

    }

    CircleInfoForDB(String name, String introduction, String school, String date){
        this.date = date;
        this.school = school;
        this.name = name;
        this.introduction=introduction;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
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
