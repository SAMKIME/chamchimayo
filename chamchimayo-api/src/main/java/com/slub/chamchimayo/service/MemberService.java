package com.slub.chamchimayo.service;


import com.slub.chamchimayo.dto.MemberRequest;
import com.slub.chamchimayo.dto.MemberResponse;
import com.slub.chamchimayo.entity.Member;
import com.slub.chamchimayo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse create(MemberRequest memberRequest) {
        Optional<Member> findMembers = memberRepository.findByMail(memberRequest.getMail());
        findMembers.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        });
        Member saveMember = Member.builder()
                .name(memberRequest.getName())
                .phoneNumber(memberRequest.getPhoneNumber())
                .mail(memberRequest.getMail())
                .build();
        memberRepository.save(saveMember);

        return MemberResponse.builder()
                .name(saveMember.getName())
                .phoneNumber(saveMember.getPhoneNumber())
                .mail(saveMember.getMail())
                .build();
    }

    public MemberResponse findOneByMail(String mail) {
        Member findMember = memberRepository.findByMail(mail).get();
        return MemberResponse.builder()
                .name(findMember.getName())
                .phoneNumber(findMember.getPhoneNumber())
                .mail(findMember.getMail())
                .build();
    }

    public List<MemberResponse> findAll() {
        List<Member> findAllMembers = memberRepository.findAll();
        List<MemberResponse> responseMembers = new ArrayList<>();
        for (Member findMember : findAllMembers) {
            responseMembers.add(
                    MemberResponse.builder()
                            .name(findMember.getName())
                            .phoneNumber(findMember.getPhoneNumber())
                            .mail(findMember.getMail())
                            .build()
            );
        }
        return responseMembers;
    }

    public void update(String name, String phoneNumber, String mail) {
        try {
            Member member = memberRepository.findByMail(mail).get();
            member.setNameAndPhoneNumber(name, phoneNumber);
        } catch (NoSuchElementException e) {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }
    }

    public void deleteOne(String mail) {
        Member memberToDelete = memberRepository.findByMail(mail).get();
        memberRepository.delete(memberToDelete);
    }
}
