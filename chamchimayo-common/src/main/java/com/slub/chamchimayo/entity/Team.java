package com.slub.chamchimayo.entity;

import com.slub.chamchimayo.dto.request.TeamRequest;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String sports;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
    private List<UserTeam> userList = new ArrayList<>();

    @Builder
    public Team(String name, String region, String sports) {
        this.name = name;
        this.region = region;
        this.sports = sports;
    }

    public void updateTeamName(TeamRequest teamRequest) {
        this.name = teamRequest.getName();
    }
}
