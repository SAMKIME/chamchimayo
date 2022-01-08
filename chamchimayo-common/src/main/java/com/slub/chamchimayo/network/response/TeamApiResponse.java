package com.slub.chamchimayo.network.response;

import com.slub.chamchimayo.entity.Team;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TeamApiResponse {

    private Long id;

    private String name;

    private String area;

    private String sports;

    private List<Team> teamList;
}
