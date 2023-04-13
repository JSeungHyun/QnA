package com.board.QnA.question.dto;

import com.board.QnA.comment.entity.Comment;
import com.board.QnA.question.entity.Question;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
public class QuestionCommentResponseDto {
    @Positive
    private long questionId;
    private String title;
    private String content;
    private String name;
    private Question.QuestionDisclosure questionDisclosure;
    private Question.QuestionStatus questionStatus;
    private long comment_id;
    private String commentWriter;
    private String commentContent;
    private LocalDateTime commentTime;

}