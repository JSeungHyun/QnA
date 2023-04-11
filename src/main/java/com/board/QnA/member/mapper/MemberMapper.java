package com.board.QnA.member.mapper;

import com.board.QnA.member.dto.MemberPatchDto;
import com.board.QnA.member.dto.MemberPostDto;
import com.board.QnA.member.dto.MemberResponseDto;
import com.board.QnA.member.entity.Member;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostToMember(MemberPostDto memberPostDto);
    Member memberPatchToMember(MemberPatchDto memberPatchDto);
    MemberResponseDto memberToResponse(Member member);
    List<MemberResponseDto> membersToResponseDtos(List<Member> members);
}
