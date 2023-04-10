package com.salatin.similarsearch.service.impl;

import com.salatin.similarsearch.model.Question;
import com.salatin.similarsearch.repository.QuestionRepository;
import com.salatin.similarsearch.service.QuestionService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final static String NOT_SYMBOLS_PATTERN = "[^a-zA-Z\\s]";
    private final static String WHITESPACES_PATTERN = "\\s+";

    private final QuestionRepository questionRepository;

    @Override
    public List<Question> findTopLongest(int count) {
        return questionRepository.findTopLongestByOrderDesc(count);
    }

    @Override
    public List<Question> findTopSimilar(int count, String question) {
        String firstWord = (
            question.replaceAll(NOT_SYMBOLS_PATTERN, WHITESPACES_PATTERN)
            .trim()
            .split(WHITESPACES_PATTERN)[0] + " "
        )
            .toLowerCase();

        List<Question> candidates = questionRepository.findByWordStartingWith(firstWord);

        TreeMap<Double, Question> similarityMap = new TreeMap<>(Collections.reverseOrder());

        candidates.forEach(candidate ->
            similarityMap.put(
                calculateSimilarity(question, candidate.getText()),
                candidate)
        );

        if (!similarityMap.isEmpty() && similarityMap.firstKey() > 0) {
            return similarityMap.entrySet().stream()
                .filter(entry -> entry.getKey() > 0)
                .map(Map.Entry::getValue)
                .limit(count)
                .toList();
        }

        Question newQuestion = new Question();
        newQuestion.setText(question);

        questionRepository.save(newQuestion);

        return List.of();
    }

    private double calculateSimilarity(String queryQuestion, String candidateQuestion) {
        String[] queryWords = queryQuestion
            .trim()
            .replaceAll(NOT_SYMBOLS_PATTERN, " ")
            .split(WHITESPACES_PATTERN);

        String[] candidateWords = candidateQuestion
            .trim()
            .replaceAll(NOT_SYMBOLS_PATTERN, " ")
            .split(WHITESPACES_PATTERN);

        int commonWords = 0;
        int totalWords = 0;

        for (String word1 : queryWords) {
            if (word1.length() > 3) {
                totalWords++;

                for (String word2 : candidateWords) {
                    if (word2.length() > 3 && word1.equalsIgnoreCase(word2)) {
                        commonWords++;
                        break;
                    }
                }
            }
        }

        return (double) commonWords / totalWords;
    }
}
