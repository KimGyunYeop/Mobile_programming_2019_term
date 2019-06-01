package com.MP2019.NDND.circle.Schedule;

public class ScheduleInfoForDB implements Comparable<ScheduleInfoForDB>{
    private String name;
    private String time;
    private String date;
    private String description;
    ScheduleInfoForDB(){

    }

    ScheduleInfoForDB(String name, String introduction, String time, String date){
        this.date = date;
        this.time = time;
        this.name = name;
        this.description =introduction;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return date+"/"+time+":"+name;
    }

    @Override
    public int compareTo(ScheduleInfoForDB o) {
        if(Integer.parseInt(this.time.split(":")[0]) < Integer.parseInt(o.time.split(":")[0])){
            return -1;
        }else if(Integer.parseInt(this.time.split(":")[0]) > Integer.parseInt(o.time.split(":")[0])){
            return 1;
        }else{
            if(Integer.parseInt(this.time.split(":")[1]) < Integer.parseInt(o.time.split(":")[1])){
                return -1;
            }else if(Integer.parseInt(this.time.split(":")[1]) > Integer.parseInt(o.time.split(":")[1])){
                return 1;
            }else{ return 0;}
        }
    }
}
