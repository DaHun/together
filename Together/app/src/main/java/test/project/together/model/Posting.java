package test.project.together.model;

/**
 * Created by jeongdahun on 2017. 9. 8..
 */

public class Posting {
    public String image_path;
    String date;
    String content;
    int user_id;
    int post_id;

    public Posting(int user_id, String date, String content, String image_path) {
        this.user_id = user_id;
        this.date = date;
        this.content = content;
        this.image_path = image_path;
    }

    public Posting(int post_id, int user_id, String date, String content, String image_path) {
        this.user_id = user_id;
        this.date = date;
        this.content = content;
        this.image_path = image_path;
        this.post_id = post_id;
    }

    public Posting() {
    }

    public int getPost_id() {
        return post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getDate(){
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getImage_path() {
        return image_path;
    }
}
