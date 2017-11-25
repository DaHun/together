package test.project.together.model;

/**
 * Created by jeongdahun on 2017. 9. 8..
 */

public class Comment {
    int comment_id;
    int post_id;
    String content;
    String date;

    public Comment(int comment_id, int post_id, String content, String date) {
        this.comment_id = comment_id;
        this.post_id = post_id;
        this.content = content;
        this.date = date;
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

}
