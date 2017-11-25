package test.project.together.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import test.project.together.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public ImageView postingImage;
    public TextView userId;
    public TextView content;


    public CommentViewHolder(View itemView) {
        super(itemView);
        userId=(TextView) itemView.findViewById(R.id.userId);
        content = (TextView)itemView.findViewById(R.id.snstext);
    }
}
