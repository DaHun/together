package test.project.together.tab;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.project.together.R;
import test.project.together.adapter.CommentRecyclerViewAdapter;
import test.project.together.application.ApplicationController;
import test.project.together.model.Comment;
import test.project.together.network.NetworkService;

/**
 * Created by Eunju on 2017-11-24.
 */

public class CommentActivity extends Activity {

    @BindView(R.id.commentRecyclerview)
    RecyclerView commentRecyclerView;

    public static int post_id;

    NetworkService service;
    final String TAG="ActivityComment";
    LinearLayout layout;
    ArrayList<Comment> commentList;
    CommentRecyclerViewAdapter commentRecyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.activity_comment, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }

    public void initSetting() {
        service= ApplicationController.getInstance().getNetworkService();
        Call<ArrayList<Comment>> load_comment=service.load_comment(post_id);
        load_comment.enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                if(response.isSuccessful()){
                    commentList=response.body();
                    Log.d(TAG,commentList.size()+" ");

                    //RecyclerView Setting
                    commentRecyclerViewAdapter=new CommentRecyclerViewAdapter(commentList);
                    commentRecyclerView.setAdapter(commentRecyclerViewAdapter);
                    linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                    commentRecyclerView.setLayoutManager(linearLayoutManager);

                }else
                    Log.d(TAG,"fail1");

            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {
                Log.d(TAG,"fail2");

            }
        });

    }
}
