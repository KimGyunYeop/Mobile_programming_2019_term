package com.MP2019.NDND.circleList;

public class MemberInfoForDB {
    private String autority;
    private String date;

    public MemberInfoForDB(){

    }

    public MemberInfoForDB(String autority, String date){
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
