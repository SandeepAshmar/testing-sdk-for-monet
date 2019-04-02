package com.monet.mylibrary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.listner.RadioClickListner;
import com.monet.mylibrary.model.sdk.Options;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.monet.mylibrary.activity.QuestionActivity.btn_question;
import static com.monet.mylibrary.activity.QuestionActivity.savedQuesAndAnswers;

public class RadioTypeAdapter extends RecyclerView.Adapter<RadioTypeAdapter.ViewHolder> {

    private Context ctx;
    private RadioClickListner radioClickListner;
    private ArrayList<Options> sdkOptions;

    public RadioTypeAdapter(Context ctx, ArrayList<Options> sdkOptions, RadioClickListner radioClickListner) {
        this.ctx = ctx;
        this.radioClickListner = radioClickListner;
        this.sdkOptions = sdkOptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_radio_options, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Options options = sdkOptions.get(position);
        holder.rd_opetionValue.setText(options.getOption_value());

        holder.rd_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioClickListner != null) {
                    radioClickListner.onItemClick(v, position, options.getOpt_id());
                    notifyDataSetChanged();
                }
            }
        });

        colorChange(holder, options);
    }

    @Override
    public int getItemCount() {
        return sdkOptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView rd_opetionValue;
        private RelativeLayout rd_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rd_opetionValue = itemView.findViewById(R.id.rd_opetionValue);
            rd_view = itemView.findViewById(R.id.rd_view);
        }
    }

    public void colorChange(ViewHolder holder, Options question_options) {
        if (savedQuesAndAnswers.getRadioAnsIds().contains(question_options.getOpt_id())) {
            holder.rd_view.setBackgroundResource(R.drawable.ic_selected_background);
            holder.rd_opetionValue.setTextColor(Color.parseColor("#FFCF4A"));
            btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
            btn_question.setEnabled(true);
        } else {
            holder.rd_view.setBackground(null);
            holder.rd_opetionValue.setTextColor(Color.parseColor("#ffffff"));
        }
    }

}
