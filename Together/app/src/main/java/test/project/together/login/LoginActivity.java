package test.project.together.login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

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

public class LoginActivity extends AppCompatActivity implements android.widget.RadioGroup.OnCheckedChangeListener{

    EditText nametxt;
    Spinner agespinner;
    EditText phonetxt;

    RadioGroup radio;

    Button signinbtn;
    Button cancelbtn;

    String name;
    String phone;
    int age;
    int gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        initSetting();
        init();
    }

    public void initSetting(){
        signinbtn=(Button)findViewById(R.id.sign_in_btn);
        cancelbtn=(Button)findViewById(R.id.sign_in_cancel);
        nametxt=(EditText)findViewById(R.id.sign_in_name);
        agespinner=(Spinner)findViewById(R.id.sign_in_age);
        radio = (RadioGroup)findViewById(R.id.radio);
        radio.setOnCheckedChangeListener(this);
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

        //성별


        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이름
                name = nametxt.getText().toString();
                //전화번호
                phone = phonetxt.getText().toString();
                //age;
                //gender;
                //서버에 저장
            }
        });




    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        if(radio.getCheckedRadioButtonId() == R.id.radio_boy){
            gender = 0;//성별 남자
        }else{
            gender = 1;//성별 여자
        }

    }


}

