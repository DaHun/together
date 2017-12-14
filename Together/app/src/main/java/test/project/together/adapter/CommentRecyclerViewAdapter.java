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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import test.project.together.R;
import test.project.together.model.Comment;
import test.project.together.viewholder.CommentViewHolder;

/**
 * Created by jeongdahun on 2017. 6. 26..
 */

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentViewHolder>{

    ArrayList<Comment> items;
    Context context;

    public static TextToSpeech tts;

    public CommentRecyclerViewAdapter(ArrayList<Comment> items, Context context){
        this.items=items;
        this.context=context;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_comment,parent,false);
        //Log.d("Comment",items.size()+"");
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, final int position) {
        final Comment item=items.get(position);

        Log.d("POSITION",position+"");
        if(!item.getImage_path().equals("null"))
            Glide.with(context).load(item.getImage_path()).into(holder.proimg);
        holder.userName.setText(item.getUser_name());
        String date = item.getDate().toString().substring(0,10);
        String time = item.getDate().toString().substring(11,16);
        holder.date.setText(date + " " +time);
        holder.content.setText(item.getContent());

        holder.readbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = holder.content.getText().toString();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Log.d("Comment","21");
                    ttsGreater21(txt);
                } else {
                    Log.d("Comment","20");
                    ttsUnder20(txt);
                }
            }
        });
        //여기다가 구현


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
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
}