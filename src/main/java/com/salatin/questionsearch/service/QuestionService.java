package com.salatin.questionsearch.service;

import com.salatin.questionsearch.model.Question;
import java.util.List;

public interface QuestionService {

    List<Question> findTopLongest(int count);

    List<Question> findTopSimilar(int count, String question);
}
