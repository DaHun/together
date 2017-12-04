package test.project.together.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import test.project.together.R;

public class MyPostingViewHolder extends RecyclerView.ViewHolder {
    public ImageView postingImage;
    public TextView snstext;
    public TextView snsdate;
    public Button snslike;
    public TextView snslikecount;
    public Button snscomment;
    public Button sharebtn;
    public Button deletebtn;
    //public Button plusposting;

    public MyPostingViewHolder(View itemView) {
        super(itemView);
        postingImage=(ImageView)itemView.findViewById(R.id.imageView);
        snstext = (TextView)itemView.findViewById(R.id.snstext);
        snsdate = (TextView)itemView.findViewById(R.id.snsdate);
        snslike = (Button) itemView.findViewById(R.id.snslike);
       // snslikecount = (TextView) itemView.findViewById(R.id.likecount);
        snscomment = (Button) itemView.findViewById(R.id.snscomment);
    //    plusposting = (Button) itemView.findViewById(R.id.plusposting);
        sharebtn = (Button)itemView.findViewById(R.id.sharebtn);
        deletebtn = (Button)itemView.findViewById(R.id.deletebtn);
    }
}
