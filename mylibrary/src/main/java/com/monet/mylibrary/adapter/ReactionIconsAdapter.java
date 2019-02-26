package com.monet.mylibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.listner.IOnItemClickListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReactionIconsAdapter extends RecyclerView.Adapter<ReactionIconsAdapter.ViewHolder> {

    private Context context;
    private IOnItemClickListener iClickListener;
    private ArrayList<String> iconList;

    public ReactionIconsAdapter(Context context, IOnItemClickListener iClickListener, ArrayList<String> iconList) {
        this.context = context;
        this.iClickListener = iClickListener;
        this.iconList = iconList;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textView.setText(iconList.get(position));

        if (iconList.get(position).equals("Like")) {
            holder.imageView.setImageResource(R.drawable.ic_like);
        }
        if (iconList.get(position).equals("Love")) {
            holder.imageView.setImageResource(R.drawable.ic_love);
        }
        if (iconList.get(position).equals("Want")) {
            holder.imageView.setImageResource(R.drawable.ic_want);
        }
        if (iconList.get(position).equals("Memorable")) {
            holder.imageView.setImageResource(R.drawable.ic_memorable);
        }
        if (iconList.get(position).equals("Dislike")) {
            holder.imageView.setImageResource(R.drawable.ic_dislike);
        }
        if (iconList.get(position).equals("Accurate")) {
            holder.imageView.setImageResource(R.drawable.ic_helpful);
        }
        if (iconList.get(position).equals("Misleading")) {
            holder.imageView.setImageResource(R.drawable.ic_not_helpful);
        }
        if (iconList.get(position).equals("Engaging")) {
            holder.imageView.setImageResource(R.drawable.ic_surprise);
        }
        if (iconList.get(position).equals("Boring")) {
            holder.imageView.setImageResource(R.drawable.ic_boring);
        }
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_gridItems);
            imageView = itemView.findViewById(R.id.img_gridItems);
        }
    }
}
