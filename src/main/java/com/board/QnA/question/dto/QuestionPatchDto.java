package com.board.QnA.question.dto;

import com.board.QnA.question.entity.Question;
import lombok.Getter;

import javax.validation.constraints.Positive;

@Getter
public class QuestionPatchDto {

    @Positive
    private long memberId;
    private String title;
    private String content;
    private Question.QuestionStatus questionStatus;
    private Question.QuestionDisclosure questionDisclosure;
}
