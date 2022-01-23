package com.slub.chamchimayo.service;

import com.slub.chamchimayo.entity.Team;
import com.slub.chamchimayo.dto.request.TeamRequest;
import com.slub.chamchimayo.dto.response.TeamResponse;
import com.slub.chamchimayo.exception.DuplicatedTeamNameException;
import com.slub.chamchimayo.exception.TeamNotFoundException;
import com.slub.chamchimayo.repository.TeamRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamResponse create(TeamRequest teamRequest) {

        teamRepository.findByName(teamRequest.getName())
            .ifPresent(selectTeam -> {
                throw new DuplicatedTeamNameException("중복된 팀명 입니다.");
            });

        Team newTeam = teamRepository.save(teamRequest.toEntity());
        return TeamResponse.from(newTeam);
    }

    public TeamResponse searchById(Long id) {

        Optional<Team> team = teamRepository.findById(id);
        team.orElseThrow(() -> new TeamNotFoundException("존재하는 팀이 없습니다."));

        return TeamResponse.from(team.get());
    }

    public List<TeamResponse> searchTeam(Specification<Team> spec) {

        List<Team> teamList = teamRepository.findAll(spec);
        if (teamList.isEmpty())
            throw (new TeamNotFoundException("존재하는 팀이 없습니다."));

        return teamList
            .stream()
            .map(TeamResponse::from)
            .collect(Collectors.toList());
    }

    public void updateTeamName(TeamRequest teamRequest) {

        teamRepository.findById(teamRequest.getId())
            .map(team -> {
                team.updateTeamName(teamRequest);
                return team;
            })
            .map(team -> teamRepository.save(team))
            .map(team -> TeamResponse.from(team));
    }

    public void delete(Long id) {

        Optional<Team> team = teamRepository.findById(id);

        teamRepository.delete(team.get());

    }
}
