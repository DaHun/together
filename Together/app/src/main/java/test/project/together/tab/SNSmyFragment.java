package test.project.together.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.project.together.R;
import test.project.together.adapter.MyPostingRecyclerViewAdapter;
import test.project.together.adapter.PostingRecyclerViewAdapter;
import test.project.together.adapter.ViewPagerAdapter;
import test.project.together.application.ApplicationController;
import test.project.together.model.ChangeEvent;
import test.project.together.model.Posting;
import test.project.together.network.NetworkService;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class SNSmyFragment extends Fragment{

    @BindView(R.id.postingRecyclerView) RecyclerView postingRecyclerView;
    //@BindView(R.id.previousBtn) Button previousBtn;
    @BindView(R.id.plusposting) Button plusregi;
    @BindView(R.id.change_posting) Button myPosting;


    final String TAG="SNSFragment";
    LinearLayout layout;
    NetworkService service;

    ArrayList<Posting> postingList;
    MyPostingRecyclerViewAdapter mypostingRecyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;


    public SNSmyFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_my_sns, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }

    public void initSetting() {
        service= ApplicationController.getInstance().getNetworkService();
        Call<ArrayList<Posting>> load_myPosting=service.load_myPosting(ApplicationController.user_id);

        load_myPosting.enqueue(new Callback<ArrayList<Posting>>() {
            @Override
            public void onResponse(Call<ArrayList<Posting>> call, Response<ArrayList<Posting>> response) {
                if(response.isSuccessful()){
                    postingList=response.body();
                    Log.d(TAG,postingList.size()+" ");

                    //RecyclerView Setting
                    mypostingRecyclerViewAdapter=new MyPostingRecyclerViewAdapter(postingList);
                    postingRecyclerView.setAdapter(mypostingRecyclerViewAdapter);
                    linearLayoutManager=new LinearLayoutManager(getContext());
                    postingRecyclerView.setLayoutManager(linearLayoutManager);

                }else
                    Log.d(TAG,"fail1");

            }

            @Override
            public void onFailure(Call<ArrayList<Posting>> call, Throwable t) {
                Log.d(TAG,"fail2");

            }
        });





        //게시물 추가
        plusregi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPagerAdapter.SNSmode=1;
                EventBus.getDefault().post(new ChangeEvent());
            }
        });

        myPosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ViewPagerAdapter.SNSmode=0;
                    EventBus.getDefault().post(new ChangeEvent());
            }
        });
    }


}
