package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class SdkPreType {
    private ArrayList<SdkPreQuestions> Questions;

    public ArrayList<SdkPreQuestions> getQuestions ()
    {
        return Questions;
    }

    public void setQuestions (ArrayList<SdkPreQuestions> Questions)
    {
        this.Questions = Questions;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Questions = "+Questions+"]";
    }
}
