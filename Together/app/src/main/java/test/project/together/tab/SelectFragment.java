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
import test.project.together.model.Change;
import test.project.together.model.Posting;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class SelectFragment extends Fragment
{

    @BindView(R.id.takeBtn) Button takeBtn;
    @BindView(R.id.giveBtn) Button giveBtn;
    @BindView(R.id.nextBtn) Button nextBtn;

    LinearLayout layout;


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
        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagerAdapter.mode=1;
                EventBus.getDefault().post(new Change());
            }
        });

        giveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagerAdapter.mode=2;
                EventBus.getDefault().post(new Change());
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Posting());
            }
        });
    }


}
