package test.project.together.model;

/**
 * Created by jeongdahun on 2017. 9. 8..
 */

public class Posting {
    int post_id;
    int user_id;
    public String image_path;
    String content;
    String date;
    int like_count;
    String name;

    public Posting(int user_id, String date, String content, String image_path, int like_count) {
        this.user_id = user_id;
        this.date = date;
        this.content = content;
        this.image_path = image_path;
        this.like_count = like_count;
    }

    public Posting(int post_id, int user_id, String date, String content, String image_path, int like_count, String name) {
        this.user_id = user_id;
        this.date = date;
        this.content = content;
        this.image_path = image_path;
        this.post_id = post_id;
        this.like_count = like_count;
        this.name = name;
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

    public int getLike_count() { return like_count; }

    public String getName() {return name;}
}
