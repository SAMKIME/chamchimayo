package com.slub.chamchimayo.repository;

import com.slub.chamchimayo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
