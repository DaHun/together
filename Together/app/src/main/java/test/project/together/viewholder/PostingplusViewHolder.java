package test.project.together.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import test.project.together.R;

public class PostingplusViewHolder extends RecyclerView.ViewHolder {

    public Button registerposting;

    public PostingplusViewHolder(View itemView) {
        super(itemView);

        registerposting = (Button) itemView.findViewById(R.id.registerposting);
    }
}
