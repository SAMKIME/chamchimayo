package com.slub.chamchimayo.repository;

import com.slub.chamchimayo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findByMail(String mail);
}
