package com.board.QnA.member.entity;

import com.board.QnA.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(nullable = false, updatable = false, unique = true)
    private String email;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(length = 13, nullable = false, unique = true)
    private String phone;
    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus status;
    public enum MemberStatus{
        MEMBER_ACTIVE("활동중"), MEMBER_SLEEP("휴면 상태"), MEMBER_QUIT("탈퇴 상태");
        @Getter
        private String status;
        MemberStatus(String status) {
            this.status = status;
        }
    }

}
