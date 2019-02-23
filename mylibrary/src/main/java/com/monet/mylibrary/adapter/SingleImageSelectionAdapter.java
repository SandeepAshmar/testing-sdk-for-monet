package com.monet.mylibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.monet.mylibrary.R;
import com.monet.mylibrary.listner.IOnItemClickListener;
import com.monet.mylibrary.model.question.SdkOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SingleImageSelectionAdapter extends RecyclerView.Adapter<SingleImageSelectionAdapter.ViewHolder> {

    private Context ctx;
    private IOnItemClickListener iOnItemClickListener;
    private ArrayList<SdkOptions> sdkOptions;

    public SingleImageSelectionAdapter(Context ctx, IOnItemClickListener iOnItemClickListener, ArrayList<SdkOptions> sdkOptions) {
        this.ctx = ctx;
        this.iOnItemClickListener = iOnItemClickListener;
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

        SdkOptions sdkOption = sdkOptions.get(position);
        if(sdkOption.getOption_value() != null){
            Glide.with(ctx).load(sdkOption.getOption_value()).into(holder.thumb);
        }

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
}
