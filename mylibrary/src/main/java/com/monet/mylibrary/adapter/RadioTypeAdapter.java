package com.monet.mylibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.listner.RadioClickListner;
import com.monet.mylibrary.model.question.SdkOptions;

import java.util.ArrayList;

public class RadioTypeAdapter extends RecyclerView.Adapter<RadioTypeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SdkOptions> sdkOptions;
    private RadioClickListner radioClickListner;

    public RadioTypeAdapter(Context context, ArrayList<SdkOptions> sdkOptions, RadioClickListner radioClickListner) {
        this.context = context;
        this.sdkOptions = sdkOptions;
        this.radioClickListner = radioClickListner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_radio_options, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SdkOptions options = sdkOptions.get(position);
        holder.rd_opetionValue.setText(options.getOption_value());

        holder.rd_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioClickListner != null) {
                    radioClickListner.onItemClick(v, position, options.getQuestion_id(), options.getOption_id());
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sdkOptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rd_opetionValue;
        RelativeLayout rd_view;

        public ViewHolder(View itemView) {
            super(itemView);
            rd_opetionValue = itemView.findViewById(R.id.rd_opetionValue);
            rd_view = itemView.findViewById(R.id.rd_view);
        }
    }
}
