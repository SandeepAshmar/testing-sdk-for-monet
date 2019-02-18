package com.monet.mylibrary.model;

public class Item {

    String reactionName;
    String ImageUrl;

    public Item(String reactionName, String imageUrl) {
        this.reactionName = reactionName;
        ImageUrl = imageUrl;
    }

    public String getReactionName() {
        return reactionName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

}
