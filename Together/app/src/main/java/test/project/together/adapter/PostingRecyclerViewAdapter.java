package test.project.together.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import test.project.together.R;
import test.project.together.model.Comment;
import test.project.together.model.Posting;
import test.project.together.tab.CommentActivity;
import test.project.together.viewholder.PostingViewHolder;

/**
 * Created by jeongdahun on 2017. 6. 26..
 */

public class PostingRecyclerViewAdapter extends RecyclerView.Adapter<PostingViewHolder>{

    ArrayList<Posting> items;
    Context context;

    public TextToSpeech posttts;


    public PostingRecyclerViewAdapter(ArrayList<Posting> items){
        this.items=items;
    }

    @Override
    public PostingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_posting,parent,false);
        context=parent.getContext();
        Log.d("Posting",items.size()+"");
        return new PostingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PostingViewHolder holder, final int position) {
        final Posting item=items.get(position);

        Glide.with(context).load(items.get(position).getImage_path()).into(holder.postingImage);
        holder.snstext.setText(item.getContent());

        posttts=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    posttts.setLanguage(Locale.ENGLISH);
                }
            }

        });

        holder.snstext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = holder.snstext.getText().toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21(txt);
                } else {
                    ttsUnder20(txt);
                }
            }
        });

        String date = item.getDate().toString().substring(0,10);
        String time = item.getDate().toString().substring(11,16);
        holder.snsdate.setText(date+" "+time);
        holder.snslike.setText(String.valueOf(item.getLike_count()));

        holder.snscomment.setOnClickListener(new View.OnClickListener() {   //COMMENT버튼 눌렀을 때
            @Override
            public void onClick(View v) {
                CommentActivity.post_id=item.getPost_id();
                EventBus.getDefault().post(new Comment());
            }
        });

    }

    @Override
    public int getItemCount() {
        if(items != null)
            return items.size();
        else
            return 0;
    }


    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        posttts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        posttts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

}
