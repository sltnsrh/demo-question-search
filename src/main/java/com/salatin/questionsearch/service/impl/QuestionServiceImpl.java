package com.salatin.questionsearch.service.impl;

import com.salatin.questionsearch.model.Question;
import com.salatin.questionsearch.model.dto.SimilarQuestion;
import com.salatin.questionsearch.repository.QuestionRepository;
import com.salatin.questionsearch.service.QuestionService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class QuestionServiceImpl implements QuestionService {
    private final static String NOT_SYMBOLS_PATTERN = "[^a-zA-Z\\s]";
    private final static String WHITESPACES_PATTERN = "\\s+";
    private final static String WHITESPACE_SYMBOL = " ";
    private final static short WORD_SIZE_MIN = 3;

    private final QuestionRepository questionRepository;

    @Override
    public List<Question> findTopLongest(int count) {
        return questionRepository.findTopLongestByOrderDesc(count);
    }

    @Override
    public List<Question> findTopSimilar(int count, String question) {
        long timeStarted = System.currentTimeMillis();
        log.info(String.format("Searching for a question: '%s'", question));

        String firstWord = getFirstWord(question);
        List<Question> candidates = questionRepository.findByWordStartingWith(firstWord);
        Queue<SimilarQuestion> similarQuestions = new PriorityQueue<>(Collections.reverseOrder());
        ExecutorService executorService = null;

        try {
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            CompletionService<SimilarQuestion> completionService = new ExecutorCompletionService<>(executorService);

            candidates.forEach(candidate -> completionService.submit(
                () -> {
                    double similarity = calculateSimilarity(question, candidate.getText());
                    return new SimilarQuestion(similarity, candidate);
                })
            );

            gatherSimilarQuestions(similarQuestions, completionService, candidates.size());

        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }

        log.info(String.format("Finished searching for a question: '%s' in %s ms",
            question,
            System.currentTimeMillis() - timeStarted));

        return getSimilarQuestionsLimitedList(similarQuestions, question, count);
    }

    private String getFirstWord(String question) {
        return (question.replaceAll(NOT_SYMBOLS_PATTERN, WHITESPACE_SYMBOL)
                .trim()
                .split(WHITESPACES_PATTERN)[0] + WHITESPACE_SYMBOL)
            .toLowerCase();
    }

    private double calculateSimilarity(String queryQuestion, String candidateQuestion) {
        String[] queryWords = queryQuestion
            .trim()
            .replaceAll(NOT_SYMBOLS_PATTERN, WHITESPACE_SYMBOL)
            .split(WHITESPACES_PATTERN);

        String[] candidateWords = candidateQuestion
            .trim()
            .replaceAll(NOT_SYMBOLS_PATTERN, WHITESPACE_SYMBOL)
            .split(WHITESPACES_PATTERN);

        AtomicInteger totalWords = new AtomicInteger();

        int commonWords = (int) Arrays.stream(queryWords)
            .filter(word1 -> word1.length() > WORD_SIZE_MIN)
            .peek(word -> totalWords.incrementAndGet())
            .flatMap(word1 -> Arrays.stream(candidateWords)
                .filter(word2 -> word2.length() > WORD_SIZE_MIN)
                .filter(word1::equalsIgnoreCase)
            )
            .distinct()
            .count();

        return (double) commonWords / totalWords.get();
    }

    private void gatherSimilarQuestions(Queue<SimilarQuestion> similarQuestions,
                                        CompletionService<SimilarQuestion> completionService,
                                        int numCandidates) {
        for (int i = 0; i < numCandidates; i++) {
            try {
                SimilarQuestion similarQuestion = completionService.take().get();
                if (similarQuestion.getSimilarity() > 0) {
                    similarQuestions.add(similarQuestion);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Question> getSimilarQuestionsLimitedList(Queue<SimilarQuestion> similarQuestions,
                                                          String question,
                                                          int count) {
        if (similarQuestions.isEmpty()) {
            createAndSaveNewQuestion(question);

            return List.of();
        }

        return similarQuestions.stream()
            .map(SimilarQuestion::getQuestion)
            .limit(count)
            .toList();
    }

    private void createAndSaveNewQuestion(String question) {
        Question newQuestion = new Question();
        newQuestion.setText(question);

        questionRepository.save(newQuestion);
    }
}
