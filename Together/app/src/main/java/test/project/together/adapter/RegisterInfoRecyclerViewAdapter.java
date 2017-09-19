package test.project.together.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import test.project.together.R;
import test.project.together.model.Matching;
import test.project.together.model.Posting;
import test.project.together.viewholder.PostingViewHolder;
import test.project.together.viewholder.RegisterInfoViewHolder;

/**
 * Created by jeongdahun on 2017. 6. 26..
 */

public class RegisterInfoRecyclerViewAdapter extends RecyclerView.Adapter<RegisterInfoViewHolder>{

    ArrayList<Matching> items;
    Context context;

    public RegisterInfoRecyclerViewAdapter(ArrayList<Matching> items){
        this.items=items;
    }

    @Override
    public RegisterInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.register_viewholder,parent,false);
        context=parent.getContext();

        return new RegisterInfoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RegisterInfoViewHolder holder, final int position) {
        Matching item=items.get(position);

        holder.date.setText(item.getDate());
        holder.startTime.setText(item.getStartDate());
        holder.finishTime.setText(item.getFinishDate());
        holder.wish.setText(item.getWish());

    }


    @Override
    public int getItemCount() {
        if(items != null)
            return items.size();
        else
            return 0;
    }
}
