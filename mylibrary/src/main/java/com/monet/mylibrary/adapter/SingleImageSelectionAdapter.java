package com.monet.mylibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.listner.ImageQClickListner;
import com.monet.mylibrary.model.sdk.Options;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.monet.mylibrary.activity.QuestionActivity.btn_question;
import static com.monet.mylibrary.activity.QuestionActivity.savedQuesAndAnswers;


public class SingleImageSelectionAdapter extends RecyclerView.Adapter<SingleImageSelectionAdapter.ViewHolder> {

    private Context ctx;
    private ImageQClickListner imageQClickListner;
    private ArrayList<Options> sdkOptions;

    public SingleImageSelectionAdapter(Context ctx, ImageQClickListner imageQClickListner, ArrayList<Options> sdkOptions) {
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

        final Options sdkOption = sdkOptions.get(position);
        if (sdkOption.getOption_value() != null) {
            Picasso.get().load(sdkOption.getOption_value())
                    .into(holder.thumb);
        } else {
            holder.thumb.setBackgroundResource(R.drawable.ic_imagenotavailable);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageQClickListner != null) {
                    imageQClickListner.onItemClick(sdkOption.getOpt_id());
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
        private ImageView thumb, selection;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.img_itemThumb);
            selection = itemView.findViewById(R.id.img_itemSelection);
        }
    }

    public void colorChange(ViewHolder holder, Options question_options) {
        if (savedQuesAndAnswers.getSingleImageOid().contains(question_options.getOpt_id())) {
            holder.selection.setVisibility(View.VISIBLE);
            holder.selection.setBackgroundResource(R.drawable.ic_radio_check_image);
            btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
            btn_question.setEnabled(true);
        } else {
            holder.selection.setVisibility(View.GONE);
        }

    }
}
