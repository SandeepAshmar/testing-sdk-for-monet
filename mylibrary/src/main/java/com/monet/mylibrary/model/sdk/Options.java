package com.monet.mylibrary.model.sdk;

public class Options
{
    private String option_value;

    private String type;

    private String opt_id;

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
