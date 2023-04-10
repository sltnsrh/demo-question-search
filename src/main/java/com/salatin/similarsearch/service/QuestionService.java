package com.salatin.similarsearch.service;

import com.salatin.similarsearch.model.Question;
import java.util.List;

public interface QuestionService {

    List<Question> findTopLongestQuestions(int limit);
}
