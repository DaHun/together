package test.project.together.login;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.project.together.R;
import test.project.together.adapter.ViewPagerAdapter;
import test.project.together.main.MainActivity;
import test.project.together.model.ChangeEvent;
import test.project.together.model.InfoLayoutEvent;
import test.project.together.model.Matching;
import test.project.together.model.Posting;
import test.project.together.tab.VolunteerFragment;

import static test.project.together.adapter.ViewPagerAdapter.subMode;

public class LoginActivity extends AppCompatActivity{

    EditText nametxt;
    Spinner agespinner;

    RadioGroup rg;

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
        rg = (RadioGroup)findViewById(R.id.radiogroup);

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
                editor.putString("gender",rb.getText().toString());
                editor.commit();


                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //서버에 저장
            }
        });




    }

}

