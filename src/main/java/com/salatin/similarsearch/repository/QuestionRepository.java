package com.salatin.similarsearch.repository;

import com.salatin.similarsearch.model.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q ORDER BY LENGTH(q.text) DESC")
    List<Question> findTopLongestByOrderDesc(int limit);
}
