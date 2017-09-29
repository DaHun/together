package test.project.together.model;

/**
 * Created by jeongdahun on 2017. 9. 8..
 */

public class Matching {
    String user_id;
    String location;
    double latitude;
    double longitude;
    String wish;
    String date;
    String startTime;
    String finishTime;
    int check;


    public Matching(String user_id, String location, double latitude, double longitude, String wish, String date, String startTime, String finishTime, int check) {
        this.user_id = user_id;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.wish = wish;
        this.date = date;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.check = check;
    }

    public Matching() {
    }

    public String getUser_id() {
        return user_id;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getWish() {
        return wish;
    }

    public String getDate(){
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public int isCheck() {
        return check;
    }
}
