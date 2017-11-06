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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.project.together.R;
import test.project.together.adapter.ViewPagerAdapter;
import test.project.together.model.ChangeEvent;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class SelectFragment extends Fragment
{

    @BindView(R.id.takeBtn) Button takeBtn;
    @BindView(R.id.giveBtn) Button giveBtn;
  //  @BindView(R.id.nextBtn) Button nextBtn;
    AlertDialog.Builder ad;
    LinearLayout layout;
    public static final String TAG = "Test_Alert_Dialog";

    public SelectFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_select, container, false);
//tests
        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }

    public void initSetting() {
        ad = new AlertDialog.Builder(getContext());
        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagerAdapter.mode=1;
                EventBus.getDefault().post(new ChangeEvent());
            }
        });

        giveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.setTitle("Please agree.");       // 제목 설정
                ad.setMessage("From now on, it will record what location information you have checked. Do you agree?");   // 내용 설정

                // 확인 버튼 설정
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(TAG,"Yes Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            int permissionResult = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
                            /**
                             * 패키지는 안드로이드 어플리케이션의 아이디이다.
                             * 현재 어플리케이션이 ACCESS_FINE_LOCATION에 대해 거부되어있는지 확인한다.
                             * */
                            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                                /** * 사용자가 ACCESS_FINE_LOCATION 권한을 거부한 적이 있는지 확인한다.
                                 * 거부한적이 있으면 True를 리턴하고
                                 * 거부한적이 없으면 False를 리턴한다. */
                                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(getActivity().getApplication());
                                    dialog2.setTitle("Permission is required.") .setMessage("\"Location\" permission is required to use this function. Do you want to continue?") .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            /**
                                             * 새로운 인스턴스(onClickListener)를 생성했기 때문에
                                             * 버전체크를 다시 해준다.
                                             * */
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // ACCESS_FINE_LOCATION 권한을 Android OS에 요청한다.
                                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000); } } }) .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getActivity().getApplication(), "Canceled.", Toast.LENGTH_SHORT).show(); } }) .create() .show();
                                } // 최초로 권한을 요청할 때
                                else { // ACCESS_FINE_LOCATION 권한을 Android OS에 요청한다.
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
                                }
                            } // ACCESS_FINE_LOCATION 권한이 있을 때
                            else { // 즉시 실행
                                //지도화면으로 전환
                                ViewPagerAdapter.mode=2;
                                EventBus.getDefault().post(new ChangeEvent());
                                 }
                        } // 마시멜로우 미만의 버전일 때
                        else { // 즉시 실행
                            //지도화면으로 전환
                            ViewPagerAdapter.mode=2;
                            EventBus.getDefault().post(new ChangeEvent());
                        }
                    }
                });

                // 취소 버튼 설정
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(TAG,"No Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                // 창 띄우기
                ad.show();
            }
        });
/*
        *nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Posting());
            }
        });
        */
    }


}
