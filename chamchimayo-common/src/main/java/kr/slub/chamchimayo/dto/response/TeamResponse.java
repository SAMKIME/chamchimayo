package kr.slub.chamchimayo.dto.response;

import kr.slub.chamchimayo.entity.Team;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TeamResponse {

    private Long id;

    private String name;

    private String area;

    private String sports;

    public static TeamResponse from(Team team) {
        return TeamResponse.builder()
            .id(team.getId())
            .name(team.getName())
            .area(team.getArea())
            .sports(team.getSports())
            .build();
    }
}
