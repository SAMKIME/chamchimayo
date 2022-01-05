package com.slub.chamchimayo.controller;

import com.slub.chamchimayo.entity.Member;
import com.slub.chamchimayo.repository.MemberRepository;
import com.slub.chamchimayo.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberControllerTest {

    @Autowired
    public MemberService memberService;


    @BeforeEach
    void beforeTest() {
        // given
        Member member1 = new Member("mino", "01012341234", "bewriter310@naver.com");
        Member member2 = new Member("minsu", "01034532342", "minisu@tmax.com");
        Member member3 = new Member("hungry", "01088889999", "baemin@baemin.com");
        memberService.join(member1);
        memberService.join(member2);
        memberService.join(member3);
    }

    @AfterEach
    void afterTest() {
        List<Member> all = memberService.findAll();
        for (Member member : all) {
            memberService.deleteOne(member.getMail());
        }
    }

    @Test
    void createTest() {

        // given

        // when
        Member for_test = new Member("for_test", "111111111", "22222222222");
        memberService.join(for_test);
        List<Member> result = memberService.findAll();

        // then
        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(3).getName()).isEqualTo("for_test");
    }

    @Test
    void readTest() {
        // given
        Member testMember = new Member("read_test", "101010101010", "read_test@naver.com");
        memberService.join(testMember);

        // when
        Member findMember = memberService.findOneByMail("read_test@naver.com").get();

        // then
        assertThat(findMember.getName()).isEqualTo(testMember.getName());
        assertThat(findMember.getPhoneNumber()).isEqualTo(testMember.getPhoneNumber());
        assertThat(findMember.getMail()).isEqualTo(testMember.getMail());

    }

    @Test
    void noMemberReadNseTest() {
//        Member member = memberService.findOneByMail("zzz").get();

        assertThrows(NoSuchElementException.class, () -> memberService.findOneByMail("zzz").get());
    }

    @Test
    void optionalReadTest() {

    }

    @Test
    void updateTest() {
        // given
        // @BeforeEach 로 대체
        Member updatedMember = new Member("updated_test", "0103033030", "bewriter310@naver.com");

        // when
        memberService.update(updatedMember.getName(), updatedMember.getPhoneNumber(), updatedMember.getMail());
        List<Member> all = memberService.findAll();

        // then
        Member findMember = memberService.findOneByMail(updatedMember.getMail()).get();
        assertThat(findMember.getName()).isEqualTo("updated_test");
        assertThat(findMember.getPhoneNumber()).isEqualTo("0103033030");
        assertThat(all.size()).isEqualTo(3);

    }

    @Test
    void deleteTest() {
        // given
        // @BeforeEach 로 대체

        // when
        memberService.deleteOne("bewriter310@naver.com");
        List<Member> all = memberService.findAll();

        // then
        assertThat(all.size()).isEqualTo(2);

    }


}