package test.project.together.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import test.project.together.R;
import test.project.together.model.Comment;
import test.project.together.viewholder.CommentViewHolder;

/**
 * Created by jeongdahun on 2017. 6. 26..
 */

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentViewHolder>{

    ArrayList<Comment> items;
    Context context;

    public CommentRecyclerViewAdapter(ArrayList<Comment> items){
        this.items=items;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_comment,parent,false);
        context=parent.getContext();
        Log.d("Comment",items.size()+"");
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, final int position) {
        final Comment item=items.get(position);

        holder.userId.setText(item.getDate());
        holder.content.setText(item.getContent());


    }

    @Override
    public int getItemCount() {
        if(items != null)
            return items.size();
        else
            return 0;
    }
}
