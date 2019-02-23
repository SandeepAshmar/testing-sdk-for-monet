package com.monet.mylibrary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.listner.RadioClickListner;
import com.monet.mylibrary.model.question.SdkGrid;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridOptionAdapter extends RecyclerView.Adapter<GridOptionAdapter.ViewHolder> {

    private Context context;
    private RadioClickListner radioClickListner;
    private ArrayList<SdkGrid> gridArrayList;
    private String optionId = "";

    public GridOptionAdapter(Context context, RadioClickListner radioClickListner, ArrayList<SdkGrid> gridArrayList,
                             String optionId) {
        this.context = context;
        this.radioClickListner = radioClickListner;
        this.gridArrayList = gridArrayList;
        this.optionId = optionId;
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
        final SdkGrid sdkGrid = gridArrayList.get(position);

        holder.rd_opetionValue.setText(sdkGrid.getGrid_value());

        holder.rd_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioClickListner != null) {
                    radioClickListner.onItemClick(v, position, "", sdkGrid.getGrid_id());
                    sdkGrid.setChecked(true);
                    notifyDataSetChanged();
                }
            }
        });

        colorChange(holder, sdkGrid);
    }

    private void colorChange(ViewHolder holder, SdkGrid sdkGrid) {

        if (sdkGrid.getChecked()) {
            holder.rd_view.setBackgroundResource(R.drawable.ic_selected_background);
            holder.rd_opetionValue.setTextColor(Color.parseColor("#FFCF4A"));
        } else {
            holder.rd_view.setBackground(null);
            holder.rd_opetionValue.setTextColor(Color.parseColor("#ffffff"));
        }

//        if (savedQuesAndAnswers == null || savedQuesAndAnswers.gridQuesIds == null
//                || savedQuesAndAnswers.gridOptionIds == null || savedQuesAndAnswers.gridAnsIds == null) {
//            Log.e("", "");
//        } else {
//            if (savedQuesAndAnswers.getGridQuesIds().contains(sdkGrid.getGrid_q_id())) {
//                if (savedQuesAndAnswers.getGridOptionIds().contains(optionId)) {
//                    if (savedQuesAndAnswers.getGridAnsIds().contains(sdkGrid.getGrid_id())) {
//                        holder.rd_view.setBackgroundResource(R.drawable.ic_selected_background);
//                        holder.rd_opetionValue.setTextColor(Color.parseColor("#FFCF4A"));
//                    } else {
//                        holder.rd_view.setBackground(null);
//                        holder.rd_opetionValue.setTextColor(Color.parseColor("#ffffff"));
//                    }
//                }else{
//                    holder.rd_view.setBackground(null);
//                    holder.rd_opetionValue.setTextColor(Color.parseColor("#ffffff"));
//                }
//
//            } else {
//                holder.rd_view.setBackground(null);
//                holder.rd_opetionValue.setTextColor(Color.parseColor("#ffffff"));
//            }
//        }
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
