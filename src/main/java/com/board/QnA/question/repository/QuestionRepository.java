package com.board.QnA.question.repository;

import com.board.QnA.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findByQuestionStatusNot(Question.QuestionStatus questionStatus, Pageable pageable);
}
