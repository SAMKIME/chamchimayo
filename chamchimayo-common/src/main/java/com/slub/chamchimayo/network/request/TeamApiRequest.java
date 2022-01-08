package com.slub.chamchimayo.network.request;

import com.slub.chamchimayo.entity.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TeamApiRequest {

    private String name;

    private String area;

    private String sports;

    public Team toEntity() {
        return Team.builder()
            .name(this.name)
            .area(this.area)
            .sports(this.sports)
            .build();
    }
}
