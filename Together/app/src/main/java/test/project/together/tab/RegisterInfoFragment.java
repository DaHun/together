package test.project.together.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.project.together.R;
import test.project.together.application.ApplicationController;
import test.project.together.model.Matching;
import test.project.together.model.Posting;
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


    LinearLayout layout;
    NetworkService service;
    final static String TAG="RegisterInfoFragment";

    public static int matching_id;
    public RegisterInfoFragment() {
        super();
    }


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
                    if(response.body().isCheck()==1)
                        matchingInfo();
                }
            }

            @Override
            public void onFailure(Call<Matching> call, Throwable t) {

            }
        });





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
