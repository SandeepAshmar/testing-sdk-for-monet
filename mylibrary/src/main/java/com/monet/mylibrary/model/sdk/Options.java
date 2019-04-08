package com.monet.mylibrary.model.sdk;

import java.util.ArrayList;

public class Options
{
    private String option_value;

    private String type;

    private String opt_id;

    private ArrayList<Values> value;

    public ArrayList<Values> getValues() {
        return value;
    }

    public void setValues(Values values) {
        values = new Values();
        this.value.add(values);
    }

    public String getOption_value ()
    {
        return option_value;
    }

    public void setOption_value (String option_value)
    {
        this.option_value = option_value;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getOpt_id ()
    {
        return opt_id;
    }

    public void setOpt_id (String opt_id)
    {
        this.opt_id = opt_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [option_value = "+option_value+", type = "+type+", opt_id = "+opt_id+"]";
    }
}
