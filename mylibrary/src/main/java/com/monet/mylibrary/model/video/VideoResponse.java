package com.monet.mylibrary.model.video;

public class VideoResponse {
    private String url;

    private String quality;

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    public String getQuality ()
    {
        return quality;
    }

    public void setQuality (String quality)
    {
        this.quality = quality;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [url = "+url+", quality = "+quality+"]";
    }
}
