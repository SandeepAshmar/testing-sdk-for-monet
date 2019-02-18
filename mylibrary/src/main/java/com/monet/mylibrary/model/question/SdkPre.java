package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class SdkPre {

    private ArrayList<SdkPreQuestion> Questions;

    public ArrayList<SdkPreQuestion> getQuestions ()
    {
        return Questions;
    }

    public void setQuestions (ArrayList<SdkPreQuestion> Questions)
    {
        this.Questions = Questions;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Questions = "+Questions+"]";
    }

}
