package test.project.together.tab;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.project.together.R;
import test.project.together.adapter.ViewPagerAdapter;
import test.project.together.application.ApplicationController;
import test.project.together.model.ChangeEvent;
import test.project.together.model.Matching;
import test.project.together.model.Posting;
import test.project.together.network.NetworkService;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class SNSplusFragment extends Fragment{

    //@BindView(R.id.postingRecyclerView) RecyclerView postingRecyclerView;
    @BindView(R.id.previousBtn) Button previousBtn;
    //@BindView(R.id.plusposting) Button plusregi;
    @BindView(R.id.registerposting) Button registerposting;
    @BindView(R.id.getphotobtn) Button getphotobtn;
    @BindView(R.id.newposttxt) TextView newposttxt;
    @BindView(R.id.newImage) ImageView newImage;

    final String TAG="SNSplusFragment";
    LinearLayout layout;
    NetworkService service;

    Uri image_uri;
    String image_path = "";
    String date = "";
    String content = "";
    final int REQ_CODE_SELECT_IMAGE=100;

    //ArrayList<Posting> postingList;
    //PostingRecyclerViewAdapter postingRecyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;

    public SNSplusFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_plusposting, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }

    public void initSetting() {

        //사진 가져오기
        getphotobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });


        //게시물 추가
        registerposting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = getDateString();

                content = newposttxt.getText().toString();
                image_path = image_uri.toString();
                Posting posting = new Posting(ApplicationController.user_id, date, content, image_path);
                Call<Void> snsPlus=service.snsPlus(posting);
                snsPlus.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG,"success");
                        }else
                            Log.d(TAG,"fail1");

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(TAG,"fail2");

                    }
                });

                Toast.makeText(getContext(), "눌림",Toast.LENGTH_SHORT).show();
                ViewPagerAdapter.SNSmode=0;
                EventBus.getDefault().post(new ChangeEvent());
            }
        });



        //이전 버튼 클릭 리스너
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Matching());
            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
     //   Toast.makeText(getBaseContext(), "resultCode : "+resultCode,Toast.LENGTH_SHORT).show();

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    image_uri = data.getData();

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                    //배치해놓은 ImageView에 set
                    newImage.setImageBitmap(image_bitmap);


                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getDateString()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String str_date = df.format(new Date());

        return str_date;
    }
}
