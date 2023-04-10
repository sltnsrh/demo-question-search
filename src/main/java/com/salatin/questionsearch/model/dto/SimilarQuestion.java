package com.salatin.questionsearch.model.dto;

import com.salatin.questionsearch.model.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SimilarQuestion implements Comparable<SimilarQuestion> {
    private double similarity;
    private Question question;

    @Override
    public int compareTo(SimilarQuestion o) {
        return Double.compare(this.similarity, o.similarity);
    }
}
