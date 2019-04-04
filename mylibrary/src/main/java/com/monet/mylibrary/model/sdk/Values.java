package com.monet.mylibrary.model.sdk;

import java.util.ArrayList;

public class Values
{
    private String grid_value;

    private String gr_id;

    private ArrayList<String> optionId;

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getGrid_value ()
    {
        return grid_value;
    }

    public void setGrid_value (String grid_value)
    {
        this.grid_value = grid_value;
    }

    public String getGr_id ()
    {
        return gr_id;
    }

    public void setGr_id (String gr_id)
    {
        this.gr_id = gr_id;
    }

    public ArrayList<String> getOptionId() {
        return optionId;
    }

    public void setOptionId(ArrayList<String> optionId) {
        this.optionId = optionId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [grid_value = "+grid_value+", gr_id = "+gr_id+"]";
    }
}