package com.board.QnA.comment.service;

import com.board.QnA.comment.entity.Comment;
import com.board.QnA.comment.mapper.CommentMapper;
import com.board.QnA.comment.repository.CommentRepository;
import com.board.QnA.exception.BusinessLogicException;
import com.board.QnA.exception.ExceptionCode;
import com.board.QnA.member.entity.Member;
import com.board.QnA.member.repository.MemberRepository;
import com.board.QnA.question.entity.Question;
import com.board.QnA.question.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final QuestionService questionService;
    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository, QuestionService questionService, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.questionService = questionService;
        this.memberRepository = memberRepository;
    }

    public Comment createComment(Comment comment, long boardId){
        Question question = questionService.verifyQuestion(boardId);
        if (question.getQuestionStatus().equals(Question.QuestionStatus.QUESTION_REGISTERED)) {
            Member member = memberRepository.findById(question.getMember().getMemberId()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
            comment.setName(member.getName());
            question.setComment(comment);
            question.setQuestionStatus(Question.QuestionStatus.QUESTION_ANSWERED);
            return commentRepository.save(comment);

        }else{
            throw new BusinessLogicException(ExceptionCode.ALREADY_COMPLETED);
        }
    }

    public void updateComment(Comment comment, long boardId){
        Question question = questionService.verifyQuestion(boardId);
        if (!question.getQuestionStatus().equals(Question.QuestionStatus.QUESTION_ANSWERED)){
            throw new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND);
        }
        Comment result = commentRepository.findById(question.getComment().getCommentId()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
        result.setContent(comment.getContent());
    }

    public void removeComment(long boardId){
        Question question = questionService.verifyQuestion(boardId);
        question.setQuestionStatus(Question.QuestionStatus.QUESTION_REGISTERED);
        question.setComment(null);
        commentRepository.deleteById(boardId);
    }
    private String findAdminName() {
        List<Member> admin = memberRepository.findByRolesContaining("ADMIN");
        if (admin.size() == 1) {
            return admin.get(0).getName();
        } else {
            throw new RuntimeException("There are multiple administrators or no administrator found!");
        }
    }
}
