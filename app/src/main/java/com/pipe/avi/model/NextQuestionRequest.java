package com.pipe.avi.model;

public class NextQuestionRequest {

    public RiasecScores riasec_scores;
    public String session_id;

    public NextQuestionRequest(RiasecScores scores, String sessionId) {
        this.riasec_scores = scores;
        this.session_id = sessionId;
    }
}
