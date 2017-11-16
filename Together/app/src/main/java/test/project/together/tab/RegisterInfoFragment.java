package test.project.together.tab;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.project.together.R;
import test.project.together.application.ApplicationController;
import test.project.together.model.Matching;
import test.project.together.model.User;
import test.project.together.network.NetworkService;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class RegisterInfoFragment extends Fragment {

   // @BindView(R.id.nextBtn) Button nextBtn;

    @BindView(R.id.locationText) TextView locationText;
    @BindView(R.id.wantText) TextView wantText;
    @BindView(R.id.dateText) TextView dateText;

    @BindView(R.id.startTimeText) TextView startTimeText;
    @BindView(R.id.finishTimeText) TextView finishTimeText;

    @BindView(R.id.volunteerName) TextView volunteerNameText;
    @BindView(R.id.volunteerAge) TextView volunteerAgeText;
    @BindView(R.id.volunteerPhone) TextView volunteerPhoneText;

    @BindView(R.id.callButton) Button callButton;


    LinearLayout layout;
    NetworkService service;
    final static String TAG="RegisterInfoFragment";

    public static int matching_id;
    public RegisterInfoFragment() {
        super();
    }

    String phoneNumber = "tel:";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_registerinfo, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }

    public void initSetting() {

      /*  nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Posting());
            }
        });
*/
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
                    if(response.body().isCheck()==1){
                        matchingInfo();
                    }else{
                       callButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Matching> call, Throwable t) {

            }
        });
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
 //               startActivity(in);
                    /**
                     * 사용자 단말기의 권한 중 "전화걸기" 권한이 허용되어 있는지 확인한다.
                     * Android는 C언어 기반으로 만들어졌기 때문에 Boolean 타입보다 Int 타입을 사용한다.
                     * */
                    int permissionResult = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
                    /**
                     * 패키지는 안드로이드 어플리케이션의 아이디이다.
                     * 현재 어플리케이션이 CALL_PHONE에 대해 거부되어있는지 확인한다.
                     * */
                    if (permissionResult == PackageManager.PERMISSION_DENIED) {
                        /** * 사용자가 CALL_PHONE 권한을 거부한 적이 있는지 확인한다.
                         * 거부한적이 있으면 True를 리턴하고
                         * 거부한적이 없으면 False를 리턴한다. */
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity().getApplication());
                            dialog.setTitle("Permission is required.") .setMessage("\"CALL\" permission is required to use this function. Do you want to continue?") .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    /**
                                     * 새로운 인스턴스(onClickListener)를 생성했기 때문에
                                     * 버전체크를 다시 해준다.
                                     * */
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // CALL_PHONE 권한을 Android OS에 요청한다.
                                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000); } } }) .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity().getApplication(), "Canceled.", Toast.LENGTH_SHORT).show(); } }) .create() .show();
                        } // 최초로 권한을 요청할 때
                        else { // CALL_PHONE 권한을 Android OS에 요청한다.
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);

                        }
                    } // CALL_PHONE의 권한이 있을 때
                    else { // 즉시 실행
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
                        startActivity(intent); }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) { // 요청한 권한을 사용자가 "허용" 했다면...
            //if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
                // Add Check Permission
              //  if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
               // }
            } else {
                Toast.makeText(getContext(), "Canceled.", Toast.LENGTH_SHORT).show();
                callButton.setVisibility(View.GONE);
            }
        //}
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
                    phoneNumber += response.body().phone;
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
