package com.monet.mylibrary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.listner.IOnItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.monet.mylibrary.activity.QuestionActivity.savedQuesAndAnswers;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.ViewHolder> {

    private Context context;
    private int size;
    int i = 0;
    private IOnItemClickListener iOnItemClickListener;
    private String quesId;
    private String ansId;

    public RateAdapter(Context context, int size, IOnItemClickListener iOnItemClickListener, String quesId) {
        this.context = context;
        this.size = size;
        this.iOnItemClickListener = iOnItemClickListener;
        this.quesId = quesId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question_rate, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        i = i + 1;
        holder.tv_rate.setText(String.valueOf(i));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iOnItemClickListener != null) {
                    iOnItemClickListener.onItemClick(v, position);
                    i = 0;
                    notifyDataSetChanged();
                }
            }
        });

        if (savedQuesAndAnswers.getRateQuesId().contains(quesId)) {
            int pos = savedQuesAndAnswers.getRateQuesId().indexOf(quesId);
            ansId = String.valueOf(savedQuesAndAnswers.getRateAnsId().get(pos));
            if (holder.tv_rate.getText().equals(ansId)) {
                holder.rate_view.setBackgroundResource(R.drawable.rate_backround);
                holder.tv_rate.setTextColor(Color.parseColor("#ffffff"));
            } else {
                holder.rate_view.setBackgroundResource(R.drawable.circle);
                holder.tv_rate.setTextColor(Color.parseColor("#000000"));
            }
        } else {
            holder.rate_view.setBackgroundResource(R.drawable.circle);
            holder.tv_rate.setTextColor(Color.parseColor("#000000"));
        }

    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_rate;
        View rate_view;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_rate = itemView.findViewById(R.id.tv_rate);
            rate_view = itemView.findViewById(R.id.rate_view);
        }
    }
}
