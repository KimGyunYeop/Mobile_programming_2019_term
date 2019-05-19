package com.example.udongmp;

public class MemberInfoForDB {
    private String autority;
    private String date;

    MemberInfoForDB(){

    }

    MemberInfoForDB(String autority, String date){
        this.autority = autority;
        this.date= date;
    }


    public String getAutority() {
        return autority;
    }

    public String getDate() {
        return date;
    }
}
