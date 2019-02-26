package com.monet.mylibrary.model.video;

import java.util.ArrayList;

public class VideoPojo {

    private String code;

    private ArrayList<VideoResponse> response;

    private String message;

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public ArrayList<VideoResponse> getResponse ()
    {
        return response;
    }

    public void setResponse (ArrayList<VideoResponse> response)
    {
        this.response = response;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [code = "+code+", response = "+response+", message = "+message+"]";
    }

}
