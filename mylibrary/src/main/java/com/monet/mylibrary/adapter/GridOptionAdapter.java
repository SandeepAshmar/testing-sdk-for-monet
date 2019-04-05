package com.monet.mylibrary.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.listner.RadioClickListner;
import com.monet.mylibrary.model.sdk.Options;
import com.monet.mylibrary.model.sdk.Values;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.monet.mylibrary.activity.QuestionActivity.savedQuesAndAnswers;

public class GridOptionAdapter extends RecyclerView.Adapter<GridOptionAdapter.ViewHolder> {

    private RadioClickListner radioClickListner;
    private ArrayList<Options> gridArrayList;
    private String optionId;
    private int optionPosition;

    public GridOptionAdapter(RadioClickListner radioClickListner, ArrayList<Options> gridArrayList,
                             String optionId, int optionPosition) {
        this.radioClickListner = radioClickListner;
        this.gridArrayList = gridArrayList;
        this.optionId = optionId;
        this.optionPosition = optionPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_radio_options, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.rd_opetionValue.setText(gridArrayList.get(optionPosition).getValues().get(position).getGrid_value());

        holder.rd_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioClickListner != null) {
                    radioClickListner.onItemClick(v, position, gridArrayList.get(optionPosition).getValues().get(position).getGr_id());
                    for (int i = 0; i < gridArrayList.get(optionPosition).getValues().size(); i++) {
                        gridArrayList.get(optionPosition).getValues().get(i).setChecked(false);
                    }
                    gridArrayList.get(optionPosition).getValues().get(position).setChecked(true);
                    notifyDataSetChanged();
                }
            }
        });

        colorChange(holder, gridArrayList.get(optionPosition).getValues().get(position));
    }

    private void colorChange(ViewHolder holder, Values sdkGrid) {
        if (savedQuesAndAnswers.getGridOptionIds().contains(optionId)) {
            if (savedQuesAndAnswers.getGridAnsIds().contains(sdkGrid.getGr_id())) {
                if (sdkGrid.isChecked()) {
                    holder.rd_view.setBackgroundResource(R.drawable.ic_selected_background);
                    holder.rd_opetionValue.setTextColor(Color.parseColor("#FFCF4A"));
                } else {
                    holder.rd_view.setBackground(null);
                    holder.rd_opetionValue.setTextColor(Color.parseColor("#ffffff"));
                }
            } else {
                if (sdkGrid.isChecked()) {
                    holder.rd_view.setBackgroundResource(R.drawable.ic_selected_background);
                    holder.rd_opetionValue.setTextColor(Color.parseColor("#FFCF4A"));
                } else {
                    holder.rd_view.setBackground(null);
                    holder.rd_opetionValue.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        }else{
            if (sdkGrid.isChecked()) {
                holder.rd_view.setBackgroundResource(R.drawable.ic_selected_background);
                holder.rd_opetionValue.setTextColor(Color.parseColor("#FFCF4A"));
            } else {
                holder.rd_view.setBackground(null);
                holder.rd_opetionValue.setTextColor(Color.parseColor("#ffffff"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return gridArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView rd_opetionValue;
        private RelativeLayout rd_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rd_opetionValue = itemView.findViewById(R.id.rd_opetionValue);
            rd_view = itemView.findViewById(R.id.rd_view);
        }
    }
}
