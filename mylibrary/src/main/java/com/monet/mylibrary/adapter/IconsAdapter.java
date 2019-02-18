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
import com.monet.mylibrary.listner.IOnItemClickListener;
import com.monet.mylibrary.model.Item;

import java.util.ArrayList;

public class IconsAdapter extends RecyclerView.Adapter<IconsAdapter.ViewHolder> {

    ArrayList<Item> items = new ArrayList<>();
    Context context;
    private IOnItemClickListener iClickListener;


    public IconsAdapter(ArrayList<Item> items, Context context, IOnItemClickListener iClickListener) {
        this.items = items;
        this.context = context;
        this.iClickListener = iClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_icons_grid, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.textView.setText(items.get(position).getReactionName());
        Glide.with(context).load(items.get(position).getImageUrl()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iClickListener != null) {
                    iClickListener.onItemClick(v, position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_gridItems);
            imageView = itemView.findViewById(R.id.img_gridItems);
        }
    }
}
