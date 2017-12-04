package test.project.together.tab;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.project.together.R;
import test.project.together.adapter.ViewPagerAdapter;
import test.project.together.application.ApplicationController;
import test.project.together.model.ChangeEvent;
import test.project.together.model.InfoLayoutEvent;
import test.project.together.model.Matching;
import test.project.together.model.Posting;
import test.project.together.network.NetworkService;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class VolunteerFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener{

    @BindView(R.id.infoLayout) LinearLayout infoLayout;
    @BindView(R.id.locationText) TextView locationText;
    @BindView(R.id.wantText) TextView wantText;
    @BindView(R.id.dateText) TextView dateText;
    @BindView(R.id.startTimeText) TextView startTimeText;
    @BindView(R.id.finishTimeText) TextView finishTimeText;
    @BindView(R.id.matchBtn) Button matchBtn;

    NetworkService service;
    final String TAG="VolunteerFragment";
    RelativeLayout layout;

    //Google Setting
    GoogleApiClient mGoogleApiClient = null;
    FragmentManager fm;
    MapView mapView;
    GoogleMap map;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    //마커 윈도우
    public static boolean infoLayoutMode=false;
    View marker_window_view;
    TextView window_dateText; //마커 윈도우에 있는 date
    TextView window_typeText; //마커 윈도우에 있는 type
    //근처 봉사리스트들 마커 이미지
    Bitmap interested_bitmap;
    Matching interested_volunteer;

    ArrayList<Matching> seniorArrayList=null;
    ArrayList<Marker> markerArrayList=null;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInfoLayoutEvent(InfoLayoutEvent infoLayoutEvent){ infoLayout.setVisibility(View.GONE);}

    public VolunteerFragment() {
        super();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fm = getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_volunteer, container, false);

        ButterKnife.bind(this, layout);
        service=ApplicationController.getInstance().getNetworkService();

        initSetting(savedInstanceState);

        return layout;
    }

    public void initSetting(Bundle savedInstanceState) {

        //googleApiClient  통합 GoogleAPI!
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();

        //GoogleMap Setting
        mapView = (MapView) layout.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        //커스텀 마커
        View interested_marker_view = LayoutInflater.from(getContext()).inflate(R.layout.marker_interest, null);
        interested_bitmap=createDrawableFromView(getContext(), interested_marker_view);
        //마커 윈도우
        marker_window_view = LayoutInflater.from(getContext()).inflate(R.layout.window_marker, null);
        window_dateText = (TextView) marker_window_view.findViewById(R.id.dateText);
        window_typeText = (TextView) marker_window_view.findViewById(R.id.typeText);


        matchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<Void> matching=service.matching(interested_volunteer.getMatching_id(), ApplicationController.user_id);
                matching.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG,"success");
                            infoLayoutMode=false;
                            infoLayout.setVisibility(View.GONE);

                            VolMatchingInfoFragment.matching_id=interested_volunteer.getMatching_id();
                            ViewPagerAdapter.volsubMode=3;
                            EventBus.getDefault().post(new ChangeEvent());
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
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        //맵 타입
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // 퍼미션 체크 추가
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);

        // 구글맵의 UI 환경 가져오기
        UiSettings uiSettings = map.getUiSettings();
        // 줌기능 설정
        uiSettings.setZoomControlsEnabled(true);


        //맵 클릭
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                infoLayoutMode=false;
                infoLayout.setVisibility(View.GONE);
            }
        });

        //마커 윈도우 세팅
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {return null;}

            @Override
            public View getInfoContents(Marker marker) {
                if(!marker.getTitle().equals("interest")) return null;

                interested_volunteer=searchInfo(marker);
                if(interested_volunteer == null) return null;

                window_dateText.setText(interested_volunteer.getDate());
                window_typeText.setText(interested_volunteer.getWish());

                return marker_window_view;
            }
        });

        //마커 윈도우 클릭
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                if(!marker.getTitle().equals("interest")) return;

                interested_volunteer=searchInfo(marker);
                if(interested_volunteer == null) return;

                infoLayoutMode=true;
                infoLayout.setVisibility(View.VISIBLE);
                locationText.setText(interested_volunteer.getLocation());
                wantText.setText(interested_volunteer.getWish());
                dateText.setText(interested_volunteer.getDate());
                startTimeText.setText(interested_volunteer.getStartTime());
                finishTimeText.setText(interested_volunteer.getFinishTime());
            }
        });
    }

    public Matching searchInfo(Marker marker){

        double lat=marker.getPosition().latitude;
        double log=marker.getPosition().longitude;

        for(int i=0;i<seniorArrayList.size();i++)
            if(seniorArrayList.get(i).getLatitude() == lat && seniorArrayList.get(i).getLongitude()==log)
                return seniorArrayList.get(i);

        return null;
    }


    // View를 Bitmap으로 변환
    private Bitmap createDrawableFromView(Context context, View view) {


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public void mark_And_Loadlist(LatLng current_Loc){

        if(markerArrayList != null){
            for(int i=0;i<markerArrayList.size();i++)
                markerArrayList.get(i).remove();

            markerArrayList=null;
        }



        //봉사자:현재 위치에서 반경 nkm 있는것만 봉사등록 리스트 받아오기
        Call<ArrayList<Matching>> load_nearMyLocation=service.load_nearMyLocation(current_Loc.latitude, current_Loc.longitude);

        load_nearMyLocation.enqueue(new Callback<ArrayList<Matching>>() {
            @Override
            public void onResponse(Call<ArrayList<Matching>> call, Response<ArrayList<Matching>> response) {
                if(response.isSuccessful()){

                    Log.d(TAG, "SUCCESS");
                    markerArrayList=new ArrayList<Marker>();
                    seniorArrayList=new ArrayList<Matching>();

                    seniorArrayList=response.body();
                    //검색한 곳 마커 설정 및 옵션
                    MarkerOptions options = new MarkerOptions();
                    options.icon(BitmapDescriptorFactory.fromBitmap(interested_bitmap));

                    for(int i=0;i<seniorArrayList.size();i++){
                        options.position(new LatLng(seniorArrayList.get(i).getLatitude(), seniorArrayList.get(i).getLongitude()));
                        options.title("interest");
                        Marker marker=map.addMarker(options);
                        markerArrayList.add(marker);
                    }
                }else{
                    Log.d(TAG, "fail1");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Matching>> call, Throwable t) {
                Log.d(TAG, "fail2");

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    //mapView
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        mapView.onDestroy();

        if (mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

        super.onDestroyView();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    //GoogleApiClient 연결 되었을때
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // 퍼미션 체크 추가
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //마지막 위치
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        updateMap(mLastLocation);

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        updateMap(location);
    }

    //location변수 위치로 맵 바꿈
    public void updateMap(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        final LatLng Loc = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Loc, 16));

        mark_And_Loadlist(Loc);
    }


}
