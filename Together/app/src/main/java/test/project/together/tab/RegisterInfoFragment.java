package test.project.together.tab;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.project.together.R;
import test.project.together.adapter.ViewPagerAdapter;
import test.project.together.application.ApplicationController;
import test.project.together.model.Change;
import test.project.together.model.Matching;
import test.project.together.model.User;
import test.project.together.network.NetworkService;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class RegisterInfoFragment extends Fragment {

    @BindView(R.id.nextBtn) Button nextBtn;

    @BindView(R.id.locationText) TextView locationText;
    @BindView(R.id.wantText) TextView wantText;
    @BindView(R.id.dateText) TextView dateText;

    @BindView(R.id.startTimeText) TextView startTimeText;
    @BindView(R.id.finishTimeText) TextView finishTimeText;

    @BindView(R.id.volunteerName) TextView volunteerNameText;
    @BindView(R.id.volunteerAge) TextView volunteerAgeText;
    @BindView(R.id.volunteerPhone) TextView volunteerPhoneText;


    LinearLayout layout;
    NetworkService service;
    final static String TAG="RegisterInfoFragment";

    public static int matching_id;
    public RegisterInfoFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_registerinfo, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }

    public void initSetting() {

        service= ApplicationController.getInstance().getNetworkService();

        Call<Matching> load_oneRegisterInfo=service.load_oneRegisterInfo(matching_id);
        load_oneRegisterInfo.enqueue(new Callback<Matching>() {
            @Override
            public void onResponse(Call<Matching> call, Response<Matching> response) {
                if(response.isSuccessful()){
                    locationText.setText(response.body().getLocation());
                    wantText.setText(response.body().getWish());
                    dateText.setText(response.body().getDate());
                    startTimeText.setText(response.body().getStartTime());
                    finishTimeText.setText(response.body().getFinishTime());

                    Log.d(TAG,response.body().isCheck()+"");
                    //matching 됬을경우에만 쿼리날림
                    if(response.body().isCheck()==1)
                        matchingInfo();
                }
            }

            @Override
            public void onFailure(Call<Matching> call, Throwable t) {

            }
        });





    }

    public void matchingInfo(){

        Call<User> load_matchinginfo=service.load_matchinginfo(matching_id);
        load_matchinginfo.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"success");
                    volunteerNameText.setText(response.body().name);
                    volunteerAgeText.setText(response.body().age);
                    volunteerPhoneText.setText(response.body().phone);
                 }
                else
                    Log.d(TAG,"fail1");
                }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG,"fail2");
            }
        });

    }



}
