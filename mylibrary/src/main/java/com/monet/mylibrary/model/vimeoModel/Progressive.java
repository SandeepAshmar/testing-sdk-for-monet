package com.monet.mylibrary.model.vimeoModel;

public class Progressive {
    private String id;

    private String height;

    private String cdn;

    private String fps;

    private String width;

    private String origin;

    private String quality;

    private String mime;

    private String url;

    private String profile;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getHeight ()
    {
        return height;
    }

    public void setHeight (String height)
    {
        this.height = height;
    }

    public String getCdn ()
    {
        return cdn;
    }

    public void setCdn (String cdn)
    {
        this.cdn = cdn;
    }

    public String getFps ()
    {
        return fps;
    }

    public void setFps (String fps)
    {
        this.fps = fps;
    }

    public String getWidth ()
    {
        return width;
    }

    public void setWidth (String width)
    {
        this.width = width;
    }

    public String getOrigin ()
    {
        return origin;
    }

    public void setOrigin (String origin)
    {
        this.origin = origin;
    }

    public String getQuality ()
    {
        return quality;
    }

    public void setQuality (String quality)
    {
        this.quality = quality;
    }

    public String getMime ()
    {
        return mime;
    }

    public void setMime (String mime)
    {
        this.mime = mime;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    public String getProfile ()
    {
        return profile;
    }

    public void setProfile (String profile)
    {
        this.profile = profile;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", height = "+height+", cdn = "+cdn+", fps = "+fps+", width = "+width+", origin = "+origin+", quality = "+quality+", mime = "+mime+", url = "+url+", profile = "+profile+"]";
    }

}
