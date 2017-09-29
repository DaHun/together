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


    LinearLayout layout;
    NetworkService service;
    final static String TAG="RegisterFragment";


    public static Matching registerInfo;

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

        locationText.setText(registerInfo.getLocation());
        wantText.setText(registerInfo.getWish());
        dateText.setText(registerInfo.getDate());
        startTimeText.setText(registerInfo.getStartTime());
        finishTimeText.setText(registerInfo.getFinishTime());

    }



}
