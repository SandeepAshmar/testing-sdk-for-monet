package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class QuestionModelPost {

    private ArrayList<QuestionModelPostQuestions> questions;

    public ArrayList<QuestionModelPostQuestions> getQuestions ()
    {
        return questions;
    }

    public void setQuestions (ArrayList<QuestionModelPostQuestions> Questions)
    {
        this.questions = Questions;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Questions = "+questions+"]";
    }


}
