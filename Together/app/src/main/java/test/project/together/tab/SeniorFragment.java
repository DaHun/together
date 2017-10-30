package test.project.together.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.project.together.R;
import test.project.together.adapter.ViewPagerAdapter;
import test.project.together.model.ChangeEvent;
import test.project.together.model.Posting;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class SeniorFragment extends Fragment {

   // @BindView(R.id.nextBtn) Button nextBtn;
    @BindView(R.id.registerBtn) Button registerBtn;
    @BindView(R.id.checkBtn) Button checkBtn;

    ///////

    final static String TAG="SeniorFragment";
    LinearLayout layout;



    public SeniorFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_senior, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }



    public void initSetting() {



        //Button
        /*
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Posting());
            }
        });
*/
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagerAdapter.subMode=1;
                EventBus.getDefault().post(new ChangeEvent());
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagerAdapter.subMode=2;
                EventBus.getDefault().post(new ChangeEvent());
            }
        });


    }



}
