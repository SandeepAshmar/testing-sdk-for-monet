package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class SdkData {
    private String c_duration;

    private String c_length;

    private String cmp_eval;

    private ArrayList<String> sequence;

    private SdkPre pre;

    private String c_url;

    private SdkPost post;

    private String cf_id;

    private String api_token;

    public String getC_duration ()
    {
        return c_duration;
    }

    public void setC_duration (String c_duration)
    {
        this.c_duration = c_duration;
    }

    public String getC_length ()
    {
        return c_length;
    }

    public void setC_length (String c_length)
    {
        this.c_length = c_length;
    }

    public String getCmp_eval ()
    {
        return cmp_eval;
    }

    public void setCmp_eval (String cmp_eval)
    {
        this.cmp_eval = cmp_eval;
    }

    public ArrayList<String> getSequence ()
    {
        return sequence;
    }

    public void setSequence (ArrayList<String> sequence)
    {
        this.sequence = sequence;
    }

    public SdkPre getPre ()
    {
        return pre;
    }

    public void setPre (SdkPre pre)
    {
        this.pre = pre;
    }

    public String getC_url ()
    {
        return c_url;
    }

    public void setC_url (String c_url)
    {
        this.c_url = c_url;
    }

    public SdkPost getPost ()
    {
        return post;
    }

    public void setPost (SdkPost post)
    {
        this.post = post;
    }

    public String getCf_id ()
    {
        return cf_id;
    }

    public void setCf_id (String cf_id)
    {
        this.cf_id = cf_id;
    }

    public String getApi_token ()
    {
        return api_token;
    }

    public void setApi_token (String api_token)
    {
        this.api_token = api_token;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [c_duration = "+c_duration+", c_length = "+c_length+", cmp_eval = "+cmp_eval+", sequence = "+sequence+", pre = "+pre+", c_url = "+c_url+", post = "+post+", cf_id = "+cf_id+", api_token = "+api_token+"]";
    }
}
