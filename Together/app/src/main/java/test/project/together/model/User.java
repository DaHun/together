package test.project.together.model;

/**
 * Created by jeongdahun on 2017. 9. 8..
 */

public class User {
    public String user_id;
    public String name;
    public String phone;
    public String age;
    public String gender;
    public String token;

    public User(String name, String phone, String age, String gender, String token) {
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
        this.token = token;
    }
}
