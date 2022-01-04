package com.slub.chamchimayo.service;

import com.slub.chamchimayo.domain.Member;
import com.slub.chamchimayo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EntityManager em;

    // 회원 저장
    public void join(Member member) {
        memberRepository.save(member);
    }

    // 회원 조회
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    // 회원 이름 수정
    @Transactional
    public void update(Long memberId, String name, String phoneNumber) {
        Member member = memberRepository.findById(memberId).get();
        member.setName(name);
        member.setPhoneNumber(phoneNumber);
    }

    // 회원 삭제
    public void deleteOne(Long memberId) {
        Member memberToDelete = memberRepository.findById(memberId).get();
        memberRepository.delete(memberToDelete);
    }

}
