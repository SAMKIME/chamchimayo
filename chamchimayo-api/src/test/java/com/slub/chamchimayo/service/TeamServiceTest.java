package com.slub.chamchimayo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.slub.chamchimayo.dto.request.TeamRequest;
import com.slub.chamchimayo.dto.response.TeamResponse;
import com.slub.chamchimayo.entity.Team;
import com.slub.chamchimayo.repository.TeamRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    private TeamRequest teamRequest = TeamRequest.builder()
        .id(1L)
        .name("samkime")
        .region("서울")
        .sports("축구")
        .build();

    @Test
    @DisplayName("팀 생성 테스트")
    void create() {
        // given
        given(teamRepository.save(any())).willReturn(teamRequest.toEntity());

        // when
        TeamResponse teamResponse = teamService.create(teamRequest);

        //then
        assertEquals(teamRequest.getName(), teamResponse.getName());
    }

    @Test
    @DisplayName("팀 ID로 조회 테스트")
    void searchById() {
        // given
        given(teamRepository.findById(any())).willReturn(Optional.of(teamRequest.toEntity()));

        // when
        TeamResponse teamResponse = teamService.searchById(1L);

        // then
        assertEquals(teamRequest.getName(), teamResponse.getName());
    }

    @Test
    @DisplayName("팀명 업데이트 테스트")
    void updateTeamName() {
        // given
        TeamRequest updateTeamNameRequest = TeamRequest.builder()
            .id(1L)
            .name("samkime2")
            .region("서울")
            .sports("축구")
            .build();
        given(teamRepository.findById(any())).willReturn(Optional.of(teamRequest.toEntity()));
        given(teamRepository.save(any())).willReturn(updateTeamNameRequest.toEntity());

        // when
        TeamResponse teamResponse = teamService.updateTeamName(updateTeamNameRequest);

        // then
        assertEquals(updateTeamNameRequest.getName(), teamResponse.getName());
    }
}