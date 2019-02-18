package com.monet.mylibrary.model.question;

public class SdkPojo {

    private String code;

    private String Message;

    private SdkData data;

    private String status;

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getMessage ()
    {
        return Message;
    }

    public void setMessage (String Message)
    {
        this.Message = Message;
    }

    public SdkData getData ()
    {
        return data;
    }

    public void setData (SdkData data)
    {
        this.data = data;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [code = "+code+", Message = "+Message+", data = "+data+", status = "+status+"]";
    }

}
