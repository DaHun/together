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
import test.project.together.model.Posting;
import test.project.together.viewholder.PostingViewHolder;

/**
 * Created by jeongdahun on 2017. 6. 26..
 */

public class PostingRecyclerViewAdapter extends RecyclerView.Adapter<PostingViewHolder>{

    ArrayList<Posting> items;
    Context context;

    public PostingRecyclerViewAdapter(ArrayList<Posting> items){
        this.items=items;
    }

    @Override
    public PostingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_posting,parent,false);
        context=parent.getContext();
        Log.d("Posting",items.size()+"");
        return new PostingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostingViewHolder holder, final int position) {
        final Posting item=items.get(position);

        Glide.with(context).load(items.get(position).getImage_path()).into(holder.postingImage);
        holder.snstext.setText(item.getContent());
        holder.snsdate.setText(item.getDate());
        holder.snslike.setText(String.valueOf(item.getLike_count()));

    }

    @Override
    public int getItemCount() {
        if(items != null)
            return items.size();
        else
            return 0;
    }
}
