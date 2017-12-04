package test.project.together.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import test.project.together.R;
import test.project.together.model.ChangeEvent;
import test.project.together.model.Matching;
import test.project.together.tab.RegisterInfoFragment;
import test.project.together.tab.VolMatchingInfoFragment;
import test.project.together.viewholder.RegisterInfoViewHolder;
import test.project.together.viewholder.VolRegisterInfoViewHolder;

/**
 * Created by jeongdahun on 2017. 6. 26..
 */

public class VolRegisterInfoRecyclerViewAdapter extends RecyclerView.Adapter<VolRegisterInfoViewHolder>{

    ArrayList<Matching> items;
    Context context;

    public VolRegisterInfoRecyclerViewAdapter(ArrayList<Matching> items){
        this.items=items;
    }

    @Override
    public VolRegisterInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.vol_viewholder_register,parent,false);
        context=parent.getContext();

        return new VolRegisterInfoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VolRegisterInfoViewHolder holder, final int position) {
        final Matching item=items.get(position);

        holder.voldate.setText(item.getDate());
        holder.volstartTime.setText(item.getStartTime());
        holder.volfinishTime.setText(item.getFinishTime());

        holder.volwish.setText(item.getWish());

        holder.voldetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolMatchingInfoFragment.matching_id=item.getMatching_id();
                ViewPagerAdapter.volsubMode=3;
                EventBus.getDefault().post(new ChangeEvent());
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
}
