package com.board.QnA.comment.controller;

import com.board.QnA.comment.dto.CommentDto;
import com.board.QnA.comment.entity.Comment;
import com.board.QnA.comment.mapper.CommentMapper;
import com.board.QnA.comment.service.CommentService;
import com.board.QnA.response.SingleResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/comments/{question-id}")
@Valid
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper mapper;

    public CommentController(CommentService commentService, CommentMapper mapper) {
        this.commentService = commentService;
        this.mapper = mapper;
    }
    @PostMapping
    public ResponseEntity postComment(@RequestBody @Valid CommentDto commentDto,
                                      @PathVariable("question-id") long questionId){
        Comment comment = mapper.commentPostDtoToComment(commentDto);
        commentService.createComment(comment, questionId);

        return ResponseEntity.created(URI.create("/questions/" + questionId)).build();
    }
    @PatchMapping
    public ResponseEntity patchComment(@RequestBody @Valid CommentDto commentDto,
                                       @PathVariable("question-id") long questionId){
        Comment comment = mapper.commentPostDtoToComment(commentDto);
        commentService.updateComment(comment, questionId);

        return ResponseEntity.created(URI.create("/questions/" + questionId)).build();
    }

    @DeleteMapping ResponseEntity deleteComment(@PathVariable("question-id") long questionId){
        commentService.removeComment(questionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
