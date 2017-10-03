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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.project.together.R;
import test.project.together.application.ApplicationController;
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

    NetworkService service;
    final String TAG="MatchingFragment";

    LinearLayout layout;

    GoogleApiClient mGoogleApiClient = null;
    FragmentManager fm;
    MapView mapView;
    GoogleMap map;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    Bitmap interested_bitmap;

    ArrayList<Matching> seniorArrayList=null;
    ArrayList<Marker> markerArrayList=null;


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
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_volunteer, container, false);

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

                    markerArrayList=new ArrayList<Marker>();
                    seniorArrayList=new ArrayList<Matching>();

                    seniorArrayList=response.body();
                    //검색한 곳 마커 설정 및 옵션
                    MarkerOptions options = new MarkerOptions();
                    options.icon(BitmapDescriptorFactory.fromBitmap(interested_bitmap));

                    for(int i=0;i<seniorArrayList.size();i++){
                        options.position(new LatLng(seniorArrayList.get(i).getLatitude(), seniorArrayList.get(i).getLongitude()));
                        Marker marker=map.addMarker(options);
                        markerArrayList.add(marker);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Matching>> call, Throwable t) {

            }
        });

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
