package com.MP2019.NDND.login;

public class UserInfoForDB {
    public String email;
    public String name;
    public String school;
    public String studentId;

    public UserInfoForDB(){

    }
    public UserInfoForDB(String email, String name, String school, String studentId){
        this.email=email;
        this.name=name;
        this.school=school;
        this.studentId=studentId;
    }
    public String getName() {
        return name;
    }
    public String getEmail(){return email;
    }
}