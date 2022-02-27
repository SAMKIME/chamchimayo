package com.slub.chamchimayo.dto.request;

import com.slub.chamchimayo.entity.Team;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TeamRequest {

    private Long id;

    private String name;

    private String region;

    private String sports;

    public Team toEntity() {
        return Team.builder()
            .name(this.name)
            .region(this.region)
            .sports(this.sports)
            .build();
    }
}
