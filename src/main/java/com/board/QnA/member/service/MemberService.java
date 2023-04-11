package com.board.QnA.member.service;

import com.board.QnA.auth.utils.CustomAuthorityUtils;
import com.board.QnA.exception.BusinessLogicException;
import com.board.QnA.exception.ExceptionCode;
import com.board.QnA.member.entity.Member;
import com.board.QnA.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, CustomAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
    }

    public Member createMember(Member member){
        verifyExistsEmail(member.getEmail());
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRoles(authorityUtils.createAuthorities(member.getEmail()));

        return memberRepository.save(member);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Member updateMember(Member member){
        Member findMember = findVerifiedMember(member.getMemberId());
        Optional.ofNullable(member.getMemberId()).ifPresent(id -> findMember.setMemberId(id));
        Optional.ofNullable(member.getName()).ifPresent(name -> findMember.setName(name));
        Optional.ofNullable(member.getPhone()).ifPresent(phone -> findMember.setPhone(phone));
        Optional.ofNullable(member.getStatus()).ifPresent(status -> findMember.setStatus(status));

        return memberRepository.save(findMember);
    }

    public Member getMember(long memberId){
        return findVerifiedMember(memberId);
    }

    public void deleteMember(long memberId) {
        Member findMember = findVerifiedMember(memberId);
        memberRepository.delete(findMember);
    }

    public Page<Member> findMembers(int page, int size) {
        return memberRepository.findAll(PageRequest.of(page, size, Sort.by("memberId").descending()));
    }
    public Member findVerifiedMember(long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        Member member = findMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return member;
    }

    private void verifyExistsEmail(String email){
        memberRepository.findByEmail(email).ifPresent(e -> {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        });
    }
}
