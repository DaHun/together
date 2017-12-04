package test.project.together.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import test.project.together.R;

public class PostingViewHolder extends RecyclerView.ViewHolder {
    public ImageView postingImage;
    public TextView snstext;
    public TextView snsdate;
    public Button snslike;
    public TextView userName;
    public TextView snslikecount;
    public Button snscomment;
    //public Button plusposting;

    public PostingViewHolder(View itemView) {
        super(itemView);
        userName = (TextView)itemView.findViewById(R.id.userName);
        postingImage=(ImageView)itemView.findViewById(R.id.imageView);
        snstext = (TextView)itemView.findViewById(R.id.snstext);
        snsdate = (TextView)itemView.findViewById(R.id.snsdate);
        snslike = (Button) itemView.findViewById(R.id.snslike);
       // snslikecount = (TextView) itemView.findViewById(R.id.likecount);
        snscomment = (Button) itemView.findViewById(R.id.snscomment);
    //    plusposting = (Button) itemView.findViewById(R.id.plusposting);
    }
}
