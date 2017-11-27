package test.project.together.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import test.project.together.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public TextView userName;
    public TextView content;
    public TextView date;
    public Button readbtn;

    public CommentViewHolder(View itemView) {
        super(itemView);
        userName=(TextView) itemView.findViewById(R.id.userName);
        content = (TextView)itemView.findViewById(R.id.content);
        date = (TextView)itemView.findViewById(R.id.date);
        readbtn = (Button)itemView.findViewById(R.id.sound);
    }
}