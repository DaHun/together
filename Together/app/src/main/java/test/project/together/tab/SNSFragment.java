package test.project.together.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.project.together.R;
import test.project.together.adapter.PostingRecyclerViewAdapter;
import test.project.together.adapter.ViewPagerAdapter;
import test.project.together.model.ChangeEvent;
import test.project.together.model.Matching;
import test.project.together.model.Posting;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class SNSFragment extends Fragment{

    @BindView(R.id.postingRecyclerView) RecyclerView postingRecyclerView;
    //@BindView(R.id.previousBtn) Button previousBtn;
    @BindView(R.id.plusposting) Button plusregi;


    final String TAG="SNSFragment";
    LinearLayout layout;

    ArrayList<Posting> postingList;
    PostingRecyclerViewAdapter postingRecyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;

    public SNSFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_sns, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }

    public void initSetting() {
        postingList=new ArrayList<Posting>();

        Posting posting=new Posting();
        posting.image_path="http://www.google.co.kr/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png";
        //posting.getDate();
        //posting.getContent();

        postingList.add(posting);
        postingList.add(posting);
        postingList.add(posting);
        postingList.add(posting);


        //Posting RecyclerView Setting
        postingRecyclerViewAdapter=new PostingRecyclerViewAdapter(postingList);
        postingRecyclerView.setAdapter(postingRecyclerViewAdapter);
        linearLayoutManager=new LinearLayoutManager(getContext());
        postingRecyclerView.setLayoutManager(linearLayoutManager);

        //게시물 추가
        plusregi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPagerAdapter.SNSmode=1;
                EventBus.getDefault().post(new ChangeEvent());
            }
        });


/*
        //이전 버튼 클릭 리스너
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Matching());
            }
        });


*/
    }


}
