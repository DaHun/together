package test.project.together.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.project.together.R;
import test.project.together.application.ApplicationController;
import test.project.together.model.ChangeEvent;
import test.project.together.model.Comment;
import test.project.together.model.Posting;
import test.project.together.network.NetworkService;
import test.project.together.tab.CommentActivity;
import test.project.together.viewholder.MyPostingViewHolder;

/**
 * Created by jeongdahun on 2017. 6. 26..
 */

public class MyPostingRecyclerViewAdapter extends RecyclerView.Adapter<MyPostingViewHolder>{

    ArrayList<Posting> items;
    Context context;
    NetworkService service;

    public TextToSpeech posttts;


    public MyPostingRecyclerViewAdapter(ArrayList<Posting> items){
        this.items=items;
    }

    @Override
    public MyPostingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_myposting,parent,false);
        context=parent.getContext();
        Log.d("Posting",items.size()+"");
        return new MyPostingViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final MyPostingViewHolder holder, final int position) {
        final Posting item=items.get(position);
        service= ApplicationController.getInstance().getNetworkService();
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

        String date = item.getDate().toString().substring(5,10);
        String time = item.getDate().toString().substring(11,16);
        holder.snsdate.setText(date+" "+time);
        holder.snslike.setText(String.valueOf(item.getLike_count()));
        holder.userName.setText(String.valueOf(item.getName()));

        holder.snscomment.setOnClickListener(new View.OnClickListener() {   //COMMENT버튼 눌렀을 때
            @Override
            public void onClick(View v) {
                CommentActivity.post_id=item.getPost_id();
                EventBus.getDefault().post(new Comment());
            }
        });

        holder.sharebtn.setOnClickListener(new View.OnClickListener() {
            Bitmap image=null;

            @Override
            public void onClick(View view) {

                //Toast.makeText(context,"페이스북 공유",Toast.LENGTH_LONG).show();

                Glide.with(context).load(items.get(position).getImage_path()).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        image = resource;
                    }
                });

                ShareDialog shareDialog = new ShareDialog((Activity) context);

                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();

                shareDialog.show(content);
            }
        });
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Void> delete_posting=service.delete_posting(item.getPost_id());
                delete_posting.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            Log.d("Delete","Success");
                            ViewPagerAdapter.SNSmode=2;
                            EventBus.getDefault().post(new ChangeEvent());
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
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
