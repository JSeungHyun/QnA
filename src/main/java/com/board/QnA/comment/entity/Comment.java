package com.board.QnA.comment.entity;

import com.board.QnA.audit.Auditable;
import com.board.QnA.member.entity.Member;
import com.board.QnA.question.entity.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    @Column(nullable = false)
    private String content;
    private String name;
    @OneToOne
    @JoinColumn(name = "QUESTION_ID")
    private Question question;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
