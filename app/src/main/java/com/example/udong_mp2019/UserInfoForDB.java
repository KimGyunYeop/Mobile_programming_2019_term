package com.example.udong_mp2019;

public class UserInfoForDB {
    public String email;
    public String password;
    public String name;
    public String school;
    public String studentId;

    public UserInfoForDB(){

    }
    public UserInfoForDB(String email, String password, String name, String school, String studentId){
        this.email=email;
        this.password=password;
        this.name=name;
        this.school=school;
        this.studentId=studentId;
    }
}