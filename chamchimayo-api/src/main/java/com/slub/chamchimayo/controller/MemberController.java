package com.slub.chamchimayo.controller;

import com.slub.chamchimayo.domain.Member;
import com.slub.chamchimayo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/new")
    public String create(@RequestBody Member member) {
        Member newMember = new Member(member.getId(), member.getName(), member.getPhoneNumber());
        memberService.join(newMember);
        return "ok";
    }

    @GetMapping("/members")
    public Member select(@RequestBody Member member) {
        Long memberId = member.getId();
        return memberService.findOne(memberId);
    }

    @PostMapping("/members/update")
    public void update(@RequestBody Member member) {
        Long memberId = member.getId();
        memberService.update(memberId, member.getName(), member.getPhoneNumber());
    }

    @PostMapping("/members/delete")
    public void delete(@RequestBody Member member) {
        Long memberId = member.getId();
        memberService.deleteOne(memberId);
    }
}
