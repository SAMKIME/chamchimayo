package com.slub.chamchimayo.controller;

import com.slub.chamchimayo.dto.MemberRequest;
import com.slub.chamchimayo.dto.MemberResponse;
import com.slub.chamchimayo.entity.Member;
import com.slub.chamchimayo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    
    private final MemberService memberService;

    @PostMapping("/new")
    public ResponseEntity<Member> create(@RequestBody final MemberRequest memberRequest) {
        memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" )).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<MemberResponse>> findAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    @GetMapping
    public ResponseEntity<MemberResponse> select(@RequestBody MemberRequest memberRequest) {
        return ResponseEntity.ok(memberService.findOneByMail(memberRequest.getMail()));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> update(@RequestBody MemberRequest memberRequest) {
        memberService.update(memberRequest.getName(), memberRequest.getPhoneNumber(), memberRequest.getMail());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestBody MemberRequest memberRequest) {
        memberService.deleteOne(memberRequest.getMail());
        return ResponseEntity.noContent().build();
    }
}
