package com.example.udongmp;

import java.util.Date;

public class CircleInfoForDB {
    private String name;
    private String school;
    private String date;

    CircleInfoForDB(){

    }

    CircleInfoForDB(String name, String school, String date){
        this.date = date;
        this.school = school;
        this.name = name;
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

    @Override
    public String toString() {
        return school+":"+name;
    }
}
