package com.board.QnA.comment.dto;

import lombok.Getter;

import javax.persistence.Column;

@Getter
public class CommentDto {
    private long memberId;
    private long questionId;
    @Column(nullable = false)
    private String content;
}
