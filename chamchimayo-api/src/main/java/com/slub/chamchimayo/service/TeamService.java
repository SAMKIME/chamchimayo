package com.slub.chamchimayo.service;

import com.slub.chamchimayo.entity.Team;
import com.slub.chamchimayo.network.Header;
import com.slub.chamchimayo.network.request.TeamApiRequest;
import com.slub.chamchimayo.network.response.TeamApiResponse;
import com.slub.chamchimayo.repository.TeamRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public Header<TeamApiResponse> create(Header<TeamApiRequest> request) {

        TeamApiRequest teamApiRequest = request.getData();

        Optional<Team> team = teamRepository.findByName(teamApiRequest.getName());
        team.ifPresent(selectTeam -> {
            Header.ERROR("중복된 팀명 입니다.");
        });

        Team newTeam = teamRepository.save(teamApiRequest.toEntity());
        return response(newTeam);
    }

    public Header<TeamApiResponse> searchByName(String name) {

        return teamRepository.findByName(name)
            .map(team -> response(team))
            .orElseGet(
                () -> Header.ERROR("조건에 맞는 팀이 없습니다.")
            );
    }

    public Header<List<Team>> searchByArea(String area) {

        return teamRepository.findAllByArea(area)
            .map(team -> Header.OK(team))
            .orElseGet(
                () -> Header.ERROR("조건에 맞는 팀이 없습니다.")
            );
    }

    public Header<List<Team>> searchBySports(String sports) {

        return teamRepository.findAllBySports(sports)
            .map(team -> Header.OK(team))
            .orElseGet(
                () -> Header.ERROR("조건에 맞는 팀이 없습니다.")
            );
    }

    public Header<TeamApiResponse> update(Header<TeamApiRequest> request) {

        TeamApiRequest teamApiRequest = request.getData();

        return teamRepository.findByName(teamApiRequest.getName())
            .map(team -> {
                team.updateTeam(teamApiRequest);
                return team;
            })
            .map(team -> teamRepository.save(team))
            .map(team -> response(team))
            .orElseGet(() -> Header.ERROR("존재하지 않는 팀입니다."));

    }

    public Header delete(String name) {

        return teamRepository.findByName(name)
            .map(team -> {
                teamRepository.delete(team);
                return Header.OK();
            })
            .orElseGet(() -> Header.ERROR("존재하지 않는 팀입니다."));
    }

    private Header<TeamApiResponse> response(Team team) {

        TeamApiResponse teamApiResponse = TeamApiResponse.builder()
            .id(team.getId())
            .name(team.getName())
            .area(team.getArea())
            .sports(team.getSports())
            .build();

        return Header.OK(teamApiResponse);
    }
}
