package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class SdkPostType {
    private ArrayList<SdkPostQuestions> Questions;

    public ArrayList<SdkPostQuestions> getQuestions ()
    {
        return Questions;
    }

    public void setQuestions (ArrayList<SdkPostQuestions> Questions)
    {
        this.Questions = Questions;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Questions = "+Questions+"]";
    }
}
