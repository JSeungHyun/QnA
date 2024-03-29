package com.board.QnA.question.service;

import com.board.QnA.comment.entity.Comment;
import com.board.QnA.comment.repository.CommentRepository;
import com.board.QnA.exception.BusinessLogicException;
import com.board.QnA.exception.ExceptionCode;
import com.board.QnA.member.entity.Member;
import com.board.QnA.member.service.MemberService;
import com.board.QnA.question.dto.QuestionCommentResponseDto;
import com.board.QnA.question.entity.Question;
import com.board.QnA.question.mapper.QuestionMapper;
import com.board.QnA.question.repository.QuestionRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
@Service
public class QuestionService {
    @Value("${mail.address.admin}")
    private String adminEmail;
    private final QuestionRepository questionRepository;
    private final MemberService memberService;
    private final CommentRepository commentRepository;


    public QuestionService(QuestionRepository questionRepository, MemberService memberService, CommentRepository commentRepository) {
        this.questionRepository = questionRepository;
        this.memberService = memberService;
        this.commentRepository = commentRepository;
    }

    public Question createPost(Question question){
        Map principal = (Map) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberService.findVerifiedMember((Long) principal.get("memberId"));
        question.getMember().setName(member.getName());
        question.setName(member.getName());
        if (question.getQuestionDisclosure() == null){
            question.setQuestionDisclosure(Question.QuestionDisclosure.PUBLIC);
        }
        return questionRepository.save(question);
    }

    public Question updatePost(Question question){
        // 게시물을 찾기
        Question findPost = verifyQuestion(question.getQuestionId());
        // 작성자와 수정 요청자가 다르고 관리자가 아니면
        if ((question.getMember().getMemberId() == findPost.getMember().getMemberId()) || (question.getMember().getEmail().equals(adminEmail))){
            Optional.ofNullable(question.getTitle()).ifPresent(title -> findPost.setTitle(title));
            Optional.ofNullable(question.getContent()).ifPresent(content -> findPost.setContent(content));
            Optional.ofNullable(question.getQuestionStatus()).ifPresent(status -> findPost.setQuestionStatus(status));
            Optional.ofNullable(question.getQuestionDisclosure()).ifPresent(disclosure -> findPost.setQuestionDisclosure(disclosure));
            return questionRepository.save(findPost);
        }else{
            throw new BusinessLogicException(ExceptionCode.NO_PERMESSION);
        }
    }

    public Question findPost(long questionId, long memberId){
        Question question = verifyQuestion(questionId);
        // 삭제글일때 조회 불가
        if (question.getQuestionStatus().equals(Question.QuestionStatus.QUESTION_DELETED)){
            throw new BusinessLogicException(ExceptionCode.POST_NOT_FOUND);
        }
        // 비밀글일때 관리자와 작성자를 제외하면 조회 불가
        if (question.getQuestionDisclosure().equals(Question.QuestionDisclosure.SECRET)){
            if (question.getMember().getMemberId() != memberId && !memberService.findVerifiedMember(memberId).getEmail().equals(adminEmail)){
                throw new BusinessLogicException(ExceptionCode.NO_PERMESSION);
            }
        }
        return question;
    }

    public Page<Question> findQuestions(int page, int size){
        return questionRepository.findByQuestionStatusNot(Question.QuestionStatus.QUESTION_DELETED, PageRequest.of(page, size));
    }

    public void deletePost(long questionId, long memberId){
        Question findPost = verifyQuestion(questionId);
        if ((findPost.getMember().getMemberId() == memberId) || (memberService.findVerifiedMember(memberId).getEmail().equals(adminEmail))){
            findPost.setQuestionStatus(Question.QuestionStatus.QUESTION_DELETED);
        }else{
            throw new BusinessLogicException(ExceptionCode.NO_PERMESSION);
        }
    }
    public Question verifyQuestion(long questionId) {
        return questionRepository.findById(questionId).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
    }
    public void findComment(QuestionCommentResponseDto dto, long questionId){
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
        Comment comment = commentRepository.findById(question.getComment().getCommentId()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
        dto.setComment_id(comment.getCommentId());
        dto.setCommentTime(comment.getModifiedAt());
        dto.setCommentWriter(comment.getName());
        dto.setCommentContent(comment.getContent());
    }
}
