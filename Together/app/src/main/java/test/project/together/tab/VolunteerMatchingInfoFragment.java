package test.project.together.tab;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import test.project.together.adapter.RegisterInfoRecyclerViewAdapter;
import test.project.together.adapter.ViewPagerAdapter;
import test.project.together.application.ApplicationController;
import test.project.together.model.ChangeEvent;
import test.project.together.model.Matching;
import test.project.together.network.NetworkService;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class VolunteerMatchingInfoFragment extends Fragment {
//봉사자가 매칭신청한 봉사내역을 보여주는 리스트
//sdfs
    @BindView(R.id.volmatching)
    RecyclerView matchingInfoRecyclerView;

    NetworkService service;
    final String TAG="CheckFragment";
    LinearLayout layout;

    ArrayList<Matching> matchingList;
    RegisterInfoRecyclerViewAdapter registerInfoRecyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;

    public VolunteerMatchingInfoFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_matchinginfo_volunteer, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }

    public void initSetting() {


        service= ApplicationController.getInstance().getNetworkService();

        Call<ArrayList<Matching>> load_AllRegisterInfo=service.load_allRegisterInfo(ApplicationController.user_id);
        load_AllRegisterInfo.enqueue(new Callback<ArrayList<Matching>>() {
            @Override
            public void onResponse(Call<ArrayList<Matching>> call, Response<ArrayList<Matching>> response) {
                if(response.isSuccessful()){
                    matchingList=response.body();
                    Log.d(TAG,matchingList.size()+" ");

                    //RecyclerView Setting
                    registerInfoRecyclerViewAdapter=new RegisterInfoRecyclerViewAdapter(matchingList);
                    matchingInfoRecyclerView.setAdapter(registerInfoRecyclerViewAdapter);
                    linearLayoutManager=new LinearLayoutManager(getContext());
                    matchingInfoRecyclerView.setLayoutManager(linearLayoutManager);

                }else
                    Log.d(TAG,"fail1");

            }
//
            @Override
            public void onFailure(Call<ArrayList<Matching>> call, Throwable t) {
                Log.d(TAG,"fail2");

            }
        });



    }




}
