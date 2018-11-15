package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class SdkPre {

    private ArrayList<SdkQuestions> Questions;

    public ArrayList<SdkQuestions> getQuestions ()
    {
        return Questions;
    }

    public void setQuestions (ArrayList<SdkQuestions> Questions)
    {
        this.Questions = Questions;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Questions = "+Questions+"]";
    }

}
