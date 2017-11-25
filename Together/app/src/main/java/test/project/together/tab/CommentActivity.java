package test.project.together.tab;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
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


    }
}
