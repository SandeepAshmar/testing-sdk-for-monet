package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class SdkPreOptions {

    private String option_value;

    private String created_on;

    private ArrayList<SdkPreGrid> grid;

    private String option_id;

    private String question_id;

    private String created_by;

    public String getOption_value ()
    {
        return option_value;
    }

    public void setOption_value (String option_value)
    {
        this.option_value = option_value;
    }

    public String getCreated_on ()
    {
        return created_on;
    }

    public void setCreated_on (String created_on)
    {
        this.created_on = created_on;
    }

    public ArrayList<SdkPreGrid> getGrid ()
    {
        return grid;
    }

    public void setGrid (ArrayList<SdkPreGrid> grid)
    {
        this.grid = grid;
    }

    public String getOption_id ()
    {
        return option_id;
    }

    public void setOption_id (String option_id)
    {
        this.option_id = option_id;
    }

    public String getQuestion_id ()
    {
        return question_id;
    }

    public void setQuestion_id (String question_id)
    {
        this.question_id = question_id;
    }

    public String getCreated_by ()
    {
        return created_by;
    }

    public void setCreated_by (String created_by)
    {
        this.created_by = created_by;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [option_value = "+option_value+", created_on = "+created_on+", grid = "+grid+", option_id = "+option_id+", question_id = "+question_id+", created_by = "+created_by+"]";
    }
}
