package com.monet.mylibrary.model.question;

import java.util.ArrayList;

public class SdkQuestions {
    private String survey_id;

    private ArrayList<SdkOptions> Options;

    private String question;

    private String question_type;

    private String question_status;

    private String id;

    private String created_date;

    private String question_id;

    private String created_by;

    private String radio_type;

    public String getSurvey_id ()
    {
        return survey_id;
    }

    public void setSurvey_id (String survey_id)
    {
        this.survey_id = survey_id;
    }

    public ArrayList<SdkOptions> getOptions ()
    {
        return Options;
    }

    public void setOptions (ArrayList<SdkOptions> Options)
    {
        this.Options = Options;
    }

    public String getQuestion ()
    {
        return question;
    }

    public void setQuestion (String question)
    {
        this.question = question;
    }

    public String getQuestion_type ()
    {
        return question_type;
    }

    public void setQuestion_type (String question_type)
    {
        this.question_type = question_type;
    }

    public String getQuestion_status ()
    {
        return question_status;
    }

    public void setQuestion_status (String question_status)
    {
        this.question_status = question_status;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getCreated_date ()
    {
        return created_date;
    }

    public void setCreated_date (String created_date)
    {
        this.created_date = created_date;
    }

    public String getQuestion_id ()
    {
        return question_id;
    }

    public void setQuestion_id (String question_id)
    {
        this.question_id = question_id;
    }

    public String getCreated_by ()
    {
        return created_by;
    }

    public void setCreated_by (String created_by)
    {
        this.created_by = created_by;
    }

    public String getRadio_type ()
    {
        return radio_type;
    }

    public void setRadio_type (String radio_type)
    {
        this.radio_type = radio_type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [survey_id = "+survey_id+", Options = "+Options+", question = "+question+", question_type = "+question_type+", question_status = "+question_status+", id = "+id+", created_date = "+created_date+", question_id = "+question_id+", created_by = "+created_by+", radio_type = "+radio_type+"]";
    }
}
