package com.pipe.avi.model;

public class NextQuestionRequest {

    private int testId;
    private RiasecScores riasec_scores;
    private String session_id;

    public NextQuestionRequest(int testId, RiasecScores riasec_scores, String session_id) {
        this.testId = testId;
        this.riasec_scores = riasec_scores;
        this.session_id = session_id;
    }
}
