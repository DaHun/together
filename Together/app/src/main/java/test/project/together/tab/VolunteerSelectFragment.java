package test.project.together.tab;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class VolunteerSelectFragment extends Fragment {

   // @BindView(R.id.nextBtn) Button nextBtn;
    @BindView(R.id.volselectBtn) Button selectBtn;
    @BindView(R.id.volcheckBtn) Button checkBtn;

    ///////
//
    final static String TAG="SeniorFragment";
    LinearLayout layout;

    AlertDialog.Builder ad;


    public VolunteerSelectFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_volunteer_select, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }



    public void initSetting() {
        ad = new AlertDialog.Builder(getContext());

        //Button
        /*
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Posting());
            }
        });
*/
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences check = getActivity().getSharedPreferences("checkInfo", 0);
                String str = check.getString("flag", ""); // 키값으로 꺼냄
                if (!str.equals("checked")) {
                    Log.v(TAG, "checked~~~~~~!!!!!!!!!!!!!!!!!!!!!!!!!");
                    ad.setTitle("Please agree.");       // 제목 설정
                    ad.setMessage("From now on, it will record what location information you have checked. Do you agree?");   // 내용 설정

                    // 확인 버튼 설정
                    ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.v(TAG, "Yes Btn Click");

                            SharedPreferences check = getActivity().getSharedPreferences("checkInfo", 0);
                            final SharedPreferences.Editor editor = check.edit();//저장하려면 editor가 필요
                            editor.putString("flag", "checked"); // 입력
                            editor.commit(); // 파일에 최종 반영함

                            dialog.dismiss();     //닫기


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
                                        Toast.makeText(getActivity().getApplication(), "Location permissions are required. Please change your permission settings.", Toast.LENGTH_LONG).show();
                                    } // 최초로 권한을 요청할 때
                                    else { // ACCESS_FINE_LOCATION 권한을 Android OS에 요청한다.
                                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
                                    }
                                } // ACCESS_FINE_LOCATION 권한이 있을 때
                                else { // 즉시 실행
                                    //VolunteerFragment로 이동
                                    ViewPagerAdapter.volsubMode = 1;
                                    EventBus.getDefault().post(new ChangeEvent());
                                }
                            } // 마시멜로우 미만의 버전일 때
                            else { // 즉시 실행
                                //VolunteerFragment로 이동
                                ViewPagerAdapter.volsubMode = 1;
                                EventBus.getDefault().post(new ChangeEvent());
                            }
                        }
                    });
                    // 취소 버튼 설정
                    ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.v(TAG, "No Btn Click");
                            dialog.dismiss();     //닫기
                            // Event
                        }
                    });
                    // 창 띄우기
                    ad.show();
                } else {
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
                            Toast.makeText(getActivity().getApplication(), "Location permissions are required. Please change your permission settings.", Toast.LENGTH_LONG).show();
                        } // 최초로 권한을 요청할 때
                        else { // ACCESS_FINE_LOCATION 권한을 Android OS에 요청한다.
                            Log.d(TAG, "0001-2");
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
                        }
                    } // ACCESS_FINE_LOCATION 권한이 있을 때
                    else { // 즉시 실행
                        //지도화면으로 전환
                        Log.d(TAG, "0002");
                        ViewPagerAdapter.volsubMode = 1;
                        EventBus.getDefault().post(new ChangeEvent());
                    }
                }
            }
        });
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //매칭된 정보 보여주는 페이지로 이동
                ViewPagerAdapter.volsubMode=2;
                EventBus.getDefault().post(new ChangeEvent());
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            try {
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getContext(),"LOCATION permission athorized",Toast.LENGTH_SHORT).show();
                            ViewPagerAdapter.volsubMode = 1;
                            EventBus.getDefault().post(new ChangeEvent());
                        } else {
                            Toast.makeText(getContext(),"LOCATIN permission denied",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }catch (Exception e){
                Log.d(TAG, "fail~~~~~~~~~~~~~~~");
            }
        }
    }
}
