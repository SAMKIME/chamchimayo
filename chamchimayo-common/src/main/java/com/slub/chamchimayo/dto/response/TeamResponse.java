package com.slub.chamchimayo.dto.response;

import com.slub.chamchimayo.entity.Team;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TeamResponse {

    private Long id;

    private String name;

    private String region;

    private String sports;

    public static TeamResponse from(Team team) {
        return TeamResponse.builder()
            .id(team.getId())
            .name(team.getName())
            .region(team.getRegion())
            .sports(team.getSports())
            .build();
    }
}
