package com.monet.mylibrary.model.sdk;

import java.util.ArrayList;

public class Data {
    private String thumb_url;

    private Pre pre;

    private ArrayList<String> reaction_inputs;

    private String user_response_id;

    private String cmp_name;

    private String token;

    private String cmp_id;

    private ArrayList<String> sequence;

    private String content_type;

    private Post post;

    private String content_url;

    private String user_ex_id;

    private long content_length;

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public Pre getPre() {
        return pre;
    }

    public void setPre(Pre pre) {
        this.pre = pre;
    }

    public ArrayList<String> getReaction_inputs() {
        return reaction_inputs;
    }

    public void setReaction_inputs(ArrayList<String> reaction_inputs) {
        this.reaction_inputs = reaction_inputs;
    }

    public String getUser_response_id() {
        return user_response_id;
    }

    public void setUser_response_id(String user_response_id) {
        this.user_response_id = user_response_id;
    }

    public String getCmp_name() {
        return cmp_name;
    }

    public void setCmp_name(String cmp_name) {
        this.cmp_name = cmp_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCmp_id() {
        return cmp_id;
    }

    public void setCmp_id(String cmp_id) {
        this.cmp_id = cmp_id;
    }

    public ArrayList<String> getSequence() {
        return sequence;
    }

    public void setSequence(ArrayList<String> sequence) {
        this.sequence = sequence;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getUser_ex_id() {
        return user_ex_id;
    }

    public void setUser_ex_id(String user_ex_id) {
        this.user_ex_id = user_ex_id;
    }

    public long getContent_length() {
        return content_length;
    }

    public void setContent_length(long content_length) {
        this.content_length = content_length;
    }

    @Override
    public String toString() {
        return "ClassPojo [thumb_url = " + thumb_url + ", pre = " + pre + ", reaction_inputs = " + reaction_inputs + ", user_response_id = " + user_response_id + ", cmp_name = " + cmp_name + ", token = " + token + ", cmp_id = " + cmp_id + ", sequence = " + sequence + ", content_type = " + content_type + ", post = " + post + ", content_url = " + content_url + ", user_ex_id = " + user_ex_id + ", content_length = " + content_length + "]";
    }
}