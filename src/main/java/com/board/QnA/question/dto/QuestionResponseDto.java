package com.board.QnA.question.dto;

import com.board.QnA.question.entity.Question;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;

@Getter
@Setter
public class QuestionResponseDto {
    @Positive
    private long questionId;
    private String title;
    private String content;
    private String name;
    private Question.QuestionDisclosure questionDisclosure;
    private Question.QuestionStatus questionStatus;

}
