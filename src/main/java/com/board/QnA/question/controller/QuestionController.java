package com.board.QnA.question.controller;

import com.board.QnA.question.dto.QuestionPatchDto;
import com.board.QnA.question.dto.QuestionPostDto;
import com.board.QnA.question.entity.Question;
import com.board.QnA.question.mapper.QuestionMapper;
import com.board.QnA.question.repository.QuestionRepository;
import com.board.QnA.question.service.QuestionService;
import com.board.QnA.response.MultiResponseDto;
import com.board.QnA.response.SingleResponseDto;
import com.board.QnA.utils.URiCreator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/question")
@Validated
public class QuestionController {
    private final static String QUESTION_DEFAULT_URL = "/question";
    private final QuestionService questionService;

    private final QuestionMapper mapper;

    public QuestionController(QuestionService questionService, QuestionMapper mapper) {
        this.questionService = questionService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postQuestion(@RequestBody @Valid QuestionPostDto questionPostDto){
        Question question = mapper.questionPostToQuestion(questionPostDto);
        Question post = questionService.createPost(question);
        URI uri = URiCreator.createUri(QUESTION_DEFAULT_URL, post.getQuestionId());
        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/{question-id}")
    public ResponseEntity patchQuestion(@PathVariable("question-id") @Positive long questionId,
                                        @Valid @RequestBody QuestionPatchDto questionPatchDto){
        Question question = mapper.questionPatchDtoToQuestion(questionPatchDto);
        question.setQuestionId(questionId);
        Question result = questionService.updatePost(question);
        return new ResponseEntity(new SingleResponseDto<>(mapper.questionToResponseDto(result)), HttpStatus.OK);
    }

    @GetMapping("/{question-id}/{member-id}")
    public ResponseEntity getQuestion(@PathVariable("question-id") @Positive long questionId,
                                      @PathVariable("member-id") @Positive long memberId){
        Question post = questionService.findPost(questionId, memberId);
        return new ResponseEntity(new SingleResponseDto<>(mapper.questionToResponseDto(post)), HttpStatus.OK);
    }

    @DeleteMapping("/{question-id}/{member-id}")
    public ResponseEntity deleteQuestion(@PathVariable("question-id") @Positive long questionId,
                                         @PathVariable("member-id") @Positive long memberId) {
        questionService.deletePost(questionId, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity getQuestions(@Positive @RequestParam int page,
                                        @Positive @RequestParam int size) {
        Page<Question> questions = questionService.findQuestions(page - 1, size);
        List<Question> content = questions.getContent();

        return new ResponseEntity(new MultiResponseDto<>(mapper.questionsToQuestionDtos(content), questions), HttpStatus.OK);
    }

}
