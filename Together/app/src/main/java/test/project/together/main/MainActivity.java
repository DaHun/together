package test.project.together.main;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.project.together.R;
import test.project.together.adapter.ViewPagerAdapter;
import test.project.together.application.ApplicationController;
import test.project.together.gcm.QuickstartPreferences;
import test.project.together.gcm.RegistrationIntentService;
import test.project.together.model.ChangeEvent;
import test.project.together.model.InfoLayoutEvent;
import test.project.together.model.Matching;
import test.project.together.model.Posting;
import test.project.together.tab.VolunteerFragment;

import static test.project.together.adapter.ViewPagerAdapter.subMode;

public class MainActivity extends AppCompatActivity{
    //test eunju
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.pager) ViewPager viewPager;

    final String TAG="MainActivity";
    ViewPagerAdapter viewPagerAdapter;
    public static ContentResolver contentResolver;

    ///
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;





    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFirstPageEvent(Matching matching){
        viewPager.setCurrentItem(0);
    }
    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onToSecondPageEvent(Posting posting){
        viewPager.setCurrentItem(1);
    }
    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void change(ChangeEvent changeEvent){
        viewPagerAdapter.notifyDataSetChanged();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initSetting();

        getInstanceIdToken();
        registBroadcastReceiver();

    }

    public void initSetting(){


        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(Color.rgb(0,0,0), Color.rgb(255,255,255));

        contentResolver=getContentResolver();

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {

        if(viewPager.getCurrentItem() == 0) {
            if (ViewPagerAdapter.mode == 1 ) {
                if (subMode == 0)
                    ViewPagerAdapter.mode = 0;
                else if (subMode == 1 || subMode == 2)
                    subMode = 0;
                else if (subMode == 3)
                    subMode = 2;

                viewPagerAdapter.notifyDataSetChanged();
            } else if (ViewPagerAdapter.mode == 2) {

                if (VolunteerFragment.infoLayoutMode) {
                    VolunteerFragment.infoLayoutMode = false;
                    EventBus.getDefault().post(new InfoLayoutEvent());
                } else {
                    ViewPagerAdapter.mode = 0;
                    viewPagerAdapter.notifyDataSetChanged();
                }
            } else {
                super.onBackPressed();
            }
        }else if(viewPager.getCurrentItem() == 1){
            if(ViewPagerAdapter.SNSmode == 1)
                ViewPagerAdapter.SNSmode = 0;
            viewPagerAdapter.notifyDataSetChanged();
        }
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

                    String token=intent.getStringExtra("token");
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


}

