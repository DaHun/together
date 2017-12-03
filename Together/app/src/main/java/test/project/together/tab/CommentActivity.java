package test.project.together.tab;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import pl.polidea.view.ZoomView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.project.together.R;
import test.project.together.adapter.CommentRecyclerViewAdapter;
import test.project.together.application.ApplicationController;
import test.project.together.model.Comment;
import test.project.together.network.NetworkService;

/**
 * Created by Eunju on 2017-11-24.
 */

public class CommentActivity extends Activity {

   /* @BindView(R.id.commentRecyclerview)
    RecyclerView commentRecyclerView;

    @BindView(R.id.record) Button recordbtn;
    @BindView(R.id.commentregibtn) Button regibtn;
*/
   private final int REQ_CODE_SPEECH_INPUT = 100;
   EditText commenttext;

    public static int post_id;

    NetworkService service;
    final String TAG="ActivityComment";
    LinearLayout layout;
    ArrayList<Comment> commentList;
    CommentRecyclerViewAdapter commentRecyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView commentRecyclerView;

    String txt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_comment, null, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ZoomView zoomView = new ZoomView(this);
        zoomView.addView(v);
        zoomView.setLayoutParams(layoutParams);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        container.addView(zoomView);

        initSetting();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(commentRecyclerViewAdapter.tts != null) {
            commentRecyclerViewAdapter.tts.stop();
            commentRecyclerViewAdapter.tts.shutdown();
        }
    }
//
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.activity_comment, container, false);

        //ButterKnife.bind(this, layout);


        return layout;
    }

    public void initSetting() {
        service= ApplicationController.getInstance().getNetworkService();

        commenttext = (EditText)findViewById(R.id.commenttxt);
        commentRecyclerView = (RecyclerView)findViewById(R.id.commentRecyclerview);

        Button recordbtn = (Button)findViewById(R.id.record);
        recordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askSpeechInput();
            }
        });

        Button cancelbtn = (Button)findViewById(R.id.commentcancelbtn);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Button regibtn = (Button)findViewById(R.id.commentregibtn);
        regibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Comment comment=new Comment(post_id, commenttext.getText().toString(), getDateString(), ApplicationController.user_id);
                Call<ArrayList<Comment>> register_comment=service.register_comment(comment);
                register_comment.enqueue(new Callback<ArrayList<Comment>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                        if(response.isSuccessful()){
                            commentList=response.body();
                            Log.d(TAG,commentList.size()+" ");

                            //RecyclerView Setting
                            commentRecyclerViewAdapter=new CommentRecyclerViewAdapter(commentList);
                            commentRecyclerView.setAdapter(commentRecyclerViewAdapter);
                            linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                            commentRecyclerView.setLayoutManager(linearLayoutManager);
                        }else
                            Log.d(TAG,"fail1");
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {
                        Log.d(TAG,"fail2");

                    }
                });

            }
        });


        Call<ArrayList<Comment>> load_comment=service.load_comment(post_id);
        load_comment.enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                if(response.isSuccessful()){
                    commentList=response.body();
                    Log.d(TAG,commentList.size()+" ");

                    //RecyclerView Setting
                    commentRecyclerViewAdapter=new CommentRecyclerViewAdapter(commentList);
                    commentRecyclerView.setAdapter(commentRecyclerViewAdapter);
                    linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                    commentRecyclerView.setLayoutManager(linearLayoutManager);

                }else
                    Log.d(TAG,"fail1");

            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {
                Log.d(TAG,"fail2");

            }
        });

    }

    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hi speak something!");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txt=commenttext.getText().toString();
                    commenttext.setText(txt+result.get(0)+" ");
                }
                break;
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
