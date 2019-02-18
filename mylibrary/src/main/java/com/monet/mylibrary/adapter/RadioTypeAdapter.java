package com.monet.mylibrary.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.activity.QuestionActivity;
import com.monet.mylibrary.listner.RadioClickListner;
import com.monet.mylibrary.model.question.SdkOptions;

import java.util.ArrayList;

import static com.monet.mylibrary.activity.QuestionActivity.savedQuesAndAnswers;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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

        colorChange(holder, options);

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

    public void colorChange(ViewHolder holder, SdkOptions question_options) {
        if (savedQuesAndAnswers == null || savedQuesAndAnswers.getRadioQuesIds().size() == 0 || savedQuesAndAnswers.getRadioAnsIds().size() == 0) {
            Log.e("", "");
        } else {
            if(savedQuesAndAnswers.getRadioQuesIds().contains(question_options.getQuestion_id())){
                if (savedQuesAndAnswers.getRadioAnsIds().contains(question_options.getOption_id())) {
                    holder.rd_view.setBackgroundResource(R.drawable.ic_selected_background);
                    holder.rd_opetionValue.setTextColor(Color.parseColor("#ffffff"));
                    QuestionActivity.btn_quesNext.setBackgroundResource(R.drawable.btn_pro_activate);
                    QuestionActivity.btn_quesNext.setEnabled(true);
                } else {
                    holder.rd_view.setBackground(null);
                    holder.rd_opetionValue.setTextColor(Color.parseColor("#000000"));
                }
            }else{
                holder.rd_view.setBackground(null);
                holder.rd_opetionValue.setTextColor(Color.parseColor("#000000"));
            }
        }
    }
}
