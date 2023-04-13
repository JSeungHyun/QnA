package com.board.QnA.question.entity;

import com.board.QnA.audit.Auditable;
import com.board.QnA.comment.entity.Comment;
import com.board.QnA.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Question extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    private String name;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private QuestionStatus questionStatus = QuestionStatus.QUESTION_REGISTERED;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private QuestionDisclosure questionDisclosure;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToOne
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;



    public enum QuestionStatus{
        QUESTION_REGISTERED("질문 등록 상태"),
        QUESTION_ANSWERED("답변 완료 상태"),
        QUESTION_DELETED("질문 삭제 상태");

        @Getter
        private String question_status;

        QuestionStatus(String question_status) {
            this.question_status = question_status;
        }
    }

    public enum QuestionDisclosure{
        PUBLIC("공개글"),
        SECRET("비밀글");

        @Getter
        private String question_disclosure;

        QuestionDisclosure(String question_disclosure) {
            this.question_disclosure = question_disclosure;
        }
    }
}
