package test.project.together.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import test.project.together.R;

public class RegisterInfoViewHolder extends RecyclerView.ViewHolder {
    public TextView date;
    public TextView startTime;
    public TextView finishTime;
    public TextView wish;


    public RegisterInfoViewHolder(View itemView) {
        super(itemView);
        date=(TextView)itemView.findViewById(R.id.date);
        startTime=(TextView) itemView.findViewById(R.id.startTime);
        finishTime=(TextView) itemView.findViewById(R.id.finishTime);
        wish=(TextView) itemView.findViewById(R.id.wish);


    }
}
