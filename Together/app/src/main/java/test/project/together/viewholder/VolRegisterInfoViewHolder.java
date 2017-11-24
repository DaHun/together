package test.project.together.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import test.project.together.R;

public class VolRegisterInfoViewHolder extends RecyclerView.ViewHolder {
    public TextView voldate;
    public TextView volstartTime;
    public TextView volfinishTime;
    public TextView volwish;
    public Button voldetailBtn;

    public VolRegisterInfoViewHolder(View itemView) {
        super(itemView);
        voldate=(TextView)itemView.findViewById(R.id.voldate);
        volstartTime=(TextView) itemView.findViewById(R.id.volstartTime);
        volfinishTime=(TextView) itemView.findViewById(R.id.volfinishTime);
        volwish=(TextView) itemView.findViewById(R.id.volwish);
        voldetailBtn=(Button)itemView.findViewById(R.id.voldetailBtn);

    }
}
