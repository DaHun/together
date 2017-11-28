package test.project.together.tab;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
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
import test.project.together.model.ChangeEvent;
import test.project.together.model.Matching;
import test.project.together.network.NetworkService;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class RegisterFragment extends Fragment
        implements LocationListener,
        GoogleApiClient.ConnectionCallbacks
{

//    @BindView(R.id.nextBtn) Button nextBtn;

    @BindView(R.id.locationText) TextView locationText;
    @BindView(R.id.yearSpinner) Spinner yearSpinner;
    @BindView(R.id.monthSpinner) Spinner monthSpinner;
    @BindView(R.id.dateSpinner) Spinner dateSpinner;
    @BindView(R.id.start_hourSpinner) Spinner start_hourSpinner;
    @BindView(R.id.start_minuteSpinner) Spinner start_minuteSpinner;
    @BindView(R.id.finish_hourSpinner) Spinner finish_hourSpinner;
    @BindView(R.id.finish_minuteSpinner) Spinner finish_minuteSpinner;
    @BindView(R.id.wantSpinner) Spinner wantSpinner;

    @BindView(R.id.completeBtn) Button completeBtn;

    LinearLayout layout;
    NetworkService service;
    final static String TAG="RegisterFragment";

    GoogleApiClient mGoogleApiClient = null;
    LocationRequest mLocationRequest;
    Geocoder gc;
    String currentLocation;
    double currentLatitude;
    double currentLongitude;


    String mYear;
    String mMonth;
    String mDay;
    String mStartHour;
    String mStartMinute;
    String mFinishHour;
    String mFinishMinute;


    public RegisterFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_register, container, false);

        ButterKnife.bind(this, layout);

        initSetting();
        spinnerSetting();


        return layout;
    }

    public void initSetting() {

        service= ApplicationController.getInstance().getNetworkService();

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,mYear+"-"+mMonth+"-"+mDay+" "+mStartHour+":"+mStartMinute+":00");
                Log.d(TAG,mYear+"-"+mMonth+"-"+mDay+" "+mFinishHour+":"+mFinishMinute+":00");

                Matching matching=new Matching(
                        ApplicationController.user_id,
                        currentLocation,
                        currentLatitude,
                        currentLongitude,
                        String.valueOf(wantSpinner.getSelectedItem()),
                        mYear+"-"+mMonth+"-"+mDay,
                        mStartHour+" : "+mStartMinute,
                        mFinishHour+" : "+mFinishMinute,0);

                Call<Void> register=service.register(matching);
                register.enqueue(new Callback<Void>() {
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

                ViewPagerAdapter.subMode=0;
                EventBus.getDefault().post(new ChangeEvent());
            }
        });


        //googleApiClient  통합 GoogleAPI!
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    //.addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();

        //Geocoder Setting
        gc = new Geocoder(getContext(), Locale.KOREAN);

    }



    public void spinnerSetting(){

        final ArrayList<String> hourList = new ArrayList<String>();
        hourList.add("09"); hourList.add("10"); hourList.add("11");
        hourList.add("12"); hourList.add("13"); hourList.add("14");
        hourList.add("15"); hourList.add("16"); hourList.add("17");
        hourList.add("18"); hourList.add("19"); hourList.add("20");

        final ArrayList<String> yearList = new ArrayList<String>();
        yearList.add("2017"); yearList.add("2018");

        final ArrayList<String> monthList = new ArrayList<String>();
        monthList.add("01"); monthList.add("02"); monthList.add("03");
        monthList.add("04"); monthList.add("05"); monthList.add("06");
        monthList.add("07"); monthList.add("08"); monthList.add("09");
        monthList.add("10"); monthList.add("11"); monthList.add("12");

        final ArrayList<String> dateList = new ArrayList<String>();
        dateList.add("01"); dateList.add("02"); dateList.add("03"); dateList.add("04"); dateList.add("05"); dateList.add("06");
        dateList.add("07"); dateList.add("08"); dateList.add("09"); dateList.add("10"); dateList.add("11"); dateList.add("12");
        dateList.add("13"); dateList.add("14"); dateList.add("15"); dateList.add("16"); dateList.add("17"); dateList.add("18");
        dateList.add("19"); dateList.add("20"); dateList.add("21"); dateList.add("22"); dateList.add("23"); dateList.add("24");
        dateList.add("25"); dateList.add("26"); dateList.add("27"); dateList.add("28"); dateList.add("29"); dateList.add("30"); dateList.add("31");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_date, hourList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        start_hourSpinner.setAdapter(dataAdapter);
        finish_hourSpinner.setAdapter(dataAdapter);

        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_login_activity, yearList);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(dataAdapter4);

        ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_login_activity, monthList);
        dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(dataAdapter5);

        ArrayAdapter<String> dataAdapter6 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_login_activity, dateList);
        dataAdapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dataAdapter6);


        final ArrayList<String> minuteList = new ArrayList<String>();
        minuteList.add("00"); minuteList.add("10"); minuteList.add("20");
        minuteList.add("30"); minuteList.add("40"); minuteList.add("50");

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), R.layout.spinner_date, minuteList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        start_minuteSpinner.setAdapter(dataAdapter2);
        finish_minuteSpinner.setAdapter(dataAdapter2);

        ////
        start_hourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mStartHour=hourList.get(position);
                Log.d(TAG,mStartHour);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        start_minuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mStartMinute=minuteList.get(position);
                Log.d(TAG,mStartMinute);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        finish_hourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFinishHour=hourList.get(position);
                Log.d(TAG,mFinishHour);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        finish_minuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFinishMinute=minuteList.get(position);
                Log.d(TAG, mFinishMinute);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mYear=yearList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMonth=monthList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDay=dateList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ////
        ArrayList<String> wantList = new ArrayList<String>();
        wantList.add("Walk"); wantList.add("Talk"); wantList.add("Meal");wantList.add("etc");

        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_login_activity, wantList);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wantSpinner.setAdapter(dataAdapter3);

    }




    public String searchPlace(Location location){

        try {

            List<Address> addr = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 5);

            if(addr!=null){
                return addr.get(4).getFeatureName()+" "
                        +addr.get(3).getFeatureName()+" "
                        +addr.get(2).getFeatureName()+" "
                        +addr.get(1).getFeatureName()+" "
                        +addr.get(0).getFeatureName()+" ";

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // 퍼미션 체크 추가
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //주기적으로 위치 체크하기
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(1000)
                .setSmallestDisplacement(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation=searchPlace(location);
        locationText.setText(currentLocation);

        Log.d(TAG,"위치: "+currentLocation);

        currentLatitude=location.getLatitude();
        currentLongitude=location.getLongitude();

        if(mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }


}
