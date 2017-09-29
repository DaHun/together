package test.project.together.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import test.project.together.R;

public class PostingViewHolder extends RecyclerView.ViewHolder {
    public ImageView postingImage;

    public PostingViewHolder(View itemView) {
        super(itemView);
        postingImage=(ImageView)itemView.findViewById(R.id.postingImage);

    }
}
