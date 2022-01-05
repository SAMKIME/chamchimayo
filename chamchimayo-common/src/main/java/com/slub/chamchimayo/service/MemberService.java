package com.slub.chamchimayo.service;

import com.slub.chamchimayo.entity.Member;
import com.slub.chamchimayo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 저장
    public void join(Member member) {
        Optional<Member> result = memberRepository.findByMail(member.getMail());

        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        });

        memberRepository.save(member);
    }

    // 회원 단건 조회
    public Optional<Member> findOneByMail(String mail) {
        return memberRepository.findByMail(mail);
    }

    // 회원 전체 조회
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    // 회원 이름 및 번호 수정
    @Transactional
    public void update(String name, String phoneNumber, String mail) {
        Member member = memberRepository.findByMail(mail).get();
        member.setName(name);
        member.setPhoneNumber(phoneNumber);
    }

    // 회원 삭제
    public void deleteOne(String mail) {
        Member memberToDelete = memberRepository.findByMail(mail).get();
        memberRepository.delete(memberToDelete);
    }

}
