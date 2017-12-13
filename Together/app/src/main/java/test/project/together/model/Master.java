package test.project.together.model;

/**
 * Created by jeongdahun on 2017. 9. 8..
 */

public class Master {
    int user_id;
    double origin_lat;
    double origin_long;
    double range;

    public Master(int user_id, double origin_lat, double origin_long, double range){
        this.user_id=user_id;
        this.origin_lat=origin_lat;
        this.origin_long=origin_long;
        this.range=range;
    }

}
