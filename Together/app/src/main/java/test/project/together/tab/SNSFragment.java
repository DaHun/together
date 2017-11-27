package test.project.together.tab;

import android.content.SharedPreferences;
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
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.project.together.R;
import test.project.together.adapter.PostingRecyclerViewAdapter;
import test.project.together.adapter.ViewPagerAdapter;
import test.project.together.application.ApplicationController;
import test.project.together.model.ChangeEvent;
import test.project.together.model.Posting;
import test.project.together.network.NetworkService;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class SNSFragment extends Fragment{

    @BindView(R.id.postingRecyclerView) RecyclerView postingRecyclerView;
    //@BindView(R.id.previousBtn) Button previousBtn;
    @BindView(R.id.plusposting) Button plusregi;
    @BindView(R.id.change_my_posting) Button myPosting;


    final String TAG="SNSFragment";
    LinearLayout layout;
    NetworkService service;

    ArrayList<Posting> postingList;
    PostingRecyclerViewAdapter postingRecyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;


    public SNSFragment() {
        super();
    }

    @Override
    public void onResume() {
        super.onResume();
        initSetting();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_sns, container, false);

        ButterKnife.bind(this, layout);

        return layout;
    }

    public void initSetting() {
        service= ApplicationController.getInstance().getNetworkService();

        postingList=new ArrayList<Posting>();

        Call<ArrayList<Posting>> getAllSNS=service.getAllSNS();
        getAllSNS.enqueue(new Callback<ArrayList<Posting>>() {
            @Override
            public void onResponse(Call<ArrayList<Posting>> call, Response<ArrayList<Posting>> response) {
                if (response.isSuccessful()) {
                    postingList=response.body();
                    //Posting RecyclerView Setting
                    postingRecyclerViewAdapter=new PostingRecyclerViewAdapter(postingList);
                    postingRecyclerView.setAdapter(postingRecyclerViewAdapter);
                    linearLayoutManager=new LinearLayoutManager(getContext());
                    postingRecyclerView.setLayoutManager(linearLayoutManager);
                }else
                    Log.d(TAG,"fail1");
            }

            @Override
            public void onFailure(Call<ArrayList<Posting>> call, Throwable t) {

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
                SharedPreferences checkStatus = getActivity().getSharedPreferences("checkStatusInfo", 0);
                String str = checkStatus.getString("flagStatus", ""); // 키값으로 꺼냄
                if (!str.equals("checked")) {
                    myPosting.setText("ALL");

                    SharedPreferences checkStatus1 = getActivity().getSharedPreferences("checkStatusInfo", 0);
                    final SharedPreferences.Editor editor = checkStatus1.edit();//저장하려면 editor가 필요
                    editor.putString("flagStatus", "checked"); // 입력
                    editor.commit(); // 파일에 최종 반영함
                    ViewPagerAdapter.SNSmode=2;
                    EventBus.getDefault().post(new ChangeEvent());

                }else{
                    myPosting.setText("MY");

                    SharedPreferences checkStatus1 = getActivity().getSharedPreferences("checkStatusInfo", 0);
                    final SharedPreferences.Editor editor = checkStatus1.edit();//저장하려면 editor가 필요
                    editor.putString("flagStatus", "noCheck"); // 입력
                    editor.commit(); // 파일에 최종 반영함

                    ViewPagerAdapter.SNSmode=0;
                    EventBus.getDefault().post(new ChangeEvent());
                }



            }
        });


    }


}
