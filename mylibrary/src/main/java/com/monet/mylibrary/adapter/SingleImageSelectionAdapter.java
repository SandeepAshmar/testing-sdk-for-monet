package com.monet.mylibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.monet.mylibrary.R;
import com.monet.mylibrary.listner.ImageQClickListner;
import com.monet.mylibrary.model.question.SdkOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.monet.mylibrary.activity.QuestionActivity.btn_question;
import static com.monet.mylibrary.activity.QuestionActivity.savedQuesAndAnswers;

public class SingleImageSelectionAdapter extends RecyclerView.Adapter<SingleImageSelectionAdapter.ViewHolder> {

    private Context ctx;
    private ImageQClickListner imageQClickListner;
    private ArrayList<SdkOptions> sdkOptions;

    public SingleImageSelectionAdapter(Context ctx, ImageQClickListner imageQClickListner, ArrayList<SdkOptions> sdkOptions) {
        this.ctx = ctx;
        this.imageQClickListner = imageQClickListner;
        this.sdkOptions = sdkOptions;
    }

    @NonNull
    @Override
    public SingleImageSelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_selection, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleImageSelectionAdapter.ViewHolder holder, int position) {

        final SdkOptions sdkOption = sdkOptions.get(position);
        if(sdkOption.getOption_value() != null){
            Glide.with(ctx).load(sdkOption.getOption_value()).into(holder.thumb);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageQClickListner != null){
                    imageQClickListner.onItemClick(sdkOption.getQuestion_id(), sdkOption.getOption_id());
                }
                notifyDataSetChanged();
            }
        });

        colorChange(holder, sdkOption);

    }

    @Override
    public int getItemCount() {
        return sdkOptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumb, seleciton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.img_itemThumb);
            seleciton = itemView.findViewById(R.id.img_itemSelection);
        }
    }

    public void colorChange(ViewHolder holder, SdkOptions question_options) {
        if (savedQuesAndAnswers == null || savedQuesAndAnswers.getSingleImageQId().size() == 0 || savedQuesAndAnswers.getSingleImageOid().size() == 0) {
            Log.e("", "");
        } else {
            if(savedQuesAndAnswers.getSingleImageQId().contains(question_options.getQuestion_id())){
                if (savedQuesAndAnswers.getSingleImageOid().contains(question_options.getOption_id())) {
                    holder.seleciton.setVisibility(View.VISIBLE);
                    holder.seleciton.setBackgroundResource(R.drawable.ic_radio_check_image);
                    btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
                    btn_question.setEnabled(true);
                } else {
                    holder.seleciton.setVisibility(View.GONE);
                }
            }else{
                holder.seleciton.setVisibility(View.GONE);
            }
        }
    }
}
