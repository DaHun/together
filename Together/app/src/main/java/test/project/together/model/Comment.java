package test.project.together.model;

/**
 * Created by jeongdahun on 2017. 9. 8..
 */

public class Comment {
    int comment_id;
    int post_id;
    String content;
    String date;
    int user_id;
    String name;
    String image_path;


    public Comment(int post_id, String content, String date, int user_id) {
        this.post_id = post_id;
        this.content = content;
        this.date = date;
        this.user_id = user_id;
    }

    public Comment(int comment_id, int post_id, String content, String date, int user_id, String name, String image_path) {
        this.comment_id=comment_id;
        this.post_id = post_id;
        this.content = content;
        this.date = date;
        this.user_id = user_id;
        this.name=name;
        this.image_path=image_path;
    }


    public Comment() {
    }

    public int getComment_id() {
        return comment_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public String getContent() {
        return content;
    }

    public String getDate(){
        return date;
    }


    public String getUser_name(){ return name;}

    public String getImage_path(){ return image_path;}

}
