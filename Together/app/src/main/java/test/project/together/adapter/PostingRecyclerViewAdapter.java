package test.project.together.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.project.together.R;
import test.project.together.model.Comment;
import test.project.together.model.Posting;
import test.project.together.network.NetworkService;
import test.project.together.tab.CommentActivity;
import test.project.together.viewholder.PostingViewHolder;

/**
 * Created by jeongdahun on 2017. 6. 26..
 */

public class PostingRecyclerViewAdapter extends RecyclerView.Adapter<PostingViewHolder>{

    ArrayList<Posting> items;
    Context context;
    NetworkService service;
    final static String TAG="PostingRecyclerVA";

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

        holder.snscomment.setOnClickListener(new View.OnClickListener() {   //COMMENT버튼 눌렀을 때
            @Override
            public void onClick(View v) {
                CommentActivity.post_id=item.getPost_id();
                EventBus.getDefault().post(new Comment());
            }
        });

        holder.snslike.setOnClickListener(new View.OnClickListener() {   //좋아요 버튼 눌렀을 때
            @Override
            public void onClick(View v) {
                Call<Void> increase_likeCount=service.increase_likeCount(item.getPost_id());
                increase_likeCount.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG,"success");
                        }else
                            Log.d(TAG,"fail1");

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(TAG,"fail2");

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        if(items != null)
            return items.size();
        else
            return 0;
    }
}
