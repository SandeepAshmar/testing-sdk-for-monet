package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class SdkPost {

    private ArrayList<SdkPostQuestion> Questions;

    public ArrayList<SdkPostQuestion> getQuestions ()
    {
        return Questions;
    }

    public void setQuestions (ArrayList<SdkPostQuestion> Questions)
    {
        this.Questions = Questions;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Questions = "+Questions+"]";
    }

}
