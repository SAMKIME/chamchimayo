package com.slub.chamchimayo.controller;

import com.slub.chamchimayo.dto.MemberCrudDto;
import com.slub.chamchimayo.entity.Member;
import com.slub.chamchimayo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/new")
    public String create(@RequestBody MemberCrudDto memberDto) {

        try {
            memberService.join(new Member(memberDto.getName(), memberDto.getPhoneNumber(), memberDto.getMail()));
        } catch (IllegalStateException e) {
            return "이미 존재하는 회원입니다.";
        }
        return "created";
    }

    @GetMapping("/members")
    public Optional<Member> select(@RequestBody MemberCrudDto memberDto) {
        return memberService.findOneByMail(memberDto.getMail());
    }

    @PostMapping("/members/update")
    public String update(@RequestBody MemberCrudDto memberDto) {
        try {
            memberService.update(memberDto.getName(), memberDto.getPhoneNumber(), memberDto.getMail());
        } catch (NoSuchElementException e) {
            return "존재하지 않는 회원입니다";
        }
        return "updated";
    }

    @PostMapping("/members/delete")
    public String delete(@RequestBody MemberCrudDto memberDto) {
        try {
            memberService.deleteOne(memberDto.getMail());
        } catch (NoSuchElementException e) {
            return "존재하지 않는 회원입니다";
        }
        return "deleted";
    }
}
