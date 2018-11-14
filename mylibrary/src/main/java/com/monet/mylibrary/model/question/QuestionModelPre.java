package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class QuestionModelPre {

    private ArrayList<QuestionModelPreQuestions> questions;

    public ArrayList<QuestionModelPreQuestions> getQuestions ()
    {
        return questions;
    }

    public void setQuestions (ArrayList<QuestionModelPreQuestions> Questions)
    {
        this.questions = Questions;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Questions = "+questions+"]";
    }

}
