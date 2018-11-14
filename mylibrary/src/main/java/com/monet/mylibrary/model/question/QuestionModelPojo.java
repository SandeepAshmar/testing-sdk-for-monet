package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class QuestionModelPojo {

    private QuestionModelPost post;

    private String cmp_eval;

    private String c_duration;

    private String status;

    private QuestionModelPre pre;

    private ArrayList<String> sequence;

    private String c_url;

    private String api_token;

    private String c_length;

    private String code;

    private String cf_id;

    public QuestionModelPost getPost ()
    {
        return post;
    }

    public void setPost (QuestionModelPost post)
    {
        this.post = post;
    }

    public String getCmp_eval ()
    {
        return cmp_eval;
    }

    public void setCmp_eval (String cmp_eval)
    {
        this.cmp_eval = cmp_eval;
    }

    public String getC_duration ()
    {
        return c_duration;
    }

    public void setC_duration (String c_duration)
    {
        this.c_duration = c_duration;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public QuestionModelPre getPre ()
    {
        return pre;
    }

    public void setPre (QuestionModelPre pre)
    {
        this.pre = pre;
    }

    public ArrayList<String> getSequence ()
    {
        return sequence;
    }

    public void setSequence (ArrayList<String> sequence)
    {
        this.sequence = sequence;
    }

    public String getC_url ()
    {
        return c_url;
    }

    public void setC_url (String c_url)
    {
        this.c_url = c_url;
    }

    public String getApi_token ()
    {
        return api_token;
    }

    public void setApi_token (String api_token)
    {
        this.api_token = api_token;
    }

    public String getC_length ()
    {
        return c_length;
    }

    public void setC_length (String c_length)
    {
        this.c_length = c_length;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getCf_id ()
    {
        return cf_id;
    }

    public void setCf_id (String cf_id)
    {
        this.cf_id = cf_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [post = "+post+", cmp_eval = "+cmp_eval+", c_duration = "+c_duration+", status = "+status+", pre = "+pre+", sequence = "+sequence+", c_url = "+c_url+", api_token = "+api_token+", c_length = "+c_length+", code = "+code+", cf_id = "+cf_id+"]";
    }

}
