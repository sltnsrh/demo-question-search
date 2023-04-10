package com.salatin.similarsearch.service.impl;

import com.salatin.similarsearch.model.Question;
import com.salatin.similarsearch.repository.QuestionRepository;
import com.salatin.similarsearch.service.QuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    @Override
    public List<Question> findTopLongestQuestions(int count) {
        return questionRepository.findTopLongestByOrderDesc(count);
    }
}
