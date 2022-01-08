package com.slub.chamchimayo.repository;

import com.slub.chamchimayo.entity.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByName(String name);

    Optional<List<Team>> findAllBySports(String sports);

    Optional<List<Team>> findAllByArea(String area);
}
