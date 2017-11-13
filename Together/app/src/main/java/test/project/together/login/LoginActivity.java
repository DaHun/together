package test.project.together.login;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.project.together.R;
import test.project.together.application.ApplicationController;
import test.project.together.gcm.QuickstartPreferences;
import test.project.together.gcm.RegistrationIntentService;
import test.project.together.main.MainActivity;
import test.project.together.model.User;
import test.project.together.network.NetworkService;

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";

    EditText nametxt;
    Spinner agespinner;

    RadioGroup rg;

    Button signinbtn;
    Button cancelbtn;
    TextView phoneText;

    String name;
    String phone;
    String token;
    int age;
    int gender;

    NetworkService service;

    ///
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getInstanceIdToken();
        registBroadcastReceiver();

        initSetting();
        init();
    }

    public void initSetting(){

        service= ApplicationController.getInstance().getNetworkService();

        signinbtn=(Button)findViewById(R.id.sign_in_btn);
        cancelbtn=(Button)findViewById(R.id.sign_in_cancel);
        nametxt=(EditText)findViewById(R.id.sign_in_name);
        agespinner=(Spinner)findViewById(R.id.sign_in_age);
        rg = (RadioGroup)findViewById(R.id.radiogroup);
        phoneText=(TextView)findViewById(R.id.sign_in_phone);

    }




    void init() {
        //나이
        final ArrayList<String> ageList = new ArrayList<String>();

        for (int i = 15; i <= 100; i++) {
            ageList.add(Integer.toString(i));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, ageList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        agespinner.setAdapter(dataAdapter);

        agespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                age = Integer.valueOf(ageList.get(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //전화번호 권한
        /**
         * 사용자 단말기의 권한 중 "PHONE STATE" 권한이 허용되어 있는지 확인한다.
         * Android는 C언어 기반으로 만들어졌기 때문에 Boolean 타입보다 Int 타입을 사용한다.
         * */
        int permissionResult = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
        /**
         * 패키지는 안드로이드 어플리케이션의 아이디이다.
         * 현재 어플리케이션이 CALL_PHONE에 대해 거부되어있는지 확인한다.
         * */
        if (permissionResult == PackageManager.PERMISSION_DENIED) {
            /** * 사용자가 PHONE STATE 권한을 거부한 적이 있는지 확인한다.
             * 거부한적이 있으면 True를 리턴하고
             * 거부한적이 없으면 False를 리턴한다. */
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                dialog.setTitle("Permission is required.") .setMessage("\"CALL\" permission is required to use this function. Do you want to continue?") .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /**
                         * 새로운 인스턴스(onClickListener)를 생성했기 때문에
                         * 버전체크를 다시 해준다.
                         * */
                        Log.d(TAG,"if");
                        getNumber();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // READ_PHONE_STATE 권한을 Android OS에 요청한다.
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000); } } }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Canceled.", Toast.LENGTH_SHORT).show(); } }) .create() .show();
            } // 최초로 권한을 요청할 때
            else { // PHONE STATE 권한을 Android OS에 요청한다.
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
            }
        } // READ_PHONE_STATE 권한이 있을 때
        else { // 즉시 실행
            getNumber();
            Log.d(TAG,"else2");

        }



        //등록
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이름
                name = nametxt.getText().toString();
                //age;
                //gender
                int id = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton)findViewById(id);

                //sharedpreferences
                SharedPreferences pref = getSharedPreferences("Info",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("name",name);
                editor.putString("age",Integer.toString(age));
                editor.putString("phone",phone);
                editor.putString("gender",rb.getText().toString());
                editor.commit();

                User user = new User(name, phone, Integer.toString(age), rb.getText().toString(), token);

                Call<User> registerUserInfo = service.registerUserInfo(user);
                registerUserInfo.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG,response.body().user_id);
                            ApplicationController.user_id=Integer.valueOf(response.body().user_id);

                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
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
        });




    }

    void getNumber(){
        //
        String mPhoneNumber="";
        try {
            TelephonyManager tmg = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tmg.getLine1Number();
            //  Toast.makeText(getApplicationContext(), mPhoneNumber, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG).show();
        }

        phoneText.setText(mPhoneNumber);
        phone=mPhoneNumber;
    }
    ///////////


    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();


                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                    // 액션이 READY일 경우

                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                    // 액션이 GENERATING일 경우

                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우
                    //Toast.makeText(getApplicationContext(),"COMPLETE",Toast.LENGTH_SHORT).show();

                    token=intent.getStringExtra("token");
                    Log.d(TAG,token);
                    ApplicationController.token=token;

                }
            }
        };
    }

    /**
     * 앱이 실행되어 화면에 나타날때 LocalBoardcastManager에 액션을 정의하여 등록한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            getNumber();
        }
    }
}

