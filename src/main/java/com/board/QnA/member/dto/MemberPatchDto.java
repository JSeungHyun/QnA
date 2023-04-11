package com.board.QnA.member.dto;

import com.board.QnA.member.entity.Member;
import lombok.Getter;


@Getter
public class MemberPatchDto {

    private long memberId;
    private String name;
    private String phone;
    private Member.MemberStatus status;

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }
}
