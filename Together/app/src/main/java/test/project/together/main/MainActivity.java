package test.project.together.main;

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
//ㅏㅗ하ㅘㅓ
    ViewPagerAdapter viewPagerAdapter;
//testtttttt
    //test222222

    //gong ho jin babo
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
    }

    public void initSetting(){


        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(Color.rgb(0,0,0), Color.rgb(255,255,255));

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
}

