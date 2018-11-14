package com.monet.mylibrary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.monet.mylibrary.R;
import com.monet.mylibrary.modle.getCampDetails.cmpDetails.GetCampDetails_Response;

import java.util.ArrayList;

public class LandAdapter extends RecyclerView.Adapter<LandAdapter.ViewHolder> {

    private Context ctx;
    private ArrayList<GetCampDetails_Response> detailsResponses;

    public LandAdapter(Context ctx, ArrayList<GetCampDetails_Response> detailsResponses) {
        this.ctx = ctx;
        this.detailsResponses = detailsResponses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_land_page, parent, false);

       ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GetCampDetails_Response details_response = detailsResponses.get(position);

        if(details_response.getC_thumb_url().isEmpty() && details_response.getC_thumb_url() == null){
            holder.img_landImage.setImageDrawable(null);
        }else{
            Glide.with(ctx).load(details_response.getC_thumb_url()).into(holder.img_landImage);
        }

        holder.tv_landCam.setText(details_response.getCmp_name());

        int seconds = Integer.valueOf(details_response.getC_length());

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        if(hours == 0){
            holder.tv_vid_landTime.setText(minutes + ":" + seconds);
        }else{
            holder.tv_vid_landTime.setText(hours+":"+minutes + ":" + seconds);
        }

    }

    @Override
    public int getItemCount() {
        return detailsResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_landImage;
        TextView tv_landCam, tv_vid_landTime;

        public ViewHolder(View itemView) {
            super(itemView);

            img_landImage = itemView.findViewById(R.id.img_landImage);
            tv_landCam = itemView.findViewById(R.id.tv_landCam);
            tv_vid_landTime = itemView.findViewById(R.id.tv_vid_landTime);
        }
    }
}
