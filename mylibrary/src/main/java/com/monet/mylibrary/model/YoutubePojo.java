package com.monet.mylibrary.model;

public class YoutubePojo {

    private String quality;

    private String type;

    private String itag;

    private String url;

    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItag() {
        return itag;
    }

    public void setItag(String itag) {
        this.itag = itag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ClassPojo [quality = " + quality + ", type = " + type + ", itag = " + itag + ", url = " + url + "]";
    }
}
