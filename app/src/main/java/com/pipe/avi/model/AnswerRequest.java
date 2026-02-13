package com.pipe.avi.model;

public class AnswerRequest {

    public String category;
    public int answer;
    public RiasecScores riasec_scores;

    public AnswerRequest(String category, int answer, RiasecScores scores) {
        this.category = category;
        this.answer = answer;
        this.riasec_scores = scores;
    }
}
