package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class QuestionModelPre {

    private ArrayList<QuestionModelQuestions> questions;

    public ArrayList<QuestionModelQuestions> getQuestions ()
    {
        return questions;
    }

    public void setQuestions (ArrayList<QuestionModelQuestions> Questions)
    {
        this.questions = Questions;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Questions = "+questions+"]";
    }

}
