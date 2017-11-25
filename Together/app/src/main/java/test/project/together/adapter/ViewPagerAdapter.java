package test.project.together.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import test.project.together.tab.CheckFragment;
import test.project.together.tab.RegisterFragment;
import test.project.together.tab.RegisterInfoFragment;
import test.project.together.tab.SNSFragment;
import test.project.together.tab.SNSplusFragment;
import test.project.together.tab.SelectFragment;
import test.project.together.tab.SeniorFragment;
import test.project.together.tab.VolMatchingInfoFragment;
import test.project.together.tab.VolunteerFragment;
import test.project.together.tab.VolunteerMatchingInfoFragment;
import test.project.together.tab.VolunteerSelectFragment;


/**
 * Created by jeongdahun on 2017. 7. 13..
 */

//Pager Adapter
public class ViewPagerAdapter extends FragmentStatePagerAdapter
{

    public static int mode=0;
    public static int subMode=0;
    public static int volsubMode=0;
    //SNS
    public static int SNSmode=0;
    public static int SNSsubMode=0;
    //
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Matching";
            case 1:
                return "REVIEW";
            default:
                return null;

        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        Log.d("Viewpager",mode+" "+subMode);

        switch(position){
            case 0:
                switch(mode){
                    case 0:
                        return new SelectFragment();
                    case 1:
                        if(subMode==0)
                            return new SeniorFragment();
                        else if(subMode==1)
                            return new RegisterFragment();
                        else if(subMode==2)
                            return new CheckFragment();
                        else if(subMode==3)
                            return new RegisterInfoFragment();
                    case 2:
                        if(volsubMode==0)
                            return new VolunteerSelectFragment();
                        else if(volsubMode==1)
                            return new VolunteerFragment();
                        else if(volsubMode==2)
                            return new VolunteerMatchingInfoFragment();
                        else if(volsubMode==3)
                            return new VolMatchingInfoFragment();
                }
            case 1:
                switch(SNSmode){
                    case 0:
                        return new SNSFragment();
                    case 1:
                        return new SNSplusFragment();
                }

                //return new SNSFragment();
            default:
                return null;
        }

    }
    @Override
    public int getCount() {
        return 2;
    }


}