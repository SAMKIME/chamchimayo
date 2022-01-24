package kr.slub.chamchimayo.dto.request;

import kr.slub.chamchimayo.entity.Team;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TeamRequest {

    private Long id;

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
