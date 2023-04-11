package com.board.QnA.question.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
public class QuestionPostDto {

    @Positive
    private Long memberId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private String questionDisclosure;

}
