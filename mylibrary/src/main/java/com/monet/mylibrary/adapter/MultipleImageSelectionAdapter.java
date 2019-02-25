package com.monet.mylibrary.adapter;

import android.content.Context;
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

import static com.monet.mylibrary.activity.QuestionActivity.savedQuesAndAnswers;

public class MultipleImageSelectionAdapter extends RecyclerView.Adapter<MultipleImageSelectionAdapter.ViewHolder> {

    private Context ctx;
    private ImageQClickListner imageQClickListner;
    private ArrayList<SdkOptions> sdkOptions;

    public MultipleImageSelectionAdapter(Context ctx, ImageQClickListner imageQClickListner,
                                         ArrayList<SdkOptions> sdkOptions) {
        this.ctx = ctx;
        this.imageQClickListner = imageQClickListner;
        this.sdkOptions = sdkOptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_selection, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SdkOptions question_options = sdkOptions.get(position);

        if (question_options.getOption_value() != null) {
            Glide.with(ctx).load(question_options.getOption_value())
                    .into(holder.thumb);
        } else {
            holder.thumb.setBackgroundResource(R.drawable.ic_imagenotavailable);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageQClickListner != null) {
                    imageQClickListner.onItemClick(question_options.getQuestion_id(), question_options.getOption_id());
                }
                notifyDataSetChanged();
            }
        });

        changeColor(holder, question_options);

    }

    private void changeColor(ViewHolder holder, SdkOptions question_options) {

        if (savedQuesAndAnswers != null || savedQuesAndAnswers.getMultiImageQid() != null ||
                savedQuesAndAnswers.getMultiImageOid() != null) {
            if (savedQuesAndAnswers.getMultiImageQid().contains(question_options.getQuestion_id())) {
                if (savedQuesAndAnswers.getMultiImageOid().contains(question_options.getOption_id())) {
                    holder.selection.setVisibility(View.VISIBLE);
                    holder.selection.setBackgroundResource(R.drawable.ic_checkbox_image);
                } else {
                    holder.selection.setVisibility(View.GONE);
                }
            } else {
                holder.selection.setVisibility(View.GONE);
            }
        }
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
}
