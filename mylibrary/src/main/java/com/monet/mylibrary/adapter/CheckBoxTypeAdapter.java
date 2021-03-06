package com.monet.mylibrary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.listner.CheckBoxClickListner;
import com.monet.mylibrary.model.sdk.Options;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.monet.mylibrary.activity.QuestionActivity.savedQuesAndAnswers;


public class CheckBoxTypeAdapter extends RecyclerView.Adapter<CheckBoxTypeAdapter.ViewHolder> {

    private Context ctx;
    private CheckBoxClickListner iClickListener;
    private ArrayList<Options> optionsArrayList;
    private String noneId = "";

    public CheckBoxTypeAdapter(Context ctx, CheckBoxClickListner iClickListener, ArrayList<Options> optionsArrayList) {
        this.ctx = ctx;
        this.iClickListener = iClickListener;
        this.optionsArrayList = optionsArrayList;
    }

    @NonNull
    @Override
    public CheckBoxTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_radio_options, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CheckBoxTypeAdapter.ViewHolder holder, final int position) {
        final Options question_options = optionsArrayList.get(position);

        holder.rd_opetionValue.setText(question_options.getOption_value());

        holder.rd_view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (question_options.getOption_value().equalsIgnoreCase("none") || question_options.getOption_value().equalsIgnoreCase("none of the above") ||
                        question_options.getOption_value().equalsIgnoreCase("none of above") || question_options.getOption_value().equalsIgnoreCase("prefer not to answer")) {

                    if (savedQuesAndAnswers.getCheckAnsId().contains(question_options.getOpt_id())) {
                        savedQuesAndAnswers.getCheckAnsId().clear();
                    } else {
                        savedQuesAndAnswers.getCheckAnsId().clear();
                        if (iClickListener != null) {
                            iClickListener.onItemCheckBoxClick(v, position, question_options.getOpt_id());
                        }
                        noneId = question_options.getOpt_id();
                    }
                } else {
                    if (savedQuesAndAnswers.getCheckAnsId().contains(noneId)) {
                        savedQuesAndAnswers.getCheckAnsId().clear();
                        if (iClickListener != null) {
                            iClickListener.onItemCheckBoxClick(v, position, question_options.getOpt_id());
                        }
                    } else {
                        if (iClickListener != null) {
                            iClickListener.onItemCheckBoxClick(v, position, question_options.getOpt_id());
                        }
                    }

                }
                notifyDataSetChanged();
            }
        });

        changeColor(holder, question_options);

    }

    @Override
    public int getItemCount() {
        return optionsArrayList.size();
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

    public void changeColor(ViewHolder holder, Options question_options) {

        if (savedQuesAndAnswers.getCheckAnsId().contains(question_options.getOpt_id())) {
            holder.rd_view.setBackgroundResource(R.drawable.ic_selected_background);
            holder.rd_opetionValue.setTextColor(Color.parseColor("#FFCF4A"));
        } else {
            holder.rd_view.setBackground(null);
            holder.rd_opetionValue.setTextColor(Color.parseColor("#ffffff"));
        }
    }


}
