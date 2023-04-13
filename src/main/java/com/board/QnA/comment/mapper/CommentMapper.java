package com.board.QnA.comment.mapper;

import com.board.QnA.comment.dto.CommentDto;
import com.board.QnA.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "memberId", target = "member.memberId")
    @Mapping(source = "questionId", target = "question.questionId")
    Comment commentPostDtoToComment(CommentDto commentDto);
}
