package com.board.QnA.member.controller;

import com.board.QnA.member.dto.MemberPatchDto;
import com.board.QnA.member.dto.MemberPostDto;
import com.board.QnA.member.entity.Member;
import com.board.QnA.member.mapper.MemberMapper;
import com.board.QnA.member.service.MemberService;
import com.board.QnA.response.MultiResponseDto;
import com.board.QnA.response.SingleResponseDto;
import com.board.QnA.utils.URiCreator;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/members")
@Validated
@Slf4j
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/members";
    private final MemberMapper mapper;
    private final MemberService memberService;

    public MemberController(MemberMapper mapper, MemberService memberService) {
        this.mapper = mapper;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity postMember(@Valid @RequestBody MemberPostDto memberPostDto){
        Member member = mapper.memberPostToMember(memberPostDto);
        Member serviceMember = memberService.createMember(member);
        URI uri = URiCreator.createUri(MEMBER_DEFAULT_URL, serviceMember.getMemberId());

        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@PathVariable("member-id") long memberId,
                                      @Valid @RequestBody MemberPatchDto memberPatchDto){
        memberPatchDto.setMemberId(memberId);
        Member member = memberService.updateMember(mapper.memberPatchToMember(memberPatchDto));

        return new ResponseEntity(new SingleResponseDto<>(mapper.memberToResponse(member)), HttpStatus.OK);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive long memberId){
        Member verifiedMember = memberService.getMember(memberId);
        return new ResponseEntity<>(new SingleResponseDto<>(mapper.memberToResponse(verifiedMember)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getMembers(@Positive @RequestParam int page,
                                     @Positive @RequestParam int size) {
        Page<Member> pageMembers = memberService.findMembers(page - 1, size);
        List<Member> members = pageMembers.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.membersToResponseDtos(members), pageMembers), HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(
            @PathVariable("member-id") @Positive long memberId) {
        memberService.deleteMember(memberId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
