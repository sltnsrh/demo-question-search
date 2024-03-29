package com.salatin.questionsearch.repository;

import com.salatin.questionsearch.model.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT * FROM questions q ORDER BY LENGTH(q.text) DESC LIMIT :count",
        nativeQuery = true)
    List<Question> findTopLongestByOrderDesc(@Param("count") int count);

    @Query(value = "FROM Question q WHERE LOWER(q.text) LIKE :word%")
    List<Question> findByWordStartingWith(String word);
}
