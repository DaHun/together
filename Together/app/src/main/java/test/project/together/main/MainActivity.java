package test.project.together.main;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.project.together.R;
import test.project.together.adapter.ViewPagerAdapter;
import test.project.together.application.ApplicationController;
import test.project.together.login.LoginActivity;
import test.project.together.model.ChangeEvent;
import test.project.together.model.InfoLayoutEvent;
import test.project.together.model.Matching;
import test.project.together.model.Posting;
import test.project.together.tab.VolunteerFragment;

import static test.project.together.adapter.ViewPagerAdapter.subMode;
import static test.project.together.adapter.ViewPagerAdapter.volsubMode;

public class MainActivity extends AppCompatActivity{
    //test eunju
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.pager) ViewPager viewPager;

    final String TAG="MainActivity";
    ViewPagerAdapter viewPagerAdapter;
    public static ContentResolver contentResolver;




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

        if(checkinfo()==1){//정보있음
            initSetting();

        }else{
            //등록된 정보 없으므로 정보등록
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    //개인정보 등록되었나 확인
    int checkinfo(){
        //정보있으면return 1; 없으면 return 0;

        String info1;
        String info2;
        String info3;

        SharedPreferences pref = getSharedPreferences("Info",MODE_PRIVATE);
        info1 = pref.getString("name","");
        info2 = pref.getString("age","");
        info3 = pref.getString("gender","");
        ApplicationController.user_id=pref.getInt("user_id",-1);

        if(info1== "" || info2== "" || info3== ""){
            return 0;   //없음
        }

        return 1; //정보있음
    }


    public void initSetting(){


        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(Color.rgb(0,0,0), Color.rgb(0,0,0));
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

                if(volsubMode==0) {
                    ViewPagerAdapter.mode = 0;
                }else if(volsubMode==1){
                    if (VolunteerFragment.infoLayoutMode) {
                        VolunteerFragment.infoLayoutMode = false;
                        EventBus.getDefault().post(new InfoLayoutEvent());
                    } else {
                        //ViewPagerAdapter.mode = 0;
                        volsubMode=0;
                    }
                }else if(volsubMode==2){
                    volsubMode=0;
                }else if(volsubMode==3){
                volsubMode=2;
            }



            viewPagerAdapter.notifyDataSetChanged();


            } else {
                super.onBackPressed();
            }
        }else if(viewPager.getCurrentItem() == 1){
            if(ViewPagerAdapter.SNSmode == 1)
                ViewPagerAdapter.SNSmode = 0;
            viewPagerAdapter.notifyDataSetChanged();
        }
    }





}

